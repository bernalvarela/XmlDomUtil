package com.bernalvarela.xml;

import static com.bernalvarela.xml.util.XMLTestUtil.getFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

  @Test
  public void testOperationAdd() {
    List<XmlOperation> operation = Collections.singletonList(
        AddOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value("NEW_VALUE")
            .build()
    );

    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expectedNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID>NEW_VALUE</WCS_ORDER_ID></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedNotExistElementXML, returnXml);

    String inputExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistElementXML);
    String expectedExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expectedExistElementXML, returnXml);
  }

  @Test
  public void testOperationModify() {
    List<XmlOperation> operation = Collections.singletonList(
        ModifyOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH + "/" + WCS_ORDER_ID_ELEMENT_NAME)
            .value("NEW_VALUE")
            .build()
    );

    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expectedNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedNotExistElementXML, returnXml);

    String inputExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistElementXML);
    String expectedExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID>NEW_VALUE</WCS_ORDER_ID></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expectedExistElementXML, returnXml);
  }

  @Test
  public void testOperationRemove() {
    List<XmlOperation> operation = Collections.singletonList(
        RemoveOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH + "/" + WCS_ORDER_ID_ELEMENT_NAME)
            .build()
    );

    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expectedNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedNotExistElementXML, returnXml);

    String inputExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistElementXML);
    String expectedExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expectedExistElementXML, returnXml);
  }

  @Test
  public void testOperationAddBefore() {
    List<XmlOperation> operation = Collections.singletonList(
        AddBeforeOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value(EXTERNAL_REFERENCE_ID_VALUE)
            .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
            .build()
    );
    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expectedNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID><ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedNotExistElementXML, returnXml);

    String inputExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistElementXML);
    String expectedExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID/></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expectedExistElementXML, returnXml);
  }

  private List<XmlOperation> generateOperations() {
    return Arrays.asList(
        ModifyOperation.builder()
            .xpath(EXTERNAL_REFERENCE_ID_XPATH)
            .value(ORDER_ID_VALUE)
            .build(),
        AddOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value(EXTERNAL_REFERENCE_ID_VALUE)
            .build(),
        ModifyOperation.builder()
            .xpath(DELIVERY_CENTRE_GROUP_ID_XPATH)
            .value(FULFILLMENT_CENTER_ID_VALUE.toString())
            .build()
    );
  }

  @ParameterizedTest
  @MethodSource("provideStringsForIsBlank")
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
          .elementName(WCS_ORDER_ID_ELEMENT_NAME)
          .value(EXTERNAL_REFERENCE_ID_VALUE)
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
      );

  private final static List<XmlOperation> addAfter = Collections.singletonList(
      AddAfterOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .elementName(WCS_ORDER_ID_ELEMENT_NAME)
          .value(EXTERNAL_REFERENCE_ID_VALUE)
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
    );

  private final static List<XmlOperation> addBeforeOrUpdate = Collections.singletonList(
      AddBeforeOrUpdateOperation.builder()
          .xpath(WCS_ORDER_ID_XPATH)
          .elementName(WCS_ORDER_ID_ELEMENT_NAME)
          .value(EXTERNAL_REFERENCE_ID_VALUE)
          .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
          .build()
  );

  private static Stream<Arguments> provideStringsForIsBlank() {
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
        )
    );
  }

}
