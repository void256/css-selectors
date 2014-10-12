package se.fishtank.css.selectors.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {

    @Test
    public void testTokenizeWhitespace() {
        Tokenizer tokenizer = new Tokenizer("   \t");
        Token token = tokenizer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.type);
        assertEquals(TokenType.EOF, tokenizer.nextToken().type);
    }

    @Test
    public void testTokenizeString() {
        Tokenizer tokenizer1 = new Tokenizer("'foo'");
        Token token1 = tokenizer1.nextToken();
        assertEquals(TokenType.STRING, token1.type);
        assertEquals("foo", token1.value);
        assertEquals(TokenType.EOF, tokenizer1.nextToken().type);

        Tokenizer tokenizer2 = new Tokenizer("'foo\\\nbar'");
        Token token2 = tokenizer2.nextToken();
        assertEquals(TokenType.STRING, token2.type);
        assertEquals("foobar", token2.value);
        assertEquals(TokenType.EOF, tokenizer2.nextToken().type);

        Tokenizer tokenizer3 = new Tokenizer("'foo\nbar'");
        assertEquals(TokenType.ERROR, tokenizer3.nextToken().type);

        Tokenizer tokenizer4 = new Tokenizer("'b\\00e4 r'");
        Token token4 = tokenizer4.nextToken();
        assertEquals(TokenType.STRING, token2.type);
        assertEquals("bär", token4.value);
        assertEquals(TokenType.EOF, tokenizer4.nextToken().type);

        Tokenizer tokenizer5 = new Tokenizer("'r\\0000e4d'");
        Token token5 = tokenizer5.nextToken();
        assertEquals(TokenType.STRING, token2.type);
        assertEquals("räd", token5.value);
        assertEquals(TokenType.EOF, tokenizer5.nextToken().type);

        Tokenizer tokenizer6 = new Tokenizer("'foo\\");
        assertEquals(TokenType.ERROR, tokenizer6.nextToken().type);
    }

    @Test
    public void testTokenizeHash() {
        Tokenizer tokenizer1 = new Tokenizer("#foo");
        Token token1 = tokenizer1.nextToken();
        assertEquals(TokenType.HASH, token1.type);
        assertEquals("foo", token1.value);

        Tokenizer tokenizer2 = new Tokenizer("#bl\\00e5-b\\00e4r");
        Token token2 = tokenizer2.nextToken();
        assertEquals(TokenType.HASH, token2.type);
        assertEquals("blå-bär", token2.value);

        Tokenizer tokenizer3 = new Tokenizer("#f()");
        Token token3 = tokenizer3.nextToken();
        assertEquals(TokenType.HASH, token3.type);
        assertEquals("f", token3.value);
    }

}
