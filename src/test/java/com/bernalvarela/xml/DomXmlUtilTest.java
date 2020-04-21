package com.bernalvarela.xml;

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
import org.junit.jupiter.api.Test;

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

  @Test
  public void testOperationAddBeforeOrUpdate() {
    List<XmlOperation> operation = Collections.singletonList(
        AddBeforeOrUpdateOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value(EXTERNAL_REFERENCE_ID_VALUE)
            .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
            .build()
    );
    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expecteNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID></NEW_ORDER>";

    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expecteNotExistElementXML, returnXml);

    String inputExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID><WCS_ORDER_ID/></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistElementXML);
    String expecteExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/><WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID></NEW_ORDER>";
    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expecteExistElementXML, returnXml);
  }

  @Test
  public void testOperationAddAfter() {
    List<XmlOperation> operation = Collections.singletonList(
        AddAfterOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value(EXTERNAL_REFERENCE_ID_VALUE)
            .contiguousElementXpath("/NEW_ORDER/ORDER_ID").build()
    );
    String inputNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistElementXML);
    String expectedNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER><ORDER_ID/>"
        + "<WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";
    domXmlUtil.executeOperations(operation);
    String returnXml = domXmlUtil.getDocument();
    assertEquals(expectedNotExistElementXML, returnXml);

    String inputExistsElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><WCS_ORDER_ID/><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistsElementXML);
    String expectedExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<WCS_ORDER_ID/><ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";
    domXmlUtil.executeOperations(operation);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expectedExistElementXML, returnXml);
  }

  @Test
  public void testOperationAddAfterOrUpdate() {
    List<XmlOperation> operations = Collections.singletonList(
        AddAfterOrUpdateOperation.builder()
            .xpath(WCS_ORDER_ID_XPATH)
            .elementName(WCS_ORDER_ID_ELEMENT_NAME)
            .value(EXTERNAL_REFERENCE_ID_VALUE)
            .contiguousElementXpath("/NEW_ORDER/ORDER_ID")
            .build()
    );
    String returnXml;

    String inputNotExistsElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    DomXmlUtil domXmlUtil = new DomXmlUtil(inputNotExistsElementXML);
    String expecteNotExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<ORDER_ID/><WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";
    domXmlUtil.executeOperations(operations);
    returnXml = domXmlUtil.getDocument();
    assertEquals(expecteNotExistElementXML, returnXml);

    String inputExistsElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NEW_ORDER><WCS_ORDER_ID/><ORDER_ID></ORDER_ID>"
        + "<DELIVERY_CENTRE_GROUP_ID></DELIVERY_CENTRE_GROUP_ID></NEW_ORDER>";
    domXmlUtil = new DomXmlUtil(inputExistsElementXML);
    domXmlUtil.executeOperations(operations);
    returnXml = domXmlUtil.getDocument();
    String expecteExistElementXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NEW_ORDER>"
        + "<WCS_ORDER_ID>EXTERNAL_REFERENCE_ID_VALUE</WCS_ORDER_ID><ORDER_ID/><DELIVERY_CENTRE_GROUP_ID/></NEW_ORDER>";
    assertEquals(expecteExistElementXML, returnXml);
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
}
