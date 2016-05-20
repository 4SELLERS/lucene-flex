package ForSellers.Redwood.Extensions.Lucene.Server.Analysis.Tokenizer;

/**
 * ForSellers tokenizer token types.
 */
public enum TokenTypes {
    ALPHANUM(0, "<ALPHANUM>"),
    HYPHENED_ALPHANUM(1, "<HYPHENED_ALPHANUM>"),
    APOSTROPHE(2, "<APOSTROPHE>"),
    ACRONYM(3, "<ACRONYM>"),
    COMPANY(4, "<COMPANY>"),
    EMAIL(5, "<EMAIL>"),
    HOST(6, "<HOST>"),
    NUM(7, "<NUM>"),
    CJ(8, "<CJ>"),

    /**
     * @deprecated this solves a bug where HOSTs that end with '.' are identified
     *             as ACRONYMs. It is deprecated and will be removed in the next
     *             release.
     */
    ACRONYM_DEP(8, "<ACRONYM_DEP>");

    private final int id;
    private final String typeName;

    TokenTypes(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }
}

