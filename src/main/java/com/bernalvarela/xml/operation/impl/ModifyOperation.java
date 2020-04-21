package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.XmlOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
@Getter
public class ModifyOperation extends XmlOperation {

  private final String value;

  @Override
  public void executeOperation(Document document) {
    modifyElement(document, getXpath(), getValue());
  }
}
