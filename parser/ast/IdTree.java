package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class IdTree extends AST {

  private Symbol symbol;
  private int frameOffset = -1;

  public IdTree(Token token) {
    this.symbol = token.getSymbol();
  }

  public Object accept(ASTVisitor visitor) {
    return visitor.visitIdTree(this);
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public void setFrameOffset(int offset) {
    frameOffset = offset;
  }

  public int getFrameOffset() {
    return frameOffset;
  }
}
