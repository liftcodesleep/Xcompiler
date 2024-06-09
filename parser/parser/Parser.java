package parser;

import ast.*;
import java.util.*;
import lexer.*;
import tests.ILexer;

public class Parser {

  private Token currentToken;
  private ILexer lex;
  private EnumSet<Tokens> relationalOps = EnumSet.of(
      Tokens.Equal,
      Tokens.NotEqual,
      Tokens.Less,
      Tokens.LessEqual,
      Tokens.Range,
      Tokens.Greater,
      Tokens.GreaterEqual);
  private EnumSet<Tokens> addingOps = EnumSet.of(
      Tokens.Plus,
      Tokens.Minus,
      Tokens.Or);
  private EnumSet<Tokens> multiplyingOps = EnumSet.of(
      Tokens.Multiply,
      Tokens.Divide,
      Tokens.And);

  public Parser(String sourceProgram) throws Exception {
    try {
      lex = new Lexer(sourceProgram);
      scan();
    } catch (Exception e) {
      System.out.println("********exception*******" + e.toString());
      throw e;
    }
  }

  public Parser(ILexer lexer) throws Exception {
    new TokenType();
    lex = lexer;
    scan();
  }

  public Lexer getLex() {
    return (Lexer) lex;
  }

  public AST execute() throws Exception {
    try {
      return rProgram();
    } catch (SyntaxError e) {
      e.print();
      throw e;
    }
  }

  public AST rProgram() throws SyntaxError {
    AST treeNode = new ProgramTree();
    expect(Tokens.Program);
    treeNode.addKid(rBlock());
    return treeNode;
  }

  public AST rBlock() throws SyntaxError {
    expect(Tokens.LeftBrace);
    AST treeNode = new BlockTree();

    while (startingDecl()) {
      treeNode.addKid(rDecl());
    }

    while (startingStatement()) {
      treeNode.addKid(rStatement());
    }

    expect(Tokens.RightBrace);

    return treeNode;
  }

  boolean startingDecl() {
    return isNextTok(Tokens.Int) || isNextTok(Tokens.BOOLean)
        || isNextTok(Tokens.StringType) || isNextTok(Tokens.Scientific);
  }

  boolean startingStatement() {
    return (isNextTok(Tokens.If) ||
        isNextTok(Tokens.While) ||
        isNextTok(Tokens.Return) ||
        isNextTok(Tokens.LeftBrace) ||
        isNextTok(Tokens.Identifier) ||
        isNextTok(Tokens.Forall));
  }

  public AST rDecl() throws SyntaxError {
    AST treeNode, t1;
    treeNode = rType();
    t1 = rName();

    if (isNextTok(Tokens.LeftParen)) {
      treeNode = (new FunctionDeclTree()).addKid(treeNode).addKid(t1);
      treeNode.addKid(rFuncHead());
      treeNode.addKid(rBlock());
      return treeNode;
    }
    treeNode = (new DeclTree()).addKid(treeNode).addKid(t1);

    return treeNode;
  }

  public AST rType() throws SyntaxError {
    AST treeNode;

    if (isNextTok(Tokens.Int)) {
      treeNode = new IntTypeTree();
      scan();
    } else if (isNextTok(Tokens.BOOLean)) {
      expect(Tokens.BOOLean);
      treeNode = new BoolTypeTree();
    } else if (isNextTok(Tokens.StringType)) {
      expect(Tokens.StringType);
      treeNode = new StringTypeTree();
    } else {
      expect(Tokens.Scientific);
      treeNode = new ScientificTypeTree();
    }
    return treeNode;
  }

  public AST rFuncHead() throws SyntaxError {
    AST treeNode = new FormalsTree();
    expect(Tokens.LeftParen);

    if (!isNextTok(Tokens.RightParen)) {
      do {
        treeNode.addKid(rDecl());
        if (isNextTok(Tokens.Comma)) {
          scan();
        } else {
          break;
        }
      } while (true);
    }

    expect(Tokens.RightParen);

    return treeNode;
  }

  public AST rRangeExp() throws SyntaxError {
    AST treeNode = new RangeExpTree();
    scan();
    treeNode.addKid(rFactor());
    expect(Tokens.Range);
    treeNode.addKid(rFactor());
    expect(Tokens.RightBracket);
    return treeNode;
  }

