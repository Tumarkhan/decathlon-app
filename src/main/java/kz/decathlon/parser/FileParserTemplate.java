package kz.decathlon.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FileParserTemplate {
    public <T> List<List<T>> getParsedListAfterReadingFile(String path, Class<T> klass) throws URISyntaxException, IOException {
        File file = getFile(path);
        List<List<T>> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isBlank()) {
                list.add(Arrays.asList(doParse(line)));
            }
        }

        return list;
    }

    protected abstract <T> T[] doParse(String line);

    private File getFile(String path) throws URISyntaxException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("File - " + path + " not found");
        }

        return new File(resource.toURI());
    }
}
