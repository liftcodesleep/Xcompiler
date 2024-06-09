package visitor;

import java.util.HashMap;
import ast.*;

public class OffsetVisitor extends ASTVisitor {

  private static java.util.HashMap<AST, OffsetInfo> offsets = new java.util.HashMap<AST, OffsetInfo>();
  private int[] offsetByDepth = new int[100];
  private int maxDepth = 0;
  private int depth = 0;
  private int maxOffset;
  private int workingOffset;
  private int[] nodeCount = new int[100];

  private int mapOffset(AST treeNode) {
    if (depth > maxDepth) {
      maxDepth = depth;
    }
    depth++;
    visitKids(treeNode);
    depth--;
    if (treeNode.kidCount() == 0) {
      workingOffset = offsetByDepth[depth];
      pushOffset(treeNode);
      updateOffset(2);
    } else {
      int leftOffest = offsets.get(treeNode.getKid(1)).getOffset();
      int rightOffset = offsets.get(treeNode.getKid(treeNode.kidCount())).getOffset();

      workingOffset = (leftOffest + rightOffset) / 2;

      if (workingOffset < offsetByDepth[depth]) {
        shiftKids((offsetByDepth[depth] - workingOffset), treeNode);
        workingOffset = offsetByDepth[depth];
      } else {
        updateOffset(1);
      }

      pushOffset(treeNode);
      updateOffset(2);
    }
    return depth;
  }

  private void pushOffset(AST treeNode) {
    OffsetInfo offset = new OffsetInfo(depth, workingOffset);
    offsets.put(treeNode, offset);
    nodeCount[depth]++;
  }

  private void updateOffset(int update) {
    offsetByDepth[depth] += update;
    if (offsetByDepth[depth] > maxOffset) {
      maxOffset = offsetByDepth[depth];
    }
  }

  private void shiftKids(int shift, AST treeNode) {
    depth++;
    for (AST kid : treeNode.getKids()) {
      shiftKids(shift, kid);
      updateOffset(2);
      offsets.get(kid).setOffset(offsets.get(kid).getOffset() + shift);
    }
    depth--;
    return;
  }

  public HashMap<AST, OffsetInfo> getOffsets() {
    return offsets;
  }

  public int[] getNodeCount() {
    int[] count = new int[maxDepth + 1];

    for (int i = 0; i <= maxDepth; i++) {
      count[i] = nodeCount[i];
    }

    return count;
  }

  public int getMaxOffset() {
    return maxOffset;
  }

  public Object visitProgramTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitBlockTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitFunctionDeclTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitCallTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitDeclTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitIntTypeTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitBoolTypeTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitFormalsTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitActualArgsTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitIfTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitWhileTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitReturnTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitAssignTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitIntTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitIdTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitRelOpTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitAddOpTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitMultOpTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitStringTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitStringTypeTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitScientificTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitScientificTypeTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitForAllTree(AST t) {
    mapOffset(t);
    return null;
  }

  public Object visitRangeExpTree(AST t) {
    mapOffset(t);
    return null;
  }

}
