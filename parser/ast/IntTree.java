package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class IntTree extends AST {

  private Symbol symbol;

  public IntTree(Token token) {
    this.symbol = token.getSymbol();
  }

  public Object accept(ASTVisitor visitor) {
    return visitor.visitIntTree(this);
  }

  public Symbol getSymbol() {
    return symbol;
  }
}
