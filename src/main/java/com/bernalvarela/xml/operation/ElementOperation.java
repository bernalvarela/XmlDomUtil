package com.bernalvarela.xml.operation;

import com.bernalvarela.xml.entity.XmlElement;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class ElementOperation extends XmlOperation {

  private final XmlElement element;

}
