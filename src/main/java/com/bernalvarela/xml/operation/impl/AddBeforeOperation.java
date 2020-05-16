package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.AddContiguousOperation;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
public class AddBeforeOperation extends AddContiguousOperation {

  @Override
  public void executeOperation(Document document) {
    addElementBefore(
        document,
        getXpath(),
        getElement(),
        getContiguousElementXpath());
  }
}
