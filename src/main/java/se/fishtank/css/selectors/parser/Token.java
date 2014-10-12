/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.parser;

/**
 * A token emitted by the {@linkplain se.fishtank.css.selectors.parser.Tokenizer tokenizer}.
 *
 * @author Christer Sandberg
 */
public final class Token {

    /** This tokens type. */
    public final TokenType type;

    /** The token value or {@code null}. */
    public final String value;

    /**
     * Create a new token.
     *
     * @param type The token type.
     * @param value The token value.
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Create a new token.
     *
     * @param type The token type.
     */
    public Token(TokenType type) {
        this(type, null);
    }

}
