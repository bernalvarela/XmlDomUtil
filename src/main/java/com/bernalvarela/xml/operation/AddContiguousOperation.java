package com.bernalvarela.xml.operation;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class AddContiguousOperation extends AddingOperation {

  private final String contiguousElementXpath;
}
