//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.05.12 at 07:21:15 PM GST 
//


package com.bornfire.jaxb.camt_053_001_08;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Set of elements used to provide summary information on entries.
 * 
 * <p>Java class for TotalTransactions6__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TotalTransactions6__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TtlNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.08}NumberAndSumOfTransactions4__1" minOccurs="0"/&gt;
 *         &lt;element name="TtlCdtNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.08}NumberAndSumOfTransactions1__1"/&gt;
 *         &lt;element name="TtlDbtNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.08}NumberAndSumOfTransactions1__1"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TotalTransactions6__1", propOrder = {
    "ttlNtries",
    "ttlCdtNtries",
    "ttlDbtNtries"
})
public class TotalTransactions61 {

    @XmlElement(name = "TtlNtries")
    protected NumberAndSumOfTransactions41 ttlNtries;
    @XmlElement(name = "TtlCdtNtries", required = true)
    protected NumberAndSumOfTransactions11 ttlCdtNtries;
    @XmlElement(name = "TtlDbtNtries", required = true)
    protected NumberAndSumOfTransactions11 ttlDbtNtries;

    /**
     * Gets the value of the ttlNtries property.
     * 
     * @return
     *     possible object is
     *     {@link NumberAndSumOfTransactions41 }
     *     
     */
    public NumberAndSumOfTransactions41 getTtlNtries() {
        return ttlNtries;
    }

    /**
     * Sets the value of the ttlNtries property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberAndSumOfTransactions41 }
     *     
     */
    public void setTtlNtries(NumberAndSumOfTransactions41 value) {
        this.ttlNtries = value;
    }

    /**
     * Gets the value of the ttlCdtNtries property.
     * 
     * @return
     *     possible object is
     *     {@link NumberAndSumOfTransactions11 }
     *     
     */
    public NumberAndSumOfTransactions11 getTtlCdtNtries() {
        return ttlCdtNtries;
    }

    /**
     * Sets the value of the ttlCdtNtries property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberAndSumOfTransactions11 }
     *     
     */
    public void setTtlCdtNtries(NumberAndSumOfTransactions11 value) {
        this.ttlCdtNtries = value;
    }

    /**
     * Gets the value of the ttlDbtNtries property.
     * 
     * @return
     *     possible object is
     *     {@link NumberAndSumOfTransactions11 }
     *     
     */
    public NumberAndSumOfTransactions11 getTtlDbtNtries() {
        return ttlDbtNtries;
    }

    /**
     * Sets the value of the ttlDbtNtries property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberAndSumOfTransactions11 }
     *     
     */
    public void setTtlDbtNtries(NumberAndSumOfTransactions11 value) {
        this.ttlDbtNtries = value;
    }

}
