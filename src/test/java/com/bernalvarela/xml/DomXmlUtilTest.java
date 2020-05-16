package com.bernalvarela.xml;

import static com.bernalvarela.xml.util.XMLTestUtil.getFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bernalvarela.xml.entity.XmlAttribute;
import com.bernalvarela.xml.entity.XmlElement;
import com.bernalvarela.xml.operation.XmlOperation;
import com.bernalvarela.xml.operation.impl.AddAfterOperation;
import com.bernalvarela.xml.operation.impl.AddAfterOrUpdateOperation;
import com.bernalvarela.xml.operation.impl.AddBeforeOperation;
import com.bernalvarela.xml.operation.impl.AddBeforeOrUpdateOperation;
import com.bernalvarela.xml.operation.impl.AddOperation;
import com.bernalvarela.xml.operation.impl.ModifyOperation;
import com.bernalvarela.xml.operation.impl.RemoveOperation;
import com.bernalvarela.xml.util.DomXmlUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DomXmlUtilTest {

  private static final String EXTERNAL_REFERENCE_ID_XPATH = "/NEW_ORDER/ORDER_ID";

  private static final String ORDER_ID_VALUE = "ORDER_ID_VALUE";

  private static final String WCS_ORDER_ID_XPATH = "/NEW_ORDER";

  private static final String WCS_ORDER_IDS_ELEMENT_NAME = "WCS_ORDER_IDS";

  private static final String WCS_ORDER_ID_ELEMENT_NAME = "WCS_ORDER_ID";

  private static final String EXTERNAL_REFERENCE_ID_VALUE = "EXTERNAL_REFERENCE_ID_VALUE";

  private static final String DELIVERY_CENTRE_GROUP_ID_XPATH = "/NEW_ORDER/DELIVERY_CENTRE_GROUP_ID";

  private static final Integer FULFILLMENT_CENTER_ID_VALUE = 1;

  @Test
  public void testNewOrderOperations() {
    String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID><WCS_ORDER_ID></WCS_ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputXML);
    String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER><ORDER_ID>ORDER_ID_VALUE</ORDER_ID>"
        + "<WCS_ORDER_ID/><DELIVERY_CENTRE_GROUP_ID>1</DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";

    domXmlUtil.executeOperations(this.generateOperations());
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedXml, returnXml);
  }

  @Test
  public void testOperationNoWcsOrderId() {
    String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputXML);
    String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER><ORDER_ID>ORDER_ID_VALUE</ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID>1</DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID></NEW_ORDER>";

    domXmlUtil.executeOperations(this.generateOperations());
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedXml, returnXml);
  }

  private List<XmlOperation> generateOperations() {
    return Arrays.asList(
        ModifyOperation.builder()
            .xpath(EXTERNAL_REFERENCE_ID_XPATH)
            .value(ORDER_ID_VALUE)
            .build(),
        AddOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value(EXTERNAL_REFERENCE_ID_VALUE).build())
            .build(),
        ModifyOperation.builder()
            .xpath(DELIVERY_CENTRE_GROUP_ID_XPATH)
            .value(FULFILLMENT_CENTER_ID_VALUE.toString())
            .build()
    );
  }

  @ParameterizedTest
  @MethodSource("testCasesInfo")
  public void testOperation(List<XmlOperation> operations, String inputXml, String expectedXml) {
    String returnXml;
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputXml);
    domXmlUtil.executeOperations(operations);
    returnXml = domXmlUtil.getDocument();
    assertThat(returnXml).isXmlEqualTo(expectedXml);
  }

  private final static List<XmlOperation> addAfterOrUpdate = Collections.singletonList(
      AddAfterOrUpdateOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value(EXTERNAL_REFERENCE_ID_VALUE).build())
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
      );

  private final static List<XmlOperation> addAfter = Collections.singletonList(
      AddAfterOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value(EXTERNAL_REFERENCE_ID_VALUE).build())
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
    );

  private final static List<XmlOperation> addBeforeOrUpdate = Collections.singletonList(
      AddBeforeOrUpdateOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value(EXTERNAL_REFERENCE_ID_VALUE).build())
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
  );

  private final static List<XmlOperation> addBefore = Collections.singletonList(
      AddBeforeOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value(EXTERNAL_REFERENCE_ID_VALUE).build())
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
  );

  private final static List<XmlOperation> remove = Collections.singletonList(
      RemoveOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH + "/" + WCS_ORDER_ID_ELEMENT_NAME)
          .build()
  );

  private final static List<XmlOperation> modify = Collections.singletonList(
      ModifyOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH + "/" + WCS_ORDER_ID_ELEMENT_NAME)
          .value("NEW_VALUE")
          .build()
  );

  private final static List<XmlOperation> add = Collections.singletonList(
      AddOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder().name(WCS_ORDER_ID_ELEMENT_NAME).value("NEW_VALUE").build())
          .build()
  );

  private final static List<XmlOperation> addElement = Collections.singletonList(
      AddOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder()
                  .name(WCS_ORDER_IDS_ELEMENT_NAME)
                  .value(XmlElement.builder()
                      .name(WCS_ORDER_ID_ELEMENT_NAME)
                      .value("NEW_VALUE")
                      .build())
                  .build())
          .build()
  );

  private final static List<XmlOperation> addElementWithAttributes = Collections.singletonList(
      AddOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder()
              .name(WCS_ORDER_IDS_ELEMENT_NAME)
              .value(XmlElement.builder()
                  .name(WCS_ORDER_ID_ELEMENT_NAME)
                  .value("NEW_VALUE")
                  .attributes(Arrays.asList(
                      XmlAttribute.builder()
                          .name("ATTR1")
                          .value("VALUE1")
                          .build(),
                      XmlAttribute.builder()
                          .name("ATTR2")
                          .value("VALUE2")
                          .build()
                      )
                  )
                  .build())
              .build())
          .build()
  );

  private final static List<XmlOperation> addListElements = Collections.singletonList(
      AddOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .element(XmlElement.builder()
              .name(WCS_ORDER_IDS_ELEMENT_NAME)
              .value(
                  Arrays.asList(
                      XmlElement.builder()
                          .name(WCS_ORDER_ID_ELEMENT_NAME)
                          .value("NEW_VALUE")
                          .build(),
                      XmlElement.builder()
                          .name(WCS_ORDER_ID_ELEMENT_NAME)
                          .value("NEW_VALUE2")
                          .build(),
                      XmlElement.builder()
                          .name(WCS_ORDER_ID_ELEMENT_NAME)
                          .value("NEW_VALUE3")
                          .build()
                      )
              )
              .build())
          .build()
  );

  private static Stream<Arguments> testCasesInfo() {
    return Stream.of(
        Arguments.of(
            addAfterOrUpdate,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addAfterOrUpdate/expectedNotExistElement.xml")
        ),
        Arguments.of(
            addAfterOrUpdate,
            getFile("xml/DOM_XML_UTIL/addAfter/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addAfterOrUpdate/expectedExistElement.xml")
        ),
        Arguments.of(
            addAfter,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addAfter/expectedNotExistElement.xml")
        ),
        Arguments.of(
            addAfter,
            getFile("xml/DOM_XML_UTIL/addAfter/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addAfter/expectedExistElement.xml")
        ),
        Arguments.of(
            addBeforeOrUpdate,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addBeforeOrUpdate/expectedNotExistElement.xml")
        ),
        Arguments.of(
            addBeforeOrUpdate,
            getFile("xml/DOM_XML_UTIL/addBefore/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addBeforeOrUpdate/expectedExistElement.xml")
        ),
        Arguments.of(
            addBefore,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addBefore/expectedNotExistElement.xml")
        ),
        Arguments.of(
            addBefore,
            getFile("xml/DOM_XML_UTIL/addBefore/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/addBefore/expectedExistElement.xml")
        ),
        Arguments.of(
            remove,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/remove/expectedNotExistElement.xml")
        ),
        Arguments.of(
            remove,
            getFile("xml/DOM_XML_UTIL/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/remove/expectedExistElement.xml")
        ),
        Arguments.of(
            modify,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/modify/expectedNotExistElement.xml")
        ),
        Arguments.of(
            modify,
            getFile("xml/DOM_XML_UTIL/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/modify/expectedExistElement.xml")
        ),
        Arguments.of(
            add,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/value/expectedNotExistElement.xml")
        ),
        Arguments.of(
            add,
            getFile("xml/DOM_XML_UTIL/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/expectedExistElement.xml")
        ),
        Arguments.of(
            addElement,
            getFile("xml/DOM_XML_UTIL/add/element/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/element/expectedExistElement.xml")
        ),
        Arguments.of(
            addElement,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/element/single/expectedNotExistElement.xml")
        ),
        Arguments.of(
            addElementWithAttributes,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/element/single/expectedNotExistElement.xml")
        ),
        Arguments.of(addListElements,
            getFile("xml/DOM_XML_UTIL/add/element/inputExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/element/expectedExistElement.xml")
        ),
        Arguments.of(addListElements,
            getFile("xml/DOM_XML_UTIL/inputNotExistsElementXML.xml"),
            getFile("xml/DOM_XML_UTIL/add/element/list/expectedNotExistElement.xml")
        )
    );
  }

}
