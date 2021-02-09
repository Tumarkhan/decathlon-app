package kz.decathlon;

import kz.decathlon.parser.CSVParser;
import kz.decathlon.service.DecathlonCalculatorService;
import kz.decathlon.writer.XmlFileWriter;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        DecathlonCalculatorService service = new DecathlonCalculatorService(new CSVParser<String>(), new XmlFileWriter());
        String readFromFilePath = "results.csv";// read from the resources directory in a classpath
        String exportToFilePath = "res.xml";// will be exported in your user/home directory

        service.calculateFromFileAndExport(readFromFilePath, exportToFilePath);
    }
}
