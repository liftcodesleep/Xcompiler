package ast;

import visitor.*;

public class ScientificTypeTree extends AST {

  public Object accept(ASTVisitor visitor) {
    return visitor.visitScientificTypeTree(this);
  }

}
