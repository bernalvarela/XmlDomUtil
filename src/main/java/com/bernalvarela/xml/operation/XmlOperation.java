package com.bernalvarela.xml.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuperBuilder
@Getter
public abstract class XmlOperation {

  private final String xpath;

  private final String elementName;

  private final XPath xPathExpression = XPathFactory.newInstance().newXPath();

  public List<String> searchElementValues(String xpathStr, Document document) {
    List<String> retValue = new ArrayList<>();
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        retValue.add(nodes.item(i).getNodeValue());
      }
    } catch (XPathExpressionException e) {
    }
    return retValue;
  }

  public String searchElementValue(Document document, String xpathStr) {
    String retValue = null;
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      Node node = (Node) expr.evaluate(document, XPathConstants.NODE);
      if (Objects.nonNull(node)) {
        retValue = Objects.isNull(node.getNodeValue()) ? "" : node.getNodeValue();
      }
    } catch (XPathExpressionException e) {
    }
    return retValue;
  }

  protected void addElement(Document document, String xpathStr, String elementName, Object value) {
    if (searchNode(document,xpathStr + "/" + elementName) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.appendChild(generateElement(document, elementName, value.toString()));
      }
    }
  }

  protected Void addElementBefore(Document document, String xpathStr, String elementName, Object value, String beforeElementXpath) {
    if (searchNode(document, xpathStr + "/" + elementName) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.insertBefore(generateElement(document, elementName, value.toString()), searchNode(document, beforeElementXpath));
      }
    }
    return null;
  }

  protected Void addElementAfter(Document document, String xpathStr, String elementName, Object value, String afterElementXpath) {
    if (searchNode(document, xpathStr + "/" + elementName) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.insertBefore(
            generateElement(document, elementName, value.toString()),
            searchNode(document, afterElementXpath).getNextSibling()
        );
      }
    }
    return null;
  }

  private Node searchNode(Document document, String xpathStr) {
    Object res;
    Node node = null;
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      res = expr.evaluate(document, XPathConstants.NODESET);
      NodeList nlist = (NodeList) res;
      if (Objects.nonNull(nlist) && nlist.getLength() > 0) {
        node = ((NodeList) res).item(0);
      }
    } catch (XPathExpressionException e) {
    }
    return node;
  }

  private Element generateElement(Document document, String name, String value) {
    Element newElement = document.createElement(name);
    newElement.setTextContent(value);
    return newElement;
  }

  public void modifyElement(Document document, String xpathStr, Object newValue) {
    Object res;
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      res = expr.evaluate(document, XPathConstants.NODESET);
      NodeList nlist = (NodeList) res;
      for (int i = 0; i < nlist.getLength(); i++) {
        Node node = nlist.item(i);
        if (newValue != null) {
          node.setTextContent(newValue.toString());
        }
      }
    } catch (XPathExpressionException e) {
    }
  }

  public void removeElement(Document document, String xpathStr) {
    Object res;
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      res = expr.evaluate(document, XPathConstants.NODESET);
      NodeList nlist = (NodeList) res;
      for (int i = 0; i < nlist.getLength(); i++) {
        Node node = nlist.item(i);
        Node parent = node.getParentNode();
        if (parent != null) {
          parent.removeChild(node);
        }
      }
    } catch (XPathExpressionException e) {
    }
  }

  public abstract void executeOperation(Document document);
}
