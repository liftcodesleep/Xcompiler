package ast;

import visitor.*;

public class ForAllTree extends AST {

  public Object accept(ASTVisitor visitor) {
    return visitor.visitForAllTree(this);
  }

}
