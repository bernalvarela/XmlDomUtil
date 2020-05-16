package com.bernalvarela.xml.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class XmlAttribute {

  private final String name;

  private final String value;

}
