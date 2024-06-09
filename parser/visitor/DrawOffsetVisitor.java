package visitor;

import ast.*;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class DrawOffsetVisitor extends ASTVisitor {
  private HashMap<AST, OffsetInfo> offsets;

  private final int nodeWidth = 100;
  private final int nodeHeight = 30;
  private final int vertSep = 50;
  private final int horizSep = 10;

  private int width;
  private int height;
  private int maxOffset;

  private int[] nodeCount;
  private int[] progress;
  private int depth = 0;
  private BufferedImage bimg;

  private Graphics2D g2;

  public DrawOffsetVisitor(java.util.HashMap<AST, OffsetInfo> offsets, int[] nodeCount, int maxOffset) {
    this.offsets = offsets;
    this.nodeCount = nodeCount;
    this.maxOffset = maxOffset;
    width = maxOffset * (nodeWidth + horizSep) + 10;
    height = nodeCount.length * (nodeHeight + vertSep);
    progress = new int[nodeCount.length];
    g2 = createGraphics2D();
  }

  private void drawOffset(String string, AST treeNode) {
    int hstep = nodeWidth + horizSep;
    int vstep = nodeHeight + vertSep;
    int x = offsets.get(treeNode).getOffset() * hstep;
    int y = depth * vstep;

    g2.setColor(Color.BLACK);
    g2.drawOval(x, y, nodeWidth, nodeHeight);
    g2.setColor(Color.BLACK);
    g2.drawString(string, x + 10, y + 2 * nodeHeight / 3);

    int startx = x + nodeWidth / 2;
    int starty = y + nodeHeight;
    int endx;
    int endy;
    g2.setColor(Color.BLACK);
    for (int i = 0; i < treeNode.kidCount(); i++) {
      endx = (offsets.get(treeNode.getKid(i + 1)).getOffset() * hstep) + nodeWidth / 2;
      endy = (depth + 1) * vstep;
      g2.drawLine(startx, starty, endx, endy);
    }
    progress[depth]++;
    depth++;
    visitKids(treeNode);
    depth--;
  }

  private Graphics2D createGraphics2D() {
    if (bimg == null || bimg.getWidth() != width || bimg.getHeight() != height) {
      bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    g2 = bimg.createGraphics();
    g2.setBackground(Color.WHITE);
    g2.setRenderingHint(
        RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.clearRect(0, 0, width, height);

    return g2;
  }

  public BufferedImage getImage() {
    return bimg;
  }

  public Object visitProgramTree(AST treeNode) {
    drawOffset("Program", treeNode);
    return null;
  }

  public Object visitBlockTree(AST treeNode) {
    drawOffset("Block", treeNode);
    return null;
  }

  public Object visitFunctionDeclTree(AST treeNode) {
    drawOffset("FunctionDecl", treeNode);
    return null;
  }

  public Object visitCallTree(AST treeNode) {
    drawOffset("Call", treeNode);
    return null;
  }

  public Object visitDeclTree(AST treeNode) {
    drawOffset("Decl", treeNode);
    return null;
  }

  public Object visitIntTypeTree(AST treeNode) {
    drawOffset("IntType", treeNode);
    return null;
  }

  public Object visitBoolTypeTree(AST treeNode) {
    drawOffset("BoolType", treeNode);
    return null;
  }

  public Object visitFormalsTree(AST treeNode) {
    drawOffset("Formals", treeNode);
    return null;
  }

  public Object visitActualArgsTree(AST treeNode) {
    drawOffset("ActualArgs", treeNode);
    return null;
  }

  public Object visitIfTree(AST treeNode) {
    drawOffset("If", treeNode);
    return null;
  }

  public Object visitWhileTree(AST treeNode) {
    drawOffset("While", treeNode);
    return null;
  }

  public Object visitReturnTree(AST treeNode) {
    drawOffset("Return", treeNode);
    return null;
  }

  public Object visitAssignTree(AST treeNode) {
    drawOffset("Assign", treeNode);
    return null;
  }

  public Object visitIntTree(AST treeNode) {
    drawOffset("Int: " + ((IntTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitIdTree(AST treeNode) {
    drawOffset("Id: " + ((IdTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitRelOpTree(AST treeNode) {
    drawOffset("RelOp: " + ((RelOpTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitAddOpTree(AST treeNode) {
    drawOffset("AddOp: " + ((AddOpTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitMultOpTree(AST treeNode) {
    drawOffset("MultOp: " + ((MultOpTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitStringTree(AST treeNode) {
    drawOffset("String: " + ((StringTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitStringTypeTree(AST treeNode) {
    drawOffset("StringType ", treeNode);
    return null;
  }

  public Object visitScientificTree(AST treeNode) {
    drawOffset("Scientific: " + ((ScientificTree) treeNode).getSymbol().toString(), treeNode);
    return null;
  }

  public Object visitScientificTypeTree(AST treeNode) {
    drawOffset("ScientificType ", treeNode);
    return null;
  }

  public Object visitForAllTree(AST treeNode) {
    drawOffset("ForAll ", treeNode);
    return null;
  }

  public Object visitRangeExpTree(AST treeNode) {
    drawOffset("Range ", treeNode);
    return null;
  }
}
