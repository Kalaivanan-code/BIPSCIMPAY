//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.28 at 10:18:56 AM GST 
//


package com.bornfire.jaxb.camt_025_001_05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bornfire.jaxb.camt_025_001_05 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Document_QNAME = new QName("urn:iso:std:iso:20022:tech:xsd:camt.025.001.05", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bornfire.jaxb.camt_025_001_05
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link GenericIdentification11 }
     * 
     */
    public GenericIdentification11 createGenericIdentification11() {
        return new GenericIdentification11();
    }

    /**
     * Create an instance of {@link MessageHeader91 }
     * 
     */
    public MessageHeader91 createMessageHeader91() {
        return new MessageHeader91();
    }

    /**
     * Create an instance of {@link OriginalMessageAndIssuer11 }
     * 
     */
    public OriginalMessageAndIssuer11 createOriginalMessageAndIssuer11() {
        return new OriginalMessageAndIssuer11();
    }

    /**
     * Create an instance of {@link Receipt31 }
     * 
     */
    public Receipt31 createReceipt31() {
        return new Receipt31();
    }

    /**
     * Create an instance of {@link ReceiptV05 }
     * 
     */
    public ReceiptV05 createReceiptV05() {
        return new ReceiptV05();
    }

    /**
     * Create an instance of {@link RequestHandling1 }
     * 
     */
    public RequestHandling1 createRequestHandling1() {
        return new RequestHandling1();
    }

    /**
     * Create an instance of {@link RequestType4Choice1 }
     * 
     */
    public RequestType4Choice1 createRequestType4Choice1() {
        return new RequestType4Choice1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Document }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Document }{@code >}
     */
    @XmlElementDecl(namespace = "urn:iso:std:iso:20022:tech:xsd:camt.025.001.05", name = "Document")
    public JAXBElement<Document> createDocument(Document value) {
        return new JAXBElement<Document>(_Document_QNAME, Document.class, null, value);
    }

}
