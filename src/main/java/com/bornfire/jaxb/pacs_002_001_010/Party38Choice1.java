//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.04 at 04:52:11 PM IST 
//


package com.bornfire.jaxb.pacs_002_001_010;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Nature or use of the account.
 * 
 * <p>Java class for Party38Choice__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Party38Choice__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="OrgId" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10}OrganisationIdentification29__1"/&gt;
 *           &lt;element name="PrvtId" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10}PersonIdentification13__1"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party38Choice__1", propOrder = {
    "orgId",
    "prvtId"
})
public class Party38Choice1 {

    @XmlElement(name = "OrgId")
    protected OrganisationIdentification291 orgId;
    @XmlElement(name = "PrvtId")
    protected PersonIdentification131 prvtId;

    /**
     * Gets the value of the orgId property.
     * 
     * @return
     *     possible object is
     *     {@link OrganisationIdentification291 }
     *     
     */
    public OrganisationIdentification291 getOrgId() {
        return orgId;
    }

    /**
     * Sets the value of the orgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganisationIdentification291 }
     *     
     */
    public void setOrgId(OrganisationIdentification291 value) {
        this.orgId = value;
    }

    /**
     * Gets the value of the prvtId property.
     * 
     * @return
     *     possible object is
     *     {@link PersonIdentification131 }
     *     
     */
    public PersonIdentification131 getPrvtId() {
        return prvtId;
    }

    /**
     * Sets the value of the prvtId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonIdentification131 }
     *     
     */
    public void setPrvtId(PersonIdentification131 value) {
        this.prvtId = value;
    }

}
