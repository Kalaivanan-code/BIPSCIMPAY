//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.24 at 05:17:49 PM GST 
//


package com.bornfire.jaxb.pain_002_001_10;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides details information on the original transactions, to which the status report message refers.
 * 
 * <p>Java class for OriginalPaymentInstruction32__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OriginalPaymentInstruction32__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OrgnlPmtInfId" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.10}Max35Text"/&gt;
 *         &lt;element name="TxInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.10}PaymentTransaction105__1" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginalPaymentInstruction32__1", propOrder = {
    "orgnlPmtInfId",
    "txInfAndSts"
})
public class OriginalPaymentInstruction321 {

    @XmlElement(name = "OrgnlPmtInfId", required = true)
    protected String orgnlPmtInfId;
    @XmlElement(name = "TxInfAndSts", required = true)
    protected List<PaymentTransaction1051> txInfAndSts;

    /**
     * Gets the value of the orgnlPmtInfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgnlPmtInfId() {
        return orgnlPmtInfId;
    }

    /**
     * Sets the value of the orgnlPmtInfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgnlPmtInfId(String value) {
        this.orgnlPmtInfId = value;
    }

    /**
     * Gets the value of the txInfAndSts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the txInfAndSts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTxInfAndSts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PaymentTransaction1051 }
     * 
     * 
     */
    public List<PaymentTransaction1051> getTxInfAndSts() {
        if (txInfAndSts == null) {
            txInfAndSts = new ArrayList<PaymentTransaction1051>();
        }
        return this.txInfAndSts;
    }

	public void setTxInfAndSts(List<PaymentTransaction1051> txInfAndSts) {
		this.txInfAndSts = txInfAndSts;
	}
    
    

}
