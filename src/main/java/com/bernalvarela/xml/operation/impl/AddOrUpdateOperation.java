package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.ElementOperation;
import java.util.Objects;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
@Getter
public class AddOrUpdateOperation extends ElementOperation {

  public void executeOperation(Document document) {
    if (Objects.isNull(searchElementValue(document,getXpath() + "/" + getElement().getName()))) {
      addElement(document, getXpath(), getElement());
    } else {
      modifyElement(document, getXpath() + "/" + getElement().getName(), getElement().getValue());
    }
  }
}
