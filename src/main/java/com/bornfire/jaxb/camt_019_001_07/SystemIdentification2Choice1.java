//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.02 at 11:21:03 AM GST 
//


package com.bornfire.jaxb.camt_019_001_07;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Information used to identify a cash clearing system.
 * 
 * <p>Java class for SystemIdentification2Choice__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SystemIdentification2Choice__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="MktInfrstrctrId" type="{urn:iso:std:iso:20022:tech:xsd:camt.019.001.07}MarketInfrastructureIdentification1Choice__1"/&gt;
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
@XmlType(name = "SystemIdentification2Choice__1", propOrder = {
    "mktInfrstrctrId"
})
public class SystemIdentification2Choice1 {

    @XmlElement(name = "MktInfrstrctrId")
    protected MarketInfrastructureIdentification1Choice1 mktInfrstrctrId;

    /**
     * Gets the value of the mktInfrstrctrId property.
     * 
     * @return
     *     possible object is
     *     {@link MarketInfrastructureIdentification1Choice1 }
     *     
     */
    public MarketInfrastructureIdentification1Choice1 getMktInfrstrctrId() {
        return mktInfrstrctrId;
    }

    /**
     * Sets the value of the mktInfrstrctrId property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarketInfrastructureIdentification1Choice1 }
     *     
     */
    public void setMktInfrstrctrId(MarketInfrastructureIdentification1Choice1 value) {
        this.mktInfrstrctrId = value;
    }

}
