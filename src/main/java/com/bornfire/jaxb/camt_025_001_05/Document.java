//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.28 at 10:18:56 AM GST 
//


package com.bornfire.jaxb.camt_025_001_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Rct" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}ReceiptV05"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
    "rct"
})
public class Document {

    @XmlElement(name = "Rct", required = true)
    protected ReceiptV05 rct;

    /**
     * Gets the value of the rct property.
     * 
     * @return
     *     possible object is
     *     {@link ReceiptV05 }
     *     
     */
    public ReceiptV05 getRct() {
        return rct;
    }

    /**
     * Sets the value of the rct property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiptV05 }
     *     
     */
    public void setRct(ReceiptV05 value) {
        this.rct = value;
    }

}
