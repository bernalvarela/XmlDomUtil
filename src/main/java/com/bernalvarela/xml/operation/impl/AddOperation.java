package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.entity.XmlElement;
import com.bernalvarela.xml.operation.AddingOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
@Getter
public class AddOperation extends AddingOperation {

  public void executeOperation(Document document) {
    addElement(document, getXpath(), XmlElement.builder().name(getElementName()).value(getValue()).build());
  }
}
