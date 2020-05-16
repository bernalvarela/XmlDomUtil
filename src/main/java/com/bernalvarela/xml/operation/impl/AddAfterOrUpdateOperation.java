package com.bernalvarela.xml.operation.impl;

import com.bernalvarela.xml.entity.XmlElement;
import com.bernalvarela.xml.operation.AddContiguousOperation;
import com.bernalvarela.xml.operation.AddingOperation;
import java.util.Objects;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Document;

@SuperBuilder
public class AddAfterOrUpdateOperation extends AddContiguousOperation {

  @Override
  public void executeOperation(Document document) {
    if (Objects.isNull(searchElementValue(document,getXpath() + "/" + getElementName()))) {
      addElementAfter(
          document,
          getXpath(),
          XmlElement.builder().name(getElementName()).value(getValue()).build(),
          getContiguousElementXpath()
      );
    } else {
      modifyElement(document, getXpath() + "/" + getElementName(), ((AddingOperation) this).getValue());
    }
  }
}