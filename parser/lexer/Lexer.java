package lexer;

import lexer.readers.IReader;
import lexer.readers.SourceReader;
import tests.ILexer;

public class Lexer implements ILexer {
  private boolean atEOF = false;

  private char ch;
  private IReader source;

  private int startPosition, endPosition;

  /**
   * Lexer constructor
   * 
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer(String sourceFile) throws Exception {

    new TokenType();
    source = new SourceReader(sourceFile);
    nextChar();
  }

  public String toString() {
    return source.toString();
  }

  public Lexer(IReader reader) throws Exception {

    new TokenType();
    this.source = reader;
    nextChar();
  }

  private void printErrorMSG(String illString) {
    System.out.println("******** illegal character: " + illString);
    nextChar();
  }

  public Token newIdToken(String id, int startPosition, int endPosition) {
    return new Token(startPosition, endPosition, Symbol.symbol(id, Tokens.Identifier),
        source.getLineno());
  }

  public Token stringLitToken(String id, int startPosition, int endPosition) {
    return new Token(startPosition, endPosition, Symbol.symbol(id, Tokens.StringLit),
        source.getLineno());
  }

  public Token scientificLitToken(String id, int startPosition, int endPosition) {
    return new Token(startPosition, endPosition, Symbol.symbol(id, Tokens.ScientificLit),
        source.getLineno());
  }

  public Token newNumberToken(String number, int startPosition, int endPosition) {
    return new Token(startPosition, endPosition, Symbol.symbol(number, Tokens.INTeger),
        source.getLineno());
  }

  public Token makeToken(String tokenString, int startPosition, int endPosition) {

    if (tokenString.equals("//")) {
      try {
        int oldLine = source.getLineno();

        do {
          nextChar();
        } while (oldLine == source.getLineno());
      } catch (Exception e) {
        atEOF = true;
      }

      return nextToken();
    }

    Symbol symbol = Symbol.symbol(tokenString, Tokens.BogusToken);

    if (symbol == null) {
      printErrorMSG(tokenString);
      atEOF = true;
      return nextToken();
    }

    return new Token(startPosition, endPosition, symbol, source.getLineno());
  }

  public void nextChar() {
    try {
      ch = source.read();
    } catch (Exception e) {
      atEOF = true;
    }
  }

  public Token nextToken() {
    if (atEOF) {
      if (source != null) {
        source.close();

      }
      return null;
    }
    while (Character.isWhitespace(ch)) {
      nextChar();
      if (atEOF) {
        return nextToken();
      }
    }

    startPosition = source.getPosition();
    endPosition = startPosition - 1;

    if (Character.isJavaIdentifierStart(ch)) {

      String id = "";
      do {
        endPosition++;
        id += ch;
        nextChar();
      } while (Character.isJavaIdentifierPart(ch));

      return newIdToken(id, startPosition, endPosition);
    }

    else if (ch == '\"') {
      String literal = "";
      int initLine = source.getLineno();
      while (true) {

        endPosition++;
        nextChar();

        if (ch == '\"') {
          endPosition++;
          nextChar();
          return stringLitToken(literal, startPosition, endPosition);
        }
        literal += ch;

        if (initLine != source.getLineno()) {
          printErrorMSG("\n");
          return this.nextToken();
        }

        if (ch == 0) {
          printErrorMSG(literal);
          return this.nextToken();
        }
      }
    }

    else if (Character.isDigit(ch)) {
      String number = "";
      endPosition++;
      number += ch;
      nextChar();

      if (ch == '.') {
        endPosition++;
        number += ch;
        nextChar();

        if (Character.isDigit(ch)) {
          endPosition++;
          number += ch;
          nextChar();
        } else {
          printErrorMSG(String.valueOf(ch));
          return this.nextToken();
        }
        if (Character.isDigit(ch)) {
          endPosition++;
          number += ch;
          nextChar();
        }

        if (ch == 'e' || ch == 'E') {
          endPosition++;
          number += ch;
          nextChar();
          if (ch == '+' || ch == '-') {
            endPosition++;
            number += ch;
            nextChar();
          } else {
            printErrorMSG(String.valueOf(ch));
            return this.nextToken();
          }
        } else {
          printErrorMSG(String.valueOf(ch));
          return this.nextToken();
        }

        if (!Character.isDigit(ch)) {
          printErrorMSG(String.valueOf(ch));
          return this.nextToken();
        }
      }

      while (Character.isDigit(ch)) {
        endPosition++;
        number += ch;
        nextChar();
      }

      if (number.length() > 1 && number.charAt(1) == '.') {
        return scientificLitToken(number, startPosition, endPosition);
      } else if (!number.contains(".e") && !number.contains(".E")) {
        return newNumberToken(number, startPosition, endPosition);
      } else {
        printErrorMSG(number);
        return this.nextToken();
      }

    }

    String charOld = "" + ch;
    String op = charOld;
    Symbol sym;
    try {
      endPosition++;
      nextChar();
      op += ch;
      sym = Symbol.symbol(op, Tokens.BogusToken);
      if (sym == null) {
        return makeToken(charOld, startPosition, endPosition);
      }

      endPosition++;
      nextChar();

      return makeToken(op, startPosition, endPosition);
    } catch (Exception e) {
      /* no-op */ }

    atEOF = true;
    if (startPosition == endPosition) {
      op = charOld;
    }

    return

    makeToken(op, startPosition, endPosition);
  }

  public static void main(String args[]) {
    Token token;

    try {
      Lexer lex = new Lexer(args[0]);

      while (true) {
        token = lex.nextToken();
        if (token == null) {
          break;
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}