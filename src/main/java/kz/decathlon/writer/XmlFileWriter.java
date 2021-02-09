package kz.decathlon.writer;

import kz.decathlon.dto.DecathlonScoreDetails;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.util.List;

public class XmlFileWriter implements FileWriter {
    private final String nameSpace = "https://www.decathlon.com/app";

    @Override
    public <T> void write(List<T> elements, String path, Class<T> klass) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = dbf.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElementNS(nameSpace, DecathlonScoreDetails.fullName);
            document.appendChild(rootElement);

            for (T detail : elements) {
                rootElement.appendChild(getDecathlonScoreDetails(document, (DecathlonScoreDetails) detail));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);

            String directory = System.getProperty("user.home");
            String absolutePath = directory + File.separator + path;

            StreamResult streamResult = new StreamResult();
            streamResult.setWriter(new BufferedWriter(new java.io.FileWriter(absolutePath)));
            transformer.transform(source, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node getDecathlonScoreDetails(Document document, DecathlonScoreDetails details) {
        Element scoreDetails = document.createElement(DecathlonScoreDetails.fullName);
        scoreDetails.appendChild(getDecathlonScoreDetailsElements(document, "name", details.getName()));
        scoreDetails.appendChild(getDecathlonScoreDetailsElements(document, "place", String.valueOf(details.getPlace())));
        scoreDetails.appendChild(getDecathlonScoreDetailsElements(document, "totalPoints", String.valueOf(details.getTotalPoints())));
        scoreDetails.appendChild(getDecathlonScoreDetailsElements(document, "scores", details.getScores()));

        return scoreDetails;
    }

    private Node getDecathlonScoreDetailsElements(Document document, String name, String value) {
        Element node = document.createElement(name);
        node.appendChild(document.createTextNode(value));
        return node;
    }
}
