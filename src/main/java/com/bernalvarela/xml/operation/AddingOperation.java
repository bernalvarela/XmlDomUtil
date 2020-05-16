package com.bernalvarela.xml.operation;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class AddingOperation extends XmlOperation {

  private final String elementName;

  private final String value;

}
