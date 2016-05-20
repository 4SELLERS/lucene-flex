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
//  4sellers.Redwood.Extensions.Lucene.Server.Analysis - ForSellersTokenizerLogic.cs
//  2016/05/10
//
// ------------------------------------------------------------------------

package ForSellers.Redwood.Extensions.Lucene.Server.Analysis.Tokenizer;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
%%

%class ForSellersTokenizerLogic
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

// Reza
HYPHEN_ALPHANUM = ({ALPHANUM}-{ALPHANUM})

// basic word: a sequence of digits & letters
ALPHANUM   = ({LETTER}|{DIGIT}|{KOREAN})+

// internal apostrophes: O'Reilly, you're, O'Reilly's
// use a post-filter to remove possesives
APOSTROPHE =  {ALPHA} ("'" {ALPHA})+

// acronyms: U.S.A., I.B.M., etc.
// use a post-filter to remove dots
ACRONYM    =  {LETTER} "." ({LETTER} ".")+

ACRONYM_DEP	= {ALPHANUM} "." ({ALPHANUM} ".")+

// company names like AT&T and Excite@Home.
COMPANY    =  {ALPHA} ("&"|"@") {ALPHA}

// email addresses
EMAIL      =  {ALPHANUM} (("."|"-"|"_") {ALPHANUM})* "@" {ALPHANUM} (("."|"-") {ALPHANUM})+

// hostname
HOST       =  {ALPHANUM} ((".") {ALPHANUM})+

// floating point, serial, model numbers, ip addresses, etc.
// every other segment must have at least one digit
NUM        = ({ALPHANUM} {P} {HAS_DIGIT} | {HAS_DIGIT} {P} {ALPHANUM} | {ALPHANUM} ({P} {HAS_DIGIT} {P} {ALPHANUM})+ | {HAS_DIGIT} ({P} {ALPHANUM} {P} {HAS_DIGIT})+ | {ALPHANUM} {P} {HAS_DIGIT} ({P} {ALPHANUM} {P} {HAS_DIGIT})+ | {HAS_DIGIT} {P} {ALPHANUM} ({P} {HAS_DIGIT} {P} {ALPHANUM})+)

// punctuation
P	         = ("_"|"-"|"/"|"."|",")

// at least one digit
HAS_DIGIT  = ({LETTER}|{DIGIT})*{DIGIT}({LETTER}|{DIGIT})*

ALPHA      = ({LETTER})+

// A-Z a-z À-Ö Ø-ö ø-ÿ Ā-῾ ﾡ-ￜ
LETTER     = [\u0041-\u005a\u0061-\u007a\u00c0-\u00d6\u00d8-\u00f6\u00f8-\u00ff\u0100-\u1fff\uffa0-\uffdc]

DIGIT      = [\u0030-\u0039\u0660-\u0669\u06f0-\u06f9\u0966-\u096f\u09e6-\u09ef\u0a66-\u0a6f\u0ae6-\u0aef\u0b66-\u0b6f\u0be7-\u0bef\u0c66-\u0c6f\u0ce6-\u0cef\u0d66-\u0d6f\u0e50-\u0e59\u0ed0-\u0ed9\u1040-\u1049]

KOREAN     = [\uac00-\ud7af\u1100-\u11ff]

// Chinese, Japanese
CJ         = [\u3040-\u318f\u3100-\u312f\u3040-\u309F\u30A0-\u30FF\u31F0-\u31FF\u3300-\u337f\u3400-\u4dbf\u4e00-\u9fff\uf900-\ufaff\uff65-\uff9f]

WHITESPACE = \r\n | [ \r\n\t\f]

%%

{HYPHEN_ALPHANUM}                                              { return TokenTypes.ALPHANUM; }
{ALPHANUM}                                                     { return TokenTypes.ALPHANUM; }
{APOSTROPHE}                                                   { return TokenTypes.APOSTROPHE; }
{ACRONYM}                                                      { return TokenTypes.ACRONYM; }
{COMPANY}                                                      { return TokenTypes.COMPANY; }
{EMAIL}                                                        { return TokenTypes.EMAIL; }
{HOST}                                                         { return TokenTypes.HOST; }
{NUM}                                                          { return TokenTypes.NUM; }
{CJ}                                                           { return TokenTypes.CJ; }
{ACRONYM_DEP}                                                  { return TokenTypes.ACRONYM_DEP; }

/** Ignore the rest */
. | {WHITESPACE}                                               { /* ignore */ }
