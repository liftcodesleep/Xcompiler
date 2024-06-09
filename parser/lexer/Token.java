package lexer;

public class Token {
  private int leftPosition, rightPosition, lineNumber;
  private Symbol symbol;

  public Token(int leftPosition, int rightPosition, Symbol symbol) {
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.symbol = symbol;
    this.lineNumber = -1;
  }

  public Token(int leftPosition, int rightPosition, Symbol symbol, int LINE_NUMBER) {
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.symbol = symbol;
    this.lineNumber = LINE_NUMBER;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public String toString() {
    return ("+           left: " + leftPosition + "        right: " + rightPosition
        + "       line: " + lineNumber + "     " + symbol.getKind());
  }

  public void print() {
    System.out.println(this);
  }

  public int getLeftPosition() {
    return leftPosition;
  }

  public int getRightPosition() {
    return rightPosition;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public Tokens getKind() {
    return symbol.getKind();
  }
}
