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
 * Choice between an amount with or without the currency.
 * 
 * <p>Java class for Amount2Choice__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Amount2Choice__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="AmtWthCcy" type="{urn:iso:std:iso:20022:tech:xsd:camt.011.001.07}ActiveCurrencyAndAmount"/&gt;
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
@XmlType(name = "Amount2Choice__1", propOrder = {
    "amtWthCcy"
})
public class Amount2Choice1 {

    @XmlElement(name = "AmtWthCcy")
    protected ActiveCurrencyAndAmount amtWthCcy;

    /**
     * Gets the value of the amtWthCcy property.
     * 
     * @return
     *     possible object is
     *     {@link ActiveCurrencyAndAmount }
     *     
     */
    public ActiveCurrencyAndAmount getAmtWthCcy() {
        return amtWthCcy;
    }

    /**
     * Sets the value of the amtWthCcy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActiveCurrencyAndAmount }
     *     
     */
    public void setAmtWthCcy(ActiveCurrencyAndAmount value) {
        this.amtWthCcy = value;
    }

}
