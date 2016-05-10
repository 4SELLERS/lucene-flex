Lucene Flex-Based Analyzer
==========================

# Generate Flex files

The maven jFlex plugin will automatically build the flex file on project 
rebuild. Use the following command from the terminal:

```
    mvn clean compile
```


# Convert to C&#35;

The java source folder can be converted to C&#35; code by software such 
as Tangible Java to C&#35; converter. The generated code must, however,
be manually fixed. The following list summerizes list of manual changes:

```
    - replace token attributes with interfaces.
    - replace org.apache.lucene.analysis.Token to Lucene.Net.Analysis.Token.
    - replace org.apache.lucene.analysis.tokenattributes.TermAttribute to Lucene.Net.Analysis.Tokenattributes.ITermAttribute.
    - java.io.Reader usages must be replaced with TextReader.
    - TextReader output for C&#35; must not be checked for 0.
```