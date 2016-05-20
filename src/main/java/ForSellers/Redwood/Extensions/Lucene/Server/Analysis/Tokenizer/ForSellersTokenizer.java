package ForSellers.Redwood.Extensions.Lucene.Server.Analysis.Tokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;

/// <summary>
/// A grammar-based tokenizer constructed with JFlex specifically designed for shop search queries.
/// </summary>
public class ForSellersTokenizer extends Tokenizer {
    private final IForSellersTokenizerLogic scanner;

    private boolean replaceInvalidAcronym;
    private int maxTokenLength = 255;
    private TermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private PositionIncrementAttribute posIncrAtt;
    private TypeAttribute typeAtt;

    private IForSellersTokenizerLogic getScanner(Reader input) {
        return new ForSellersTokenizerLogic(input);
    }

    public void setMaxTokenLength(int length) {
        this.maxTokenLength = length;
    }

    public int getMaxTokenLength() {
        return this.maxTokenLength;
    }

    public ForSellersTokenizer(Version matchVersion, Reader input) {
        this.scanner = getScanner(input);
        this.init(input, matchVersion);
    }

    public ForSellersTokenizer(Version matchVersion, AttributeSource source, Reader input) {
        super(source);
        this.scanner = getScanner(input);
        this.init(input, matchVersion);
    }

    public ForSellersTokenizer(Version matchVersion, AttributeFactory factory, Reader input) {
        super(factory);
        this.scanner = getScanner(input);
        this.init(input, matchVersion);
    }

    private void init(Reader input, Version matchVersion) {
        if(matchVersion.onOrAfter(Version.LUCENE_24)) {
            this.replaceInvalidAcronym = true;
        } else {
            this.replaceInvalidAcronym = false;
        }

        this.input = input;
        this.termAtt = this.addAttribute(TermAttribute.class);
        this.offsetAtt = this.addAttribute(OffsetAttribute.class);
        this.posIncrAtt = this.addAttribute(PositionIncrementAttribute.class);
        this.typeAtt = this.addAttribute(TypeAttribute.class);
    }

    public final boolean incrementToken() throws IOException {
        this.clearAttributes();
        int posIncr = 1;

        while(true) {
            TokenTypes tokenType = this.scanner.GetNextToken();
            if(tokenType == null) {
                return false;
            }

            if(this.scanner.GetMatchedLength() <= this.maxTokenLength) {
                this.posIncrAtt.setPositionIncrement(posIncr);
                this.scanner.GetMatchedText(this.termAtt);
                int start = this.scanner.GetMatchedStart();
                this.offsetAtt.setOffset(this.correctOffset(start), this.correctOffset(start + this.termAtt.termLength()));
                if(tokenType == TokenTypes.ACRONYM_DEP) {
                    if(this.replaceInvalidAcronym) {
                        this.typeAtt.setType(TokenTypes.HOST.getTypeName());
                        this.termAtt.setTermLength(this.termAtt.termLength() - 1);
                    } else {
                        this.typeAtt.setType(TokenTypes.ACRONYM.getTypeName());
                    }
                } else {
                    this.typeAtt.setType(tokenType.getTypeName());
                }

                return true;
            }

            ++posIncr;
        }
    }

    public final void end() {
        int finalOffset = this.correctOffset(this.scanner.GetMatchedStart() + this.scanner.GetMatchedLength());
        this.offsetAtt.setOffset(finalOffset, finalOffset);
    }

    public void reset(Reader reader) throws IOException {
        super.reset(reader);
        this.scanner.Reset(reader);
    }

    /** @deprecated */
    public boolean isReplaceInvalidAcronym() {
        return this.replaceInvalidAcronym;
    }

    /** @deprecated */
    public void setReplaceInvalidAcronym(boolean replaceInvalidAcronym) {
        this.replaceInvalidAcronym = replaceInvalidAcronym;
    }
}
