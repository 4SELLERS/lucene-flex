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
        String text = "MODUL-PLUS Bewegungsmelder, silber, 250V~/5A, UP " +
            "Me&Myself " +
            "٩٠ 90 و and 中國 اما but 923 a " +
            " 化計劃 " +
            "Aäüxa, 䋨みょ ৯০ Äa_sds $ ABSDS διδασ-ϰάλια جُمله ἐς ९० τοὺς here Ελληνα ႠႡႢႣ ͲͳͰͱ ӐӑӒӓӔӕ " +
            "Ԣԣ Ātsā 4Sellers 8 and Das Kochbuch für Reiter " +
            " Know-it-all ";

        String fieldName = "_TITLE";

        ForSellersAnalyzer analyzer = new ForSellersAnalyzer(Version.LUCENE_30, "German2", null);
        ForSellersAnalyzer1 analyzer1 = new ForSellersAnalyzer1(Version.LUCENE_30, "German2", null);

        TokenStream a = analyzer.tokenStream(fieldName, new StringReader(text));
        TokenStream b = analyzer1.tokenStream(fieldName, new StringReader(text));

        // + Assert
        while (a.incrementToken()) {
            System.out.println("A:" + a.getAttribute(TermAttribute.class) + ", " + a.getAttribute(TypeAttribute.class));

            if (b != null && b.incrementToken()) {
                System.out.println("B:" + b.getAttribute(TermAttribute.class)+ ", " + b.getAttribute(TypeAttribute.class));
            } else {
                b = null;
            }
        }
    }
}
