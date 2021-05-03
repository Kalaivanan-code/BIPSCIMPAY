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
 * Business reference(s) to one or more relevant messages previously sent by other parties, or by the same party issuing this message.
 * 
 * .
 * 
 * <p>Java class for OriginalMessageAndIssuer1__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OriginalMessageAndIssuer1__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MsgId" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}Max35Text"/&gt;
 *         &lt;element name="MsgNmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}Max35Text"/&gt;
 *         &lt;element name="OrgtrNm" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}Max70Text" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginalMessageAndIssuer1__1", propOrder = {
    "msgId",
    "msgNmId",
    "orgtrNm"
})
public class OriginalMessageAndIssuer11 {

    @XmlElement(name = "MsgId", required = true)
    protected String msgId;
    @XmlElement(name = "MsgNmId", required = true)
    protected String msgNmId;
    @XmlElement(name = "OrgtrNm")
    protected String orgtrNm;

    /**
     * Gets the value of the msgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Sets the value of the msgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    /**
     * Gets the value of the msgNmId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgNmId() {
        return msgNmId;
    }

    /**
     * Sets the value of the msgNmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgNmId(String value) {
        this.msgNmId = value;
    }

    /**
     * Gets the value of the orgtrNm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgtrNm() {
        return orgtrNm;
    }

    /**
     * Sets the value of the orgtrNm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgtrNm(String value) {
        this.orgtrNm = value;
    }

}
