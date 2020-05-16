package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.ElementOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
@Getter
public class AddOperation extends ElementOperation {

  public void executeOperation(Document document) {
    addElement(document, getXpath(), getElement());
  }
}
