package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lexer.Lexer;
import lexer.Token;
import lexer.Tokens;
import lexer.readers.SourceReader;

public class StringTokensTest {
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
  }

  private static Stream<Arguments> provideValidTokenSources() {
    return Stream.of(
        Arguments.of("\"\"", ""),
        Arguments.of("\"a\"", "a"),
        Arguments.of("\"13\"", "123"),
        Arguments.of("\"1.12E+123\"", "1.12E+123"),
        Arguments.of("\"supercalifragilisticexpealadocious\"", "supercalifragilisticexpealadocious"),
        Arguments.of("\"string with spaces\"", "string with spaces"));
  }

  @ParameterizedTest
  @MethodSource("provideValidTokenSources")
  void testValidStringTokens(String source, String expected) {
    try {
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));
      Token token = lexer.nextToken();

      assertEquals(Tokens.StringLit, token.getKind());
      assertEquals(expected, token.getSymbol().toString());
      assertEquals(1, token.getLeftPosition());
      assertEquals(source.length(), token.getRightPosition());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void testInvalidMultiLineString() {
    try {
      String separator = System.lineSeparator();

      String sourceStart = String.format("\"123456789asdf%s", separator);
      String sourceEnd = "12\"";
      String source = sourceStart + sourceEnd;
      String expectedChar = separator;

      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));

      // This should create an invalid token
      lexer.nextToken();
      assertEquals(
          String.format(
              "%s %s%s%s %s******** illegal character: %s%s",
              SourceReader.READ_PREFIX,
              sourceStart,
              SourceReader.READ_PREFIX,
              sourceEnd,
              separator,
              expectedChar,
              separator),
          outputStreamCaptor.toString());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}