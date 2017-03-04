package PluginReference.configuration.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * XML configuration setup for Rainbow.
 *
 * <br><a href="https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm">Based on Java XML DOM example</a>
 * 
 * @author Isaiah Patton
 */
public class XmlConfiguration {
    public File file;
    public DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    public XmlConfiguration(File f) {
        this.file = f;
    }

    /**
     * Sets field to new text
     */
    public void set(String nodeName, String newText) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        doc.getAttributes().getNamedItem(nodeName).setNodeValue(newText);
 
    }
    
    /**
     * Returns the field text.
     * 
     * @return String
     */
    public String get(String nodeName) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        Node n = doc.getAttributes().getNamedItem(nodeName);

        return n.getNodeValue();
    }
}
