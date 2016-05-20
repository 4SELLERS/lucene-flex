package ForSellers.Redwood.Extensions.Lucene.Server.Analysis;

import ForSellers.Redwood.Extensions.Lucene.Server.Analysis.Tokenizer.ForSellersTokenizer1;
import org.apache.lucene.analysis.*;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by mohammadreza.roohian on 03.05.2016.
 */
public class ForSellersAnalyzer1 extends Analyzer {
    private final Version _matchVersion;
    private String _name;
    private final Set<String> _stopSet;
    private int _maxTokenLength;
    private boolean _enableStopPositionIncrements;

    /**
     * Constructor
     *
     * @param matchVersion
     * @param name
     * @param stopSet
     */
    public ForSellersAnalyzer1(Version matchVersion, String name, Set<String> stopSet) {
        this._matchVersion = matchVersion;
        this._name = name;
        this._stopSet = stopSet;

        this._maxTokenLength = 255;
        this._enableStopPositionIncrements = StopFilter.getEnablePositionIncrementsVersionDefault(matchVersion);
    }

    /// <summary>
    /// Creates a TokenStream which tokenizes all the text in the provided
    ///             Reader.  Must be able to handle null field _name for
    ///             backward compatibility.
    /// </summary>
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        ForSellersTokenizer1 forSellersTokenizer = new ForSellersTokenizer1(_matchVersion, reader);
        forSellersTokenizer.setMaxTokenLength(_maxTokenLength);

        /*TokenStream tokenStream = new LowerCaseFilter(new StandardFilter(forSellersTokenizer));

        if (_stopSet != null) {
            tokenStream = new StopFilter(_enableStopPositionIncrements, tokenStream, _stopSet);
        }

        return new SnowballFilter(tokenStream, _name);*/
        return forSellersTokenizer;
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        if(this.overridesTokenStreamMethod) {
            return this.tokenStream(fieldName, reader);
        } else {
            SavedStreams streams = (SavedStreams)this.getPreviousTokenStream();
            if(streams == null) {
                streams = new SavedStreams();
                streams.source = new ForSellersTokenizer1(_matchVersion, reader);
                /*streams.result = new StandardFilter(streams.source);
                streams.result = new LowerCaseFilter(streams.result);
                if(_stopSet != null) {
                    streams.result = new StopFilter(_enableStopPositionIncrements, streams.result, _stopSet);
                }

                streams.result = new SnowballFilter(streams.result, _name);
                */
                this.setPreviousTokenStream(streams);
            } else {
                streams.source.reset(reader);
            }

            return streams.result;
        }
    }

    private class SavedStreams {
        Tokenizer source;
        TokenStream result;
    }
}
