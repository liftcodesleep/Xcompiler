package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class MultOpTree extends AST {

  private Symbol symbol;

  public MultOpTree(Token token) {
    this.symbol = token.getSymbol();
  }

  public Object accept(ASTVisitor visitor) {
    return visitor.visitMultOpTree(this);
  }

  public Symbol getSymbol() {
    return symbol;
  }
}
