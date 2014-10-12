/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.parser;

/**
 * // TODO: Document me
 *
 * @author Christer Sandberg
 */
public enum TokenType {

    EOF, ERROR, WHITESPACE, STRING, HASH, CLASS, PSEUDO_ELEMENT, NOT,

    OPEN_PAREN, CLOSE_PAREN, IDENT

}
