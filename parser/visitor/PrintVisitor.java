package visitor;

import ast.*;

public class PrintVisitor extends ASTVisitor {

  private int indent = 0;

  private void printSpaces(int num) {
    StringBuffer spaces = new StringBuffer("");

    for (int i = 0; i < num; i++) {
      spaces.append(" ");
    }

    System.out.print(spaces);
  }

  public void print(String s, AST t) {
    int num = t.getNodeNum();
    AST decoration = t.getDecoration();
    int decNum = (decoration == null) ? -1 : decoration.getNodeNum();
    StringBuffer name = new StringBuffer(s);
    StringBuffer spaces = new StringBuffer("");

    if (num < 100)
      spaces.append(" ");
    if (num < 10)
      spaces.append(" ");

    System.out.print(num + ":" + spaces);
    printSpaces(indent);

    if (decNum != -1) {
      name.append("           Dec: " + decNum);
    }

    String label = t.getLabel();
    if (label.length() > 0) {
      name.append("  Label: " + t.getLabel());
    }

    if (t.getClass() == IdTree.class) {
      int offset = ((IdTree) t).getFrameOffset();

      if (offset >= 0) {
        name.append("  Addr: " + offset);
      }
    }

    System.out.println(s);

    indent += 2;
    visitKids(t);
    indent -= 2;
  }

  public Object visitProgramTree(AST t) {
    print("Program", t);
    return null;
  }

  public Object visitBlockTree(AST t) {
    print("Block", t);
    return null;
  }

  public Object visitFunctionDeclTree(AST t) {
    print("FunctionDecl", t);
    return null;
  }

  public Object visitCallTree(AST t) {
    print("Call", t);
    return null;
  }

  public Object visitDeclTree(AST t) {
    print("Decl", t);
    return null;
  }

  public Object visitIntTypeTree(AST t) {
    print("IntType", t);
    return null;
  }

  public Object visitBoolTypeTree(AST t) {
    print("BoolType", t);
    return null;
  }

  public Object visitFormalsTree(AST t) {
    print("Formals", t);
    return null;
  }

  public Object visitActualArgsTree(AST t) {
    print("ActualArgs", t);
    return null;
  }

  public Object visitIfTree(AST t) {
    print("If", t);
    return null;
  }

  public Object visitWhileTree(AST t) {
    print("While", t);
    return null;
  }

  public Object visitReturnTree(AST t) {
    print("Return", t);
    return null;
  }

  public Object visitAssignTree(AST t) {
    print("Assign", t);
    return null;
  }

  public Object visitIntTree(AST t) {
    print("Int: " + ((IntTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitIdTree(AST t) {
    print("Id: " + ((IdTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitRelOpTree(AST t) {
    print("RelOp: " + ((RelOpTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitAddOpTree(AST t) {
    print("AddOp: " + ((AddOpTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitMultOpTree(AST t) {
    print("MultOp: " + ((MultOpTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitStringTree(AST t) {
    print("String: " + ((StringTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitStringTypeTree(AST t) {
    print("StringType", t);
    return null;
  }

  public Object visitScientificTree(AST t) {
    print("Scientific: " + ((ScientificTree) t).getSymbol().toString(), t);
    return null;
  }

  public Object visitScientificTypeTree(AST t) {
    print("ScientificType", t);
    return null;
  }

  public Object visitRangeExpTree(AST t) {
    print("RangeExp", t);
    return null;
  }

  public Object visitForAllTree(AST t) {
    print("Forall", t);
    return null;
  }
}
