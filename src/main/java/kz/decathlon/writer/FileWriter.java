package kz.decathlon.writer;

import java.util.List;

public interface FileWriter {
    <T> void write(List<T> elements, String path, Class<T> klass);
}
