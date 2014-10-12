/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.parser;

/**
 * A tokenizer for CSS <a href="http://www.w3.org/TR/css3-selectors/#w3cselgrammar">selectors</a>.
 *
 * @author Christer Sandberg
 */
public class Tokenizer {

    private static final int EOF = -1;

    public final String input;

    private final int len;

    private int pos;

    public Tokenizer(String input) {
        this.input = input.replaceAll("\\f|\\r\\n?", "\n");
        this.len = input.codePointCount(0, input.length());
        this.pos = 0;
    }

    public Token nextToken() {
        if (pos >= len) {
            return new Token(TokenType.EOF);
        }

        int c = next();
        if (isSpace(c)) {
            while (isSpace(peek())) {
                next();
            }

            return new Token(TokenType.WHITESPACE);
        }

        switch (c) {
        case '"':
            return consumeString(false);
        case '\'':
            return consumeString(true);
        case '#':
            return consumeIdentifier(TokenType.HASH);
        case '.':
            return consumeIdentifier(TokenType.CLASS);
        case '(':
            return new Token(TokenType.OPEN_PAREN);
        case ')':
            return new Token(TokenType.CLOSE_PAREN);
        case ':':
            int d = peek();
            if (d == ':') {
                next();
                return consumeIdentifier(TokenType.PSEUDO_ELEMENT);
            } else if (input.startsWith("not", pos)) {
                pos += 3;
                return new Token(TokenType.NOT);
            } else {
                return consumeIdentifier(TokenType.IDENT);
            }
        default:
            return new Token(TokenType.ERROR);
        }
    }

    private Token consumeString(boolean singleQuote) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int c = next();
            if (c == EOF || (c == '"' && !singleQuote) || (c == '\'' && singleQuote)) {
                return new Token(TokenType.STRING, sb.toString());
            }

            switch (c) {
            case '\n':
                return new Token(TokenType.ERROR);
            case '\\':
                int d = peek();
                if (d == '\n') {
                    next();
                } else if (isEscape(d)) {
                    sb.appendCodePoint(consumeEscape());
                } else {
                    return new Token(TokenType.ERROR);
                }

                break;
            default:
                sb.appendCodePoint(c);
            }
        }
    }

    private Token consumeIdentifier(TokenType tokenType) {
        int p = pos;
        int c = next();
        if (c == '-') {
            c = next();
        }

        if (!(isAlpha(c) || c == '_' || c >= 0x80 || (c == '\\' && isEscape(peek())))) {
            return new Token(TokenType.ERROR);
        }

        pos = p;
        StringBuilder sb = new StringBuilder();
        while (true) {
            c = next();
            if (!(isLetter(c) || (c == '\\' && isEscape(peek())))) {
                break;
            }

            if (c != '\\') {
                sb.appendCodePoint(c);
            } else {
                sb.appendCodePoint(consumeEscape());
            }
        }

        return new Token(tokenType, sb.toString());
    }

    private int consumeEscape() {
        if (!isHex(peek())) {
            return next();
        }

        int u = 0;
        for (int i = 0; i < 6; ++i) {
            if (!isHex(peek())) {
                break;
            }

            int c = next();
            u = (u << 4) + (c < 'A' ? c - '0' : (c - 'A' + 10) & 0xF);
        }

        u = u > Character.MAX_CODE_POINT ? 0xFFFD : u;
        if (isSpace(peek())) {
            next();
        }

        return u;
    }

    private int next() {
        return pos >= len ? EOF : input.codePointAt(pos++);
    }

    private int peek() {
        return pos >= len ? EOF : input.codePointAt(pos);
    }

    private static boolean isAlpha(int c) {
        return (c | 0x20) >= 'a' && (c | 0x20) <= 'z';
    }

    private static boolean isEscape(int c) {
        return c >= ' ' && c != 0x7F;
    }

    private static boolean isHex(int c) {
        return isNumber(c) || ((c | 0x20) >= 'a' && (c | 0x20) <= 'f');
    }

    private static boolean isLetter(int c) {
        return c >= 0x80 || isAlpha(c) || isNumber(c) || c == '_' || c == '-';
    }

    private static boolean isNumber(int c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isSpace(int c) {
        return c == '\t' || c == '\n' || c == ' ';
    }

}
