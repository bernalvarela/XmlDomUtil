# XmlDomUtil
___

XmlDomUtil is a library to easily modify a xml using the Document Object Model (DOM) api provided by the jdk.

## Getting Started

At this moment the library is only published in the github repository. To use it we have to add this repository to 
the distributionManagement configuration in the pom.xml file.

```xml
<distributionManagement>
  <repository>
    <id>github</id>
    <name>GitHub Packages</name>
    <url>https://maven.pkg.github.com/bernalvarela/XmlDomUtil</url>
 </repository>
</distributionManagement>
```

And add the library to our dependencies.

```xml
<dependency>
  <groupId>com.bernalvarela</groupId>
  <artifactId>XmlDomUtil</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
Once added the dependency we can start to use it.

The xml used in the example is the next:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<note>
  <to>Tove</to>
  <from>Jani</from>
  <heading>Reminder</heading>
  <body>Don't forget me this weekend!</body>
</note>
```

We have to instantiate the DomXmlUtil passing the xml value as a string:

```java
String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
DomXmlUtil domXmlUtil = new DomXmlUtil(inputXML);
```

Define the operations we want to perform.

In the next example code
```java
List<XmlOperation> operations = Arrays.asList(
    ModifyOperation.builder()
        .xpath("/note/from")
        .value("JANI")
        .build(),
    AddOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .build()
);
```

And execute them

```java
domXmlUtil.executeOperations(this.generateOperations());
String returnXml = domXmlUtil.getDocument();
```

The generated xml will be:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<note>
  <to>Tove</to>
  <from>JANI</from>
  <heading>Reminder</heading>
  <body>Don't forget me this weekend!</body>
  <comentary>COMENTARY_VALUE</comentary>
</note>
```
___

### Operations
The existing operations are:

##### AddAfterOperation:
We give the name and value of a new element, and the xpath of the element after we want to place it.
If the xpath of the element don't exist, the new element will be placed at the end of the xml.
If the element we want to create already exists the operation will do nothing.
```java
List<XmlOperation> operations = Arrays.asList(
    AddAfterOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .contiguousElementXpath("/note/from")
        .build()
);
```


##### AddAfterOrUpdateOperation:
This operation acts like AddAfterOperation with a difference, if the element already exists the value it's updated.
```java
List<XmlOperation> operations = Arrays.asList(
    AddAfterOrUpdateOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .contiguousElementXpath("/note/from")
        .build()
);
```

##### AddBeforeOperation:
We give the name and value of a new element, and the xpath of the element before we want to place it.
If the xpath of the element don't exist, the new element will be placed at the end of the xml.
If the element we want to create already exists the operation will do nothing.
```java
List<XmlOperation> operations = Arrays.asList(
    AddBeforeOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .contiguousElementXpath("/note/from")
        .build()
);
```


##### AddBeforeOrUpdateOperation:
This operation acts like AddBeforeOperation with a difference, if the element already exists the value it's updated.
```java
List<XmlOperation> operations = Arrays.asList(
    AddBeforeOrUpdateOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .contiguousElementXpath("/note/from")
        .build()
);
```

##### AddOperation:
We give the name and value of a new element.
The new element will be placed at the end of the xml.
If the element we want to create already exists the operation will do nothing.
```java
List<XmlOperation> operations = Arrays.asList(
    AddOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .build()
);
```

##### AddOrUpdateOperation:
This operation acts like AddOperation with a difference, if the element already exists the value it's updated.
```java
List<XmlOperation> operations = Arrays.asList(
    AddOrUpdateOperation.builder()
        .xpath("/note")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .build()
);
```

This operation changes the value of a given element if it exists.
```java
List<XmlOperation> operations = Arrays.asList(
    ModifyOperation.builder()
        .xpath("/note/to")
        .element("newValue"").build())
        .build()
);

List<XmlOperation> operations = Arrays.asList(
    ModifyOperation.builder()
        .xpath("/note/to")
        .element(XmlElement.builder().name("comentary").value("COMENTARY_VALUE").build())
        .build()
);
```

##### RemoveOperation:
This operation deletes an element if it exists.
```java
List<XmlOperation> operations = Arrays.asList(
    RemoveOperation.builder()
        .xpath("/note/to")
        .build()
);
```