package ast;

import java.util.*;
import visitor.*;

public abstract class AST {

  protected ArrayList<AST> kids;
  protected int nodeNum;
  protected AST decoration;
  protected String label = "";

  static int NodeCount = 0;

  public AST() {
    kids = new ArrayList<AST>();
    NodeCount++;
    nodeNum = NodeCount;
  }

  public void setDecoration(AST t) {
    decoration = t;
  }

  public AST getDecoration() {
    return decoration;
  }

  public int getNodeNum() {
    return nodeNum;
  }

  public AST getKid(int i) {
    if ((i <= 0) || (i > kidCount())) {
      return null;
    }
    return kids.get(i - 1);
  }

  public int kidCount() {
    return kids.size();
  }

  public ArrayList<AST> getKids() {
    return kids;
  }

  public abstract Object accept(ASTVisitor v);

  public AST addKid(AST kid) {
    kids.add(kid);
    return this;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
