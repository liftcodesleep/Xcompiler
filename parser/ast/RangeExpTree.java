package ast;

import visitor.*;

public class RangeExpTree extends AST {
  public Object accept(ASTVisitor visitor) {
    return visitor.visitRangeExpTree(this);
  }
}
