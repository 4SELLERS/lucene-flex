package ForSellers.Redwood.Extensions.Lucene.Server.Analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by mohammadreza.roohian on 03.05.2016.
 */
public class ForSellersAnalyzerTests {
    @Test
    public void testTokenize() throws IOException {
        Reader reader = new StringReader(
            "MODUL-PLUS Bewegungsmelder, silber, 250V~/5A, UP " +
            "Me&Myself"
        );

        ForSellersAnalyzer analyzer = new ForSellersAnalyzer(Version.LUCENE_30, "German2", null);

        String fieldName = "_TITLE";

        TokenStream a = analyzer.tokenStream(fieldName, reader);

        // + Assert
        while (a.incrementToken()) {
            System.out.println(a.getAttribute(TermAttribute.class));
            System.out.println(a.getAttribute(TypeAttribute.class));
        }
    }
}
