package com.bernalvarela.xml.util;

import com.bernalvarela.xml.operation.XmlOperation;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomXmlUtil {

  private final Document document;

  public DomXmlUtil(String documentStr) {
    document = this.initializeDocument(documentStr);
  }

  public String getDocument() {
    String retValue = null;
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);

      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      transformer.transform(source, result);

      retValue = result.getWriter().toString();
    } catch (TransformerException e) {
    }
    return retValue;
  }

  public void executeOperation(XmlOperation operation) {
    operation.executeOperation(document);
  }

  public void executeOperations(List<XmlOperation> operations) {
    operations.forEach(xmlOperation -> xmlOperation.executeOperation(document));
  }

  private Document initializeDocument(String xml) {
    Document doc = null;

    // https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.md
    // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
    // XML entity attacks are prevented
    String feature = "http://apache.org/xml/features/disallow-doctype-decl";
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setFeature(feature, true);

      // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
      factory.setXIncludeAware(false);
      factory.setExpandEntityReferences(false);

      try {
        DocumentBuilder dbuilder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(xml);
        InputSource in = new InputSource(reader);
        doc = dbuilder.parse(in);
      } catch (ParserConfigurationException | SAXException | IOException e) {
      }
    } catch (ParserConfigurationException e) {
    }
    return doc;
  }

}
