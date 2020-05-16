package com.bernalvarela.xml.operation;

import com.bernalvarela.xml.entity.XmlAttribute;
import com.bernalvarela.xml.entity.XmlElement;
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

  private final XPath xPathExpression = XPathFactory.newInstance().newXPath();

  public List<String> searchElementValues(String xpathStr, Document document) {
    List<String> retValue = new ArrayList<>();
    try {
      XPathExpression expr = xPathExpression.compile(xpathStr);
      NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        retValue.add(nodes.item(i).getNodeValue());
      }
    } catch (XPathExpressionException ignored) {
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
    } catch (XPathExpressionException ignored) {
    }
    return retValue;
  }

  protected void addElement(Document document, String xpathStr, XmlElement element) {
    if (searchNode(document,xpathStr + "/" + element.getName()) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.appendChild(generateElement(document, element));
      }
    }
  }

  protected void addElementBefore(Document document, String xpathStr, XmlElement element, String beforeElementXpath) {
    if (searchNode(document, xpathStr + "/" + element.getName()) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.insertBefore(generateElement(document, element), searchNode(document, beforeElementXpath));
      }
    }
  }

  protected void addElementAfter(Document document, String xpathStr, XmlElement element, String afterElementXpath) {
    if (searchNode(document, xpathStr + "/" + element.getName()) == null) {
      Node node = searchNode(document, xpathStr);
      if (Objects.nonNull(node)) {
        node.insertBefore(
            generateElement(document, element),
            searchNode(document, afterElementXpath).getNextSibling()
        );
      }
    }
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
    } catch (XPathExpressionException ignored) {
    }
    return node;
  }

  private Element generateElement(Document document, XmlElement element) {
    Element newElement = document.createElement(element.getName());
    this.setValueToElement(document, newElement, element.getValue());
    this.setAttributesToElement(newElement, element.getAttributes());
    return newElement;
  }

  private void setValueToElement(Document document, Node newElement, Object value) {
    if (value instanceof XmlElement) {
      newElement.appendChild(this.generateElement(document, (XmlElement) value));
    } else if (value instanceof List){
      ((List) value).forEach(valueTmp -> this.setValueToElement(document, newElement, valueTmp));
    } else {
      newElement.setTextContent(value.toString());
    }
  }

  private void setAttributesToElement(Element element, List<XmlAttribute> attributes) {
    if (Objects.nonNull(attributes)) {
      attributes.forEach(xmlAttribute -> {
        if (element.hasAttribute(xmlAttribute.getName())) {
          element.removeAttribute(xmlAttribute.getName());
        }
        element.setAttribute(xmlAttribute.getName(), xmlAttribute.getValue());
      });
    }
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
    } catch (XPathExpressionException ignored) {
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
    } catch (XPathExpressionException ignored) {
    }
  }

  public abstract void executeOperation(Document document);
}
