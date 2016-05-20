// ------------------------------------------------------------------------
//  This software is part of 4SELLERS
//  Copyright © 2012 - 2016 logic-base GmbH
//  http://www.4sellers.de
//
//  All information contained herein is, and remains
//  the property of logic-base GmbH and its suppliers,
//  if any.  The intellectual and technical concepts contained
//  herein are proprietary to logic-base GmbH
//  and its suppliers and may be covered by German and Foreign Patents,
//  patents in process, and are protected by trade secret or copyright law.
//  Dissemination of this information or reproduction of this material
//  is strictly forbidden unless prior written permission is obtained
//  from logic-base GmbH.
//
//  4sellers.Redwood.Extensions.Lucene.Server.Analysis - ForSellersTokenizer1Logic.cs
//  2016/05/11
//
// ------------------------------------------------------------------------

package ForSellers.Redwood.Extensions.Lucene.Server.Analysis.Tokenizer;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
%%

%class ForSellersTokenizer1Logic
%implements IForSellersTokenizerLogic
%apiprivate

%function getNextTokenImpl
%type TokenTypes

%unicode
%pack
%char

%{
/**
 * Returns the number of characters up to the start of the matched text.
 */
@Override public final int GetMatchedStart() {
    return yychar;
}

/**
 * Returns the length of the matched text region.
 */
@Override public final int GetMatchedLength() {
    return zzMarkedPos - zzStartRead;
}

/**
 * Fills Lucene token with the current token text.
 */
@Override public final void GetMatchedText(Token t) {
  t.setTermBuffer(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
}

/**
 * Fills Lucene TermAttribute with the current token text.
 */
@Override public final void GetMatchedText(TermAttribute t) {
    t.setTermBuffer(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
}

/**
 * Create a new buffer and reset the parser
 */
@Override public final void Reset(java.io.Reader r) {
    if(this.zzBuffer.length > 16384) {
        this.zzBuffer = new char[16384];
    }

    this.yyreset(r);
}

/**
 * Resumes scanning until the next regular expression is matched,
 * the end of input is encountered or an I/O-Error occurs and
 * returns null.
 *
 * @return      the next token
 */
@Override public final TokenTypes GetNextToken() {
    try{
        return this.getNextTokenImpl();
    } catch (java.io.IOException ignored) {
        return null;
    }
}

%}

// + RULES

// ++ Basic definitions

// New line, Whitespaces and any kind of whitespace or invisible separator
WHITESPACE      = \r\n | \p{Z}

// Any kind of numeric character in any script: 0-9 ٠-٩ ۰-۹ ०-९ ০-৯ ...
DIGIT           = \p{N}

// Any kind of an uppercase letter that has a lowercase variant: A-Z À-Ö Ø-Þ ø-ÿ  ﾡ-ￜ Ā-῾ ...
CAPITAL_LETTER  = \p{Lu}

// Any kind of a lowercase letter that has an uppercase variant: a-z ...
LOWER_LETTER    = \p{Ll}

// Caseless letters
OTHER_LETTERS   = \p{Lo} | \p{Lm}

// Any kind of letter from any language
LETTER          = {CAPITAL_LETTER} | {LOWER_LETTER} | {OTHER_LETTERS}

// Any kind of letter from any language or any kind of numeric character in any script
LETTER_DIGIT    = {LETTER} | {DIGIT}

// Any kind of hyphen or dash.
HYPHEN          = \p{Pd}

// ++ Words definitions

NUMBER_WORD     = {DIGIT}+

ALLCAPS_WORD    = {CAPITAL_LETTER}+

TITLECASE_WORD  = {CAPITAL_LETTER}{LOWER_LETTER}+

// Any word containing at least one letter and one digit
HAS_DIGIT_WORD  = {LETTER_DIGIT}*(({LETTER}{DIGIT})|({DIGIT}{LETTER})){LETTER_DIGIT}*

// ++ Combined rules

// At least one digit


// Any letter or digit
ALPHANUM        = {LETTER_DIGIT}+

// Hyphened alpha numberic
HYPHEN_ALPHANUM = {ALPHANUM}({HYPHEN}{ALPHANUM})+

%%

{HAS_DIGIT_WORD}    { return TokenTypes.COMPANY; }
{NUMBER_WORD}       { return TokenTypes.NUM; }

{HYPHEN_ALPHANUM}   { return TokenTypes.HYPHENED_ALPHANUM; }
{ALPHANUM}          { return TokenTypes.ALPHANUM; }

/** Ignore the rest */
. | {WHITESPACE}    { /* ignore */ }