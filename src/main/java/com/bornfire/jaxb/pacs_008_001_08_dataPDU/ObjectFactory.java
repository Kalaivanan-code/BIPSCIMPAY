//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.27 at 06:18:50 PM IST 
//


package com.bornfire.jaxb.pacs_008_001_08_dataPDU;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bornfire.jaxb.pacs_008_001_08_dataPDU package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bornfire.jaxb.pacs_008_001_08_dataPDU
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataPDU }
     * 
     */
    public DataPDU createDataPDU() {
        return new DataPDU();
    }

    /**
     * Create an instance of {@link DataPDU.Body }
     * 
     */
    public DataPDU.Body createDataPDUBody() {
        return new DataPDU.Body();
    }

}
