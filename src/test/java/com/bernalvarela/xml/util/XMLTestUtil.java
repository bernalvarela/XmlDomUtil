package com.bernalvarela.xml.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class XMLTestUtil {

  public static String getFile(String file) {
    ClassLoader classLoader = XMLTestUtil.class.getClassLoader();
    try {
      return IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream(file)), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
