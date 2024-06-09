package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;
import lexer.readers.SourceReader;

public class Helpers {
  public static Lexer getTestLexer(String source) throws IOException, Exception {
    return new Lexer(new SourceReader(new BufferedReader(new StringReader(source))));
  }

  public static String getTestOutput(String source, String expectedChar) {
    String separator = System.lineSeparator();

    return String.format(
        "%s %s %s******** illegal character: %s%s",
        SourceReader.READ_PREFIX,
        source,
        separator,
        expectedChar,
        separator);
  }
}