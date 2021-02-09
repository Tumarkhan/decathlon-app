package kz.decathlon.parser;

public class CSVParser<T> extends FileParserTemplate {
    @Override
    public T[] doParse(String line) {
        return (T[]) line.split(";");
    }
}
