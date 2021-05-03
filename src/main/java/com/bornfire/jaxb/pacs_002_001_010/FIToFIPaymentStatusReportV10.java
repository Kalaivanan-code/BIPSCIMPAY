//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.04 at 04:52:11 PM IST 
//


package com.bornfire.jaxb.pacs_002_001_010;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Scope
 * The FIToFIPaymentStatusReport message is sent by an instructed agent to the previous party in the payment chain. It is used to inform this party about the positive or negative status of an instruction (either single or file). It is also used to report on a pending instruction.
 * Usage
 * The FIToFIPaymentStatusReport message is exchanged between agents to provide status information about instructions previously sent. Its usage will always be governed by a bilateral agreement between the agents.
 * The FIToFIPaymentStatusReport message can be used to provide information about the status (e.g. rejection, acceptance) of a credit transfer instruction, a direct debit instruction, as well as other intra-agent instructions (for example FIToFIPaymentCancellationRequest).
 * The FIToFIPaymentStatusReport message refers to the original instruction(s) by means of references only or by means of references and a set of elements from the original instruction.
 * The FIToFIPaymentStatusReport message can be used in domestic and cross-border scenarios.
 * The FIToFIPaymentStatusReport may also be sent to the receiver of the payment in a real time payment scenario, as both sides of the transactions must be informed of the status of the transaction (for example either the beneficiary is credited, or the transaction is rejected).
 * 
 * <p>Java class for FIToFIPaymentStatusReportV10 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FIToFIPaymentStatusReportV10"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GrpHdr" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10}GroupHeader91__1"/&gt;
 *         &lt;element name="OrgnlGrpInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10}OriginalGroupHeader17__1"/&gt;
 *         &lt;element name="TxInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10}PaymentTransaction110__1" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FIToFIPaymentStatusReportV10", propOrder = {
    "grpHdr",
    "orgnlGrpInfAndSts",
    "txInfAndSts"
})
public class FIToFIPaymentStatusReportV10 {

    @XmlElement(name = "GrpHdr", required = true)
    protected GroupHeader911 grpHdr;
    @XmlElement(name = "OrgnlGrpInfAndSts", required = true)
    protected OriginalGroupHeader171 orgnlGrpInfAndSts;
    @XmlElement(name = "TxInfAndSts", required = true)
    protected List<PaymentTransaction1101> txInfAndSts;

    /**
     * Gets the value of the grpHdr property.
     * 
     * @return
     *     possible object is
     *     {@link GroupHeader911 }
     *     
     */
    public GroupHeader911 getGrpHdr() {
        return grpHdr;
    }

    /**
     * Sets the value of the grpHdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupHeader911 }
     *     
     */
    public void setGrpHdr(GroupHeader911 value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the orgnlGrpInfAndSts property.
     * 
     * @return
     *     possible object is
     *     {@link OriginalGroupHeader171 }
     *     
     */
    public OriginalGroupHeader171 getOrgnlGrpInfAndSts() {
        return orgnlGrpInfAndSts;
    }

    /**
     * Sets the value of the orgnlGrpInfAndSts property.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginalGroupHeader171 }
     *     
     */
    public void setOrgnlGrpInfAndSts(OriginalGroupHeader171 value) {
        this.orgnlGrpInfAndSts = value;
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
     * {@link PaymentTransaction1101 }
     * 
     * 
     */
    public List<PaymentTransaction1101> getTxInfAndSts() {
        if (txInfAndSts == null) {
            txInfAndSts = new ArrayList<PaymentTransaction1101>();
        }
        return this.txInfAndSts;
    }

	public void setTxInfAndSts(List<PaymentTransaction1101> txInfAndSts) {
		this.txInfAndSts = txInfAndSts;
	}

    
}
