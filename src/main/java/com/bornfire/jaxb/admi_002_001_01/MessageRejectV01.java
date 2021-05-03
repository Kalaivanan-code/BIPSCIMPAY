//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.07 at 05:37:08 PM IST 
//


package com.bornfire.jaxb.admi_002_001_01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Scope
 * The MessageReject message is sent by a central system to notify the rejection of a previously received message.
 * Usage
 * The message provides specific information about the rejection reason.
 * 
 * <p>Java class for MessageRejectV01 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageRejectV01"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RltdRef" type="{urn:iso:std:iso:20022:tech:xsd:admi.002.001.01}MessageReference"/&gt;
 *         &lt;element name="Rsn" type="{urn:iso:std:iso:20022:tech:xsd:admi.002.001.01}RejectionReason2__1"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageRejectV01", propOrder = {
    "rltdRef",
    "rsn"
})
public class MessageRejectV01 {

    @XmlElement(name = "RltdRef", required = true)
    protected MessageReference rltdRef;
    @XmlElement(name = "Rsn", required = true)
    protected RejectionReason21 rsn;

    /**
     * Gets the value of the rltdRef property.
     * 
     * @return
     *     possible object is
     *     {@link MessageReference }
     *     
     */
    public MessageReference getRltdRef() {
        return rltdRef;
    }

    /**
     * Sets the value of the rltdRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageReference }
     *     
     */
    public void setRltdRef(MessageReference value) {
        this.rltdRef = value;
    }

    /**
     * Gets the value of the rsn property.
     * 
     * @return
     *     possible object is
     *     {@link RejectionReason21 }
     *     
     */
    public RejectionReason21 getRsn() {
        return rsn;
    }

    /**
     * Sets the value of the rsn property.
     * 
     * @param value
     *     allowed object is
     *     {@link RejectionReason21 }
     *     
     */
    public void setRsn(RejectionReason21 value) {
        this.rsn = value;
    }

}
