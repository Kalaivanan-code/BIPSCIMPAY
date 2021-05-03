//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.28 at 10:18:56 AM GST 
//


package com.bornfire.jaxb.camt_025_001_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides details on the request.
 * 
 * <p>Java class for Receipt3__1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Receipt3__1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OrgnlMsgId" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}OriginalMessageAndIssuer1__1"/&gt;
 *         &lt;element name="ReqHdlg" type="{urn:iso:std:iso:20022:tech:xsd:camt.025.001.05}RequestHandling1" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Receipt3__1", propOrder = {
    "orgnlMsgId",
    "reqHdlg"
})
public class Receipt31 {

    @XmlElement(name = "OrgnlMsgId", required = true)
    protected OriginalMessageAndIssuer11 orgnlMsgId;
    @XmlElement(name = "ReqHdlg", required = true)
    protected List<RequestHandling1> reqHdlg;

    /**
     * Gets the value of the orgnlMsgId property.
     * 
     * @return
     *     possible object is
     *     {@link OriginalMessageAndIssuer11 }
     *     
     */
    public OriginalMessageAndIssuer11 getOrgnlMsgId() {
        return orgnlMsgId;
    }

    /**
     * Sets the value of the orgnlMsgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginalMessageAndIssuer11 }
     *     
     */
    public void setOrgnlMsgId(OriginalMessageAndIssuer11 value) {
        this.orgnlMsgId = value;
    }

    /**
     * Gets the value of the reqHdlg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reqHdlg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReqHdlg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RequestHandling1 }
     * 
     * 
     */
    public List<RequestHandling1> getReqHdlg() {
        if (reqHdlg == null) {
            reqHdlg = new ArrayList<RequestHandling1>();
        }
        return this.reqHdlg;
    }

}
