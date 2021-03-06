//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.28 at 10:25:48 AM GST 
//


package com.bornfire.jaxb.camt_011_001_07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Defines the identification details to uniquely identify a limit within the system.
 * 
 * <p>Java class for LimitIdentification5__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LimitIdentification5__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SysId" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}SystemIdentification2Choice__1" minOccurs="0"/&gt;
 *         &lt;element name="BilLmtCtrPtyId" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}BranchAndFinancialInstitutionIdentification6__1" minOccurs="0"/&gt;
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}LimitType1Choice__1"/&gt;
 *         &lt;element name="AcctOwnr" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}BranchAndFinancialInstitutionIdentification6__1" minOccurs="0"/&gt;
 *         &lt;element name="AcctId" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}AccountIdentification4Choice__1"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LimitIdentification5__1", propOrder = {
    "sysId",
    "bilLmtCtrPtyId",
    "tp",
    "acctOwnr",
    "acctId"
})
public class LimitIdentification51 {

    @XmlElement(name = "SysId")
    protected SystemIdentification2Choice1 sysId;
    @XmlElement(name = "BilLmtCtrPtyId")
    protected BranchAndFinancialInstitutionIdentification61 bilLmtCtrPtyId;
    @XmlElement(name = "Tp", required = true)
    protected LimitType1Choice1 tp;
    @XmlElement(name = "AcctOwnr")
    protected BranchAndFinancialInstitutionIdentification61 acctOwnr;
    @XmlElement(name = "AcctId", required = true)
    protected AccountIdentification4Choice1 acctId;

    /**
     * Gets the value of the sysId property.
     * 
     * @return
     *     possible object is
     *     {@link SystemIdentification2Choice1 }
     *     
     */
    public SystemIdentification2Choice1 getSysId() {
        return sysId;
    }

    /**
     * Sets the value of the sysId property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemIdentification2Choice1 }
     *     
     */
    public void setSysId(SystemIdentification2Choice1 value) {
        this.sysId = value;
    }

    /**
     * Gets the value of the bilLmtCtrPtyId property.
     * 
     * @return
     *     possible object is
     *     {@link BranchAndFinancialInstitutionIdentification61 }
     *     
     */
    public BranchAndFinancialInstitutionIdentification61 getBilLmtCtrPtyId() {
        return bilLmtCtrPtyId;
    }

    /**
     * Sets the value of the bilLmtCtrPtyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchAndFinancialInstitutionIdentification61 }
     *     
     */
    public void setBilLmtCtrPtyId(BranchAndFinancialInstitutionIdentification61 value) {
        this.bilLmtCtrPtyId = value;
    }

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link LimitType1Choice1 }
     *     
     */
    public LimitType1Choice1 getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link LimitType1Choice1 }
     *     
     */
    public void setTp(LimitType1Choice1 value) {
        this.tp = value;
    }

    /**
     * Gets the value of the acctOwnr property.
     * 
     * @return
     *     possible object is
     *     {@link BranchAndFinancialInstitutionIdentification61 }
     *     
     */
    public BranchAndFinancialInstitutionIdentification61 getAcctOwnr() {
        return acctOwnr;
    }

    /**
     * Sets the value of the acctOwnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchAndFinancialInstitutionIdentification61 }
     *     
     */
    public void setAcctOwnr(BranchAndFinancialInstitutionIdentification61 value) {
        this.acctOwnr = value;
    }

    /**
     * Gets the value of the acctId property.
     * 
     * @return
     *     possible object is
     *     {@link AccountIdentification4Choice1 }
     *     
     */
    public AccountIdentification4Choice1 getAcctId() {
        return acctId;
    }

    /**
     * Sets the value of the acctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountIdentification4Choice1 }
     *     
     */
    public void setAcctId(AccountIdentification4Choice1 value) {
        this.acctId = value;
    }

}
