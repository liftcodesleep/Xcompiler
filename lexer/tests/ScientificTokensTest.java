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

public class ScientificTokensTest {
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
    return Stream.of(Arguments.of("1.1e+1"), Arguments.of("1.12E-1"), Arguments.of("2.34e+12345"));
  }

  @ParameterizedTest
  @MethodSource("provideValidTokenSources")
  void testValidScientificTokens(String source) {
    try {
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));
      Token token = lexer.nextToken();

      assertEquals(Tokens.ScientificLit, token.getKind());
      assertEquals(source, token.getSymbol().toString());
      assertEquals(1, token.getLeftPosition());
      assertEquals(source.length(), token.getRightPosition());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void testInvalidPrefixedToken() {
    try {
      String source = "12.12E+123";
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));
      Token token = lexer.nextToken();

      assertEquals(Tokens.INTeger, token.getKind());
      assertEquals("12", token.getSymbol().toString());

      // This should create an invalid token .
      token = lexer.nextToken();
      assertEquals(Helpers.getTestOutput(source, "."), outputStreamCaptor.toString());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static Stream<Arguments> provideInvalidTokenSources() {
    return Stream.of(Arguments.of("1.123E+123", "3"), Arguments.of("1.12A+123", "A"),
        Arguments.of("1.12ez123", "z"), Arguments.of("1.12e123", "1"), Arguments.of("1.12e+", " "));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTokenSources")
  void testInvalidDecimalPartToken(String source, String expectedChar) {
    try {
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));

      // This should create an invalid token 3
      lexer.nextToken();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
