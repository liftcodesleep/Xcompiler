package lexer;

import lexer.readers.IReader;
import lexer.readers.SourceReader;

public class Lexer {
  private boolean atEOF = false;
  // next character to process
  private char ch;
  private IReader source;

  // positions in line of current token
  private int startPosition, endPosition;

  /**
   * Lexer constructor
   * 
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer(String sourceFile) throws Exception {
    // init token table
    new TokenType();
    source = new SourceReader(sourceFile);
    nextChar();
  }

  public Lexer(IReader reader) throws Exception {
    // init token table
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
    // filter comments
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
        // source = null;
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
      // return tokens for ids and reserved words
      String id = "";
      do {
        endPosition++;
        id += ch;
        nextChar();
      } while (Character.isJavaIdentifierPart(ch));

      return newIdToken(id, startPosition, endPosition);
    }
    // open quote might be string lit
    else if (ch == '\"') {
      String literal = "";
      int initLine = source.getLineno();
      while (true) {
        // find characters until terminal condition is met
        endPosition++;
        nextChar();
        // if closing quote
        if (ch == '\"') {
          endPosition++;
          nextChar();
          return stringLitToken(literal, startPosition, endPosition);
        }
        literal += ch;
        // if newline
        if (initLine != source.getLineno()) {
          printErrorMSG("\n");
          return this.nextToken();
        }
        // if end of file
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
      // if period might be SciLit
      if (ch == '.') {
        endPosition++;
        number += ch;
        nextChar();
        // needs one digit to follow
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
        // An E must follow
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
        // One or more digits must follow
        if (!Character.isDigit(ch)) {
          printErrorMSG(String.valueOf(ch));
          return this.nextToken();
        }
      }
      // get the rest of the digits for SciLit or INT
      while (Character.isDigit(ch)) {
        endPosition++;
        number += ch;
        nextChar();
      }
      // check for SciLit
      if (number.charAt(1) == '.') {
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
        // it must be a one char token
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

  private static String pad(String value, int width) {
    int space = width - value.toString().length();

    for (int i = 0; i < space; i++) {
      value += " ";
    }

    return value.substring(0, width);
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

        String symbol = String.valueOf(token.getSymbol());
        String left = String.valueOf(token.getLeftPosition());
        String right = String.valueOf(token.getRightPosition());
        String lineNum = String.valueOf(token.getLineNumber());

        System.out.print(pad(symbol, 11) + " left: " + pad(right, 8) + " right: " +
            pad(left, 8) + " line: " + pad(lineNum, 8) + " " + token.getKind() + "\n");

      }
      System.out.println(lex.source.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}