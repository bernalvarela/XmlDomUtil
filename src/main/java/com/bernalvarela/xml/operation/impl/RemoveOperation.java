package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.XmlOperation;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
public class RemoveOperation extends XmlOperation {

  @Override
  public void executeOperation(Document document) {
    removeElement(document, getXpath());
  }
}