  public AST rStatement() throws SyntaxError {
    AST treeNode;

    if (isNextTok(Tokens.If)) {
      scan();
      treeNode = new IfTree();

      treeNode.addKid(rExpr());

      expect(Tokens.Then);
      treeNode.addKid(rBlock());

      if (isNextTok(Tokens.Else)) {
        scan();
        treeNode.addKid(rBlock());
      }
      return treeNode;
    } else if (isNextTok(Tokens.While)) {
      scan();
      treeNode = new WhileTree();

      treeNode.addKid(rExpr());
      treeNode.addKid(rBlock());

      return treeNode;

    } else if (isNextTok(Tokens.Forall)) {
      scan();
      treeNode = new ForAllTree();
      treeNode.addKid(rDecl());
      expect(Tokens.In);
      treeNode.addKid(rRangeExp());
      treeNode.addKid(rBlock());
      return treeNode;

    } else if (isNextTok(Tokens.Return)) {
      scan();
      treeNode = new ReturnTree();

      treeNode.addKid(rExpr());

      return treeNode;
    } else if (isNextTok(Tokens.LeftBrace)) {
      return rBlock();
    }

    treeNode = rName();
    treeNode = (new AssignTree()).addKid(treeNode);

    expect(Tokens.Assign);

    treeNode.addKid(rExpr());

    return treeNode;
  }

  public AST rExpr() throws SyntaxError {
    AST treeNode, kid = rSimpleExpr();

    treeNode = getRelationTree();
    if (treeNode == null) {
      return kid;
    }

    treeNode.addKid(kid);
    treeNode.addKid(rSimpleExpr());

    return treeNode;
  }

  public AST rSimpleExpr() throws SyntaxError {
    AST treeNode, kid = rTerm();

    while ((treeNode = getAddOperTree()) != null) {
      treeNode.addKid(kid);
      treeNode.addKid(rTerm());

      kid = treeNode;
    }

    return kid;
  }

  public AST rTerm() throws SyntaxError {
    AST treeNode, kid = rFactor();

    while ((treeNode = getMultOperTree()) != null) {
      treeNode.addKid(kid);
      treeNode.addKid(rFactor());

      kid = treeNode;
    }

    return kid;
  }

  public AST rFactor() throws SyntaxError {
    AST treeNode;

    if (isNextTok(Tokens.LeftParen)) {
      scan();
      treeNode = rExpr();
      expect(Tokens.RightParen);
      return treeNode;
    } else if (isNextTok(Tokens.INTeger)) {
      treeNode = new IntTree(currentToken);
      scan();
      return treeNode;
    } else if (isNextTok(Tokens.StringLit)) {
      treeNode = new StringTree(currentToken);
      scan();
      return treeNode;
    } else if (isNextTok(Tokens.ScientificLit)) {
      treeNode = new ScientificTree(currentToken);
      scan();
      return treeNode;
    }

    treeNode = rName();
    if (!isNextTok(Tokens.LeftParen)) {
      return treeNode;
    }

    scan();
    treeNode = (new CallTree()).addKid(treeNode);

    if (!isNextTok(Tokens.RightParen)) {
      do {
        treeNode.addKid(rExpr());
        if (isNextTok(Tokens.Comma)) {
          scan();
        } else {
          break;
        }
      } while (true);
    }
    expect(Tokens.RightParen);

    return treeNode;
  }

  public AST rName() throws SyntaxError {
    AST treeNode;

    if (isNextTok(Tokens.Identifier)) {
      treeNode = new IdTree(currentToken);
      scan();

      return treeNode;
    }
    throw new SyntaxError(currentToken, Tokens.Identifier);
  }

  private AST getRelationTree() {
    Tokens kind = currentToken.getKind();

    if (relationalOps.contains(kind)) {
      AST treeNode = new RelOpTree(currentToken);
      scan();

      return treeNode;
    } else {
      return null;
    }
  }

  private AST getAddOperTree() {
    Tokens kind = currentToken.getKind();

    if (addingOps.contains(kind)) {
      AST treeNode = new AddOpTree(currentToken);
      scan();

      return treeNode;
    } else {
      return null;
    }
  }

  private AST getMultOperTree() {
    Tokens kind = currentToken.getKind();

    if (multiplyingOps.contains(kind)) {
      AST treeNode = new MultOpTree(currentToken);
      scan();

      return treeNode;
    } else {
      return null;
    }
  }

  private boolean isNextTok(Tokens kind) {
    return currentToken != null && currentToken.getKind() == kind;
  }

  private void expect(Tokens kind) throws SyntaxError {
    if (isNextTok(kind)) {
      scan();

      return;
    }
    throw new SyntaxError(currentToken, kind);
  }

  private void scan() {
    currentToken = lex.nextToken();
    return;
  }
}

class SyntaxError extends Exception {

  private static final long serialVersionUID = 1L;
  private Token tokenFound;
  private Tokens kindExpected;

  public SyntaxError(Token tokenFound, Tokens kindExpected) {
    this.tokenFound = tokenFound;
    this.kindExpected = kindExpected;
  }

  void print() {
    System.out.println("Expected: " + kindExpected);
    return;
  }

  @Override
  public String toString() {
    return String.format("Expected [%s], found [%s]", kindExpected, tokenFound);
  }
}
