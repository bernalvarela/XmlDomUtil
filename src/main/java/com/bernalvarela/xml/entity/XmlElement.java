package com.bernalvarela.xml.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class XmlElement {

  private final String name;

  private final Object value;

  private final List<XmlAttribute> attributes;
}
