package tests.helpers;

import java.util.ArrayList;
import java.util.List;

import lexer.Token;
import tests.ILexer;

public class TestLexer implements ILexer {
    private List<Token> tokens;
    private int index;

    public TestLexer() {
        tokens = new ArrayList<Token>();
        index = 0;
    }

    public TestLexer(List<Token> tokens) {
        this.tokens = tokens;
        index = 0;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }
    
    public Token nextToken() {
        if(index == tokens.size()) {
            return null;
        } else {
            return tokens.get(index++);
        }
    }
}
