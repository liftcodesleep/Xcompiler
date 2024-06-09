package constrain;

import lexer.*;
import parser.Parser;
import visitor.*;
import ast.*;
import java.util.*;

/**
 * Constrainer object will visit the AST, gather/check variable
 * type information and decorate uses of variables with their
 * declarations; the decorations will be used by the code generator
 * to provide access to the frame offset of the variable for generating
 * load/store bytecodes; <br>
 * Note that when constraining expression trees we return the type tree
 * corresponding to the result type of the expression; e.g.
 * the result of constraining the tree for 1+2*3 will be the int type
 * tree
 */
public class Constrainer extends ASTVisitor {
  public enum ConstrainerErrors {
    BadAssignmentType, CallingNonFunction, ActualFormalTypeMismatch, NumberActualsFormalsDiffer, TypeMismatchInExpr,
    BooleanExprExpected, BadConditional, ReturnNotInFunction, BadReturnExpr
  }

  private AST t;
  private Table symtab = new Table();
  private Parser parser;

  /**
   * The following comment refers to the functions stack
   * declared below the comment.
   * Whenever we start constraining a function declaration
   * we push the function decl tree which indicates we're
   * in a function (to ensure that we don't attempt to return
   * from the main program - return's are only allowed from
   * within functions); it also gives us access to the return
   * type to ensure the type of the expr that is returned is
   * the same as the type declared in the function header
   */
  private Stack<AST> functions = new Stack<AST>();

  /**
   * readTree, writeTree, intTree, boolTree,falseTree, trueTree
   * are AST's that will be constructed (intrinsic trees) for
   * every program. They are constructed in the same fashion as
   * source program trees to ensure consisten processing of
   * functions, etc.
   */
  public static AST readTree, writeTree, intTree, boolTree,
      falseTree, trueTree, readId, writeId;

  public Constrainer(AST t, Parser parser) {
    this.t = t;
    this.parser = parser;
  }

  public void execute() {
    symtab.beginScope();
    t.accept(this);
  }

  /**
   * t is an IdTree; retrieve the pointer to its declaration
   */
  private AST lookup(AST t) {
    return (AST) (symtab.get(((IdTree) t).getSymbol()));
  }

  /**
   * Decorate the IdTree with the given decoration - its decl tree
   */
  private void enter(AST t, AST decoration) {
    /*
     * System.out.println("enter: "+((IdTree)t).getSymbol().toString()
     * + ": " + decoration.getNodeNum());
     */
    symtab.put(((IdTree) t).getSymbol(), decoration);
  }

  /**
   * get the type of the current type tree
   * 
   * @param t is the type tree
   * @return the intrinsic tree corresponding to the type of t
   */
  private AST getType(AST t) {
    return (t.getClass() == IntTypeTree.class) ? intTree : boolTree;
  }

  public void decorate(AST t, AST decoration) {
    t.setDecoration(decoration);
  }

  /**
   * @return the decoration of the tree
   */
  public AST decoration(AST t) {
    return t.getDecoration();
  }

  /**
   * build the intrinsic trees; constrain them in the same fashion
   * as any other AST
   */
  private void buildIntrinsicTrees() {
    Lexer lex = parser.getLex();
    trueTree = new IdTree(lex.newIdToken("true", -1, -1));
    falseTree = new IdTree(lex.newIdToken("false", -1, -1));
    readId = new IdTree(lex.newIdToken("read", -1, -1));
    writeId = new IdTree(lex.newIdToken("write", -1, -1));
    boolTree = (new DeclTree()).addKid(new BoolTypeTree()).addKid(new IdTree(lex.newIdToken("<<bool>>", -1, -1)));
    decorate(boolTree.getKid(2), boolTree);
    intTree = (new DeclTree()).addKid(new IntTypeTree()).addKid(new IdTree(lex.newIdToken("<<int>>", -1, -1)));
    decorate(intTree.getKid(2), intTree);

    readTree = (new FunctionDeclTree()).addKid(new IntTypeTree()).addKid(readId).addKid(new FormalsTree())
        .addKid(new BlockTree());

    writeTree = (new FunctionDeclTree()).addKid(new IntTypeTree()).addKid(writeId);
    AST decl = (new DeclTree()).addKid(new IntTypeTree()).addKid(new IdTree(lex.newIdToken("dummyFormal", -1, -1)));
    AST formals = (new FormalsTree()).addKid(decl);
    writeTree.addKid(formals).addKid(new BlockTree());
    writeTree.accept(this);
    readTree.accept(this);
  }

  /**
   * Constrain the program tree - visit its kid
   */
  public Object visitProgramTree(AST t) {
    buildIntrinsicTrees();
    this.t = t;
    t.getKid(1).accept(this);
    return null;
  }

  /**
   * Constrain the Block tree:<br>
   * <ol>
   * <li>open a new scope,
   * <li>constrain the kids in this new scope,
   * <li>close the
   * scope removing any local declarations from this scope
   * </ol>
   */
  public Object visitBlockTree(AST t) {
    symtab.beginScope();
    visitKids(t);
    symtab.endScope();
    return null;
  }

