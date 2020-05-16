package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.operation.AddContiguousOperation;
import java.util.Objects;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
public class AddAfterOrUpdateOperation extends AddContiguousOperation {

  @Override
  public void executeOperation(Document document) {
    if (Objects.isNull(searchElementValue(document,getXpath() + "/" + getElement().getName()))) {
      addElementAfter(
          document,
          getXpath(),
          getElement(),
          getContiguousElementXpath()
      );
    } else {
      modifyElement(document, getXpath() + "/" + getElement().getName(), getElement().getValue());
    }
  }
}