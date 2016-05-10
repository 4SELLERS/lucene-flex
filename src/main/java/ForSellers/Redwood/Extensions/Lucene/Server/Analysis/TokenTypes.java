package ForSellers.Redwood.Extensions.Lucene.Server.Analysis;

/**
 * ForSellers tokenizer token types.
 */
public enum TokenTypes {
    ALPHANUM(0, "<ALPHANUM>"),
    APOSTROPHE(1, "<APOSTROPHE>"),
    ACRONYM(2, "<ACRONYM>"),
    COMPANY(3, "<COMPANY>"),
    EMAIL(4, "<EMAIL>"),
    HOST(5, "<HOST>"),
    NUM(6, "<NUM>"),
    CJ(7, "<CJ>"),

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