  public Object visitFunctionDeclTree(AST t) {
    AST fname = t.getKid(2),
        returnType = t.getKid(1),
        formalsTree = t.getKid(3),
        bodyTree = t.getKid(4);
    functions.push(t);
    enter(fname, t);
    decorate(returnType, getType(returnType));
    symtab.beginScope();
    visitKids(formalsTree);
    bodyTree.accept(this);
    symtab.endScope();
    functions.pop();
    return null;
  }

  public Object visitCallTree(AST t) {
    AST fct,
        fname = t.getKid(1),
        fctType;
    visitKids(t);
    fct = lookup(fname);
    if (fct.getClass() != FunctionDeclTree.class) {
      constraintError(ConstrainerErrors.CallingNonFunction);
    }
    fctType = decoration(fct.getKid(1));
    decorate(t, fctType);
    decorate(t.getKid(1), fct);

    checkArgDecls(t, fct);
    return fctType;
  }

  private void checkArgDecls(AST caller, AST fct) {

    AST formals = fct.getKid(3);
    Iterator<AST> actualKids = caller.getKids().iterator(),
        formalKids = formals.getKids().iterator();
    actualKids.next();
    for (; actualKids.hasNext();) {
      try {
        AST actualDecl = decoration(actualKids.next()),
            formalDecl = formalKids.next();
        if (decoration(actualDecl.getKid(2)) != decoration(formalDecl.getKid(2))) {
          constraintError(ConstrainerErrors.ActualFormalTypeMismatch);
        }
      } catch (Exception e) {
        constraintError(ConstrainerErrors.NumberActualsFormalsDiffer);
      }
    }
    if (formalKids.hasNext()) {
      constraintError(ConstrainerErrors.NumberActualsFormalsDiffer);
    }
    return;
  }

  public Object visitDeclTree(AST t) {
    AST idTree = t.getKid(2);
    enter(idTree, t);
    AST typeTree = getType(t.getKid(1));
    decorate(idTree, typeTree);
    return null;
  }

  public Object visitIfTree(AST t) {
    if (t.getKid(1).accept(this) != boolTree) {
      constraintError(ConstrainerErrors.BadConditional);
    }
    t.getKid(2).accept(this);
    t.getKid(3).accept(this);
    return null;
  }

  public Object visitWhileTree(AST t) {
    if (t.getKid(1).accept(this) != boolTree) {
      constraintError(ConstrainerErrors.BadConditional);
    }
    t.getKid(2).accept(this);
    return null;
  }

  public Object visitReturnTree(AST t) {
    if (functions.empty()) {
      constraintError(ConstrainerErrors.ReturnNotInFunction);
    }
    AST currentFunction = (functions.peek());
    decorate(t, currentFunction);
    AST returnType = decoration(currentFunction.getKid(1));
    if ((t.getKid(1).accept(this)) != returnType) {
      constraintError(ConstrainerErrors.BadReturnExpr);
    }
    return null;
  }

  public Object visitAssignTree(AST t) {
    AST idTree = t.getKid(1),
        idDecl = lookup(idTree),
        typeTree;
    decorate(idTree, idDecl);
    typeTree = decoration(idDecl.getKid(2));

    if ((t.getKid(2).accept(this)) != typeTree) {
      constraintError(ConstrainerErrors.BadAssignmentType);
    }
    return null;
  }

  public Object visitIntTree(AST t) {
    decorate(t, intTree);
    return intTree;
  }

  public Object visitIdTree(AST t) {
    AST decl = lookup(t);
    decorate(t, decl);
    return decoration(decl.getKid(2));
  }

  public Object visitRelOpTree(AST t) {
    AST leftOp = t.getKid(1),
        rightOp = t.getKid(2);
    if ((AST) (leftOp.accept(this)) != (AST) (rightOp.accept(this))) {
      constraintError(ConstrainerErrors.TypeMismatchInExpr);
    }
    decorate(t, boolTree);
    return boolTree;
  }

  public Object visitAddOpTree(AST t) {
    AST leftOpType = (AST) (t.getKid(1).accept(this)),
        rightOpType = (AST) (t.getKid(2).accept(this));
    if (leftOpType != rightOpType) {
      constraintError(ConstrainerErrors.TypeMismatchInExpr);
    }
    decorate(t, leftOpType);
    return leftOpType;
  }

  public Object visitMultOpTree(AST t) {
    return visitAddOpTree(t);
  }

  public Object visitIntTypeTree(AST t) {
    return null;
  }

  public Object visitBoolTypeTree(AST t) {
    return null;
  }

  public Object visitFormalsTree(AST t) {
    return null;
  }

  public Object visitActualArgsTree(AST t) {
    return null;
  }

  public Object visitStringTree(AST T) {
    return null;
  }

  public Object visitStringTypeTree(AST T) {
    return null;
  }

  public Object visitScientificTree(AST T) {
    return null;
  }

  public Object visitScientificTypeTree(AST T) {
    return null;
  }

  public Object visitForAllTree(AST T) {
    return null;
  }

  public Object visitRangeExpTree(AST T) {
    return null;
  }

  void constraintError(ConstrainerErrors err) {
    PrintVisitor v1 = new PrintVisitor();
    v1.visitProgramTree(t);
    System.out.println("****CONSTRAINER ERROR: " + err + "   ****");
    System.exit(1);
    return;
  }

}