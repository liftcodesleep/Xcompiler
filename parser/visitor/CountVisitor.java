package visitor;

import ast.AST;

public class CountVisitor extends ASTVisitor {

  private int[] nodeCount = new int[100];
  private int depth = 0;
  private int maxDepth = 0;

  private void count(AST t) {
    nodeCount[depth]++;

    if (depth > maxDepth) {
      maxDepth = depth;
    }

    depth++;
    visitKids(t);
    depth--;
  }

  public int[] getCount() {
    int[] count = new int[maxDepth + 1];

    for (int i = 0; i <= maxDepth; i++) {
      count[i] = nodeCount[i];
    }

    return count;
  }

  public void printCount() {
    for (int i = 0; i <= maxDepth; i++) {
      System.out.println("Depth: " + i + " Nodes: " + nodeCount[i]);
    }
  }

  public Object visitProgramTree(AST t) {
    count(t);
    return null;
  }

  public Object visitBlockTree(AST t) {
    count(t);
    return null;
  }

  public Object visitFunctionDeclTree(AST t) {
    count(t);
    return null;
  }

  public Object visitCallTree(AST t) {
    count(t);
    return null;
  }

  public Object visitDeclTree(AST t) {
    count(t);
    return null;
  }

  public Object visitIntTypeTree(AST t) {
    count(t);
    return null;
  }

  public Object visitBoolTypeTree(AST t) {
    count(t);
    return null;
  }

  public Object visitFormalsTree(AST t) {
    count(t);
    return null;
  }

  public Object visitActualArgsTree(AST t) {
    count(t);
    return null;
  }

  public Object visitIfTree(AST t) {
    count(t);
    return null;
  }

  public Object visitWhileTree(AST t) {
    count(t);
    return null;
  }

  public Object visitForTree(AST t) {
    count(t);
    return null;
  }

  public Object visitReturnTree(AST t) {
    count(t);
    return null;
  }

  public Object visitAssignTree(AST t) {
    count(t);
    return null;
  }

  public Object visitIntTree(AST t) {
    count(t);
    return null;
  }

  public Object visitFloatTree(AST t) {
    count(t);
    return null;
  }

  public Object visitIdTree(AST t) {
    count(t);
    return null;
  }

  public Object visitRelOpTree(AST t) {
    count(t);
    return null;
  }

  public Object visitAddOpTree(AST t) {
    count(t);
    return null;
  }

  public Object visitMultOpTree(AST t) {
    count(t);
    return null;
  }

  public Object visitStringTree(AST t) {
    count(t);
    return null;
  }

  public Object visitStringTypeTree(AST t) {
    count(t);
    return null;
  }

  public Object visitScientificTree(AST t) {
    count(t);
    return null;
  }

  public Object visitScientificTypeTree(AST t) {
    count(t);
    return null;
  }

  public Object visitRangeExpTree(AST t) {
    count(t);
    return null;
  }

  public Object visitForAllTree(AST t) {
    count(t);
    return null;
  }

}
