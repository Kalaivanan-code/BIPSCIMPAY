package com.bornfire.messagebuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.CronJobScheduler;
import com.bornfire.config.IpsCertificateDAO;
import com.bornfire.entity.LDAPCrl;

@Component
public class SignDocument {

	private static final Logger logger = LoggerFactory.getLogger(SignDocument.class);

	 private static DocumentBuilderFactory dbf;
	 private static KeyStore ks ;
	    static {
	            // one time instance creation
	        	dbf = DocumentBuilderFactory.newInstance();
	    }
	    
	    static {
	        try {
	            // one time instance creation
	        	ks = KeyStore.getInstance("JKS");
	        } catch ( KeyStoreException e) {
	            throw new IllegalStateException(e);
	        }
	    }
		 
	    
	@Autowired
	CronJobScheduler cronJobScheduler;

	@Autowired
	Environment env;

	public String parseDoc(String document) {
		logger.info("Inside Sign Parse Doc Initial");
		DocumentBuilder db = null;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(document)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		String signedString = signDoc(doc);
		logger.info("Output Sign Doc "+signedString);
		return signedString;
	}

	private String signDoc(Document doc) {

		try {
			logger.info("Inside Sign Doc Initial");
			// Getting JKS Keystore
			//KeyStore ks = KeyStore.getInstance("JKS");
			char[] pwdArray = env.getProperty("sign.pwd").toCharArray();

			//ks.load(new FileInputStream(ResourceUtils.getFile("classpath:ipsx.jks")), pwdArray);
			//ks.load(new FileInputStream(ResourceUtils.getFile("classpath:ipsxprod.jks")), pwdArray);
			ks.load(new FileInputStream(env.getProperty("sign.file")), pwdArray);

			logger.info("Inside Sign File Calling Initial");
			Enumeration<String> e = ks.aliases();
			String alis = e.nextElement();

			// Get the Certificate from keystore
			Certificate[] chain = ks.getCertificateChain(alis);
			X509Certificate cert = (X509Certificate) chain[0];

			// Signing Properties
			final String xadesNS = "http://uri.etsi.org/01903/v1.3.2#";
			final String signedpropsIdSuffix = "-signedprops";
			final String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");

			// Create a SignFactory
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
					(Provider) Class.forName(providerName).newInstance());

			// 1. Prepare KeyInfo
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			X509IssuerSerial x509is = kif.newX509IssuerSerial(cert.getIssuerX500Principal().toString(),
					cert.getSerialNumber());
			X509Data x509data = kif.newX509Data(Collections.singletonList(x509is));
			final String keyInfoId = "_" + UUID.randomUUID().toString();
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509data), keyInfoId);

			// 2. Prepare SignInfo and references
			List<Reference> refs = new ArrayList<Reference>();
			// Referece1
			Reference ref1 = fac.newReference("#" + keyInfoId, fac.newDigestMethod(DigestMethod.SHA1, null),
					Collections.singletonList(
							fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null)),
					null, null);
			refs.add(ref1);
			final String signedpropsId = "_" + UUID.randomUUID().toString() + signedpropsIdSuffix;
			// Reference2
			Reference ref2 = fac.newReference("#" + signedpropsId, fac.newDigestMethod(DigestMethod.SHA1, null),
					Collections.singletonList(
							fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null)),
					"http://uri.etsi.org/01903/v1.3.2#SignedProperties", null);
			refs.add(ref2);
			// Reference3
			Reference ref3 = fac.newReference(null, fac.newDigestMethod(DigestMethod.SHA1, null),
					Collections.singletonList(
							fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null)),
					null, null);
			refs.add(ref3);
			SignedInfo si = fac.newSignedInfo(
					fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null),
					fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), refs);

			// 3. Create element Sgntr inside AppHdr that will contain the <ds:Signature>

			Node appHdr = null;
			NodeList sgntrList = doc.getElementsByTagName("AppHdr");
			logger.info("Inside Sign File Calling App Hdr");
			if (sgntrList.getLength() != 0)
				appHdr = sgntrList.item(0);
			if (appHdr == null)
				throw new Exception("mandatory element AppHdr is missing in the document to be signed");
			Node sgntr = appHdr.appendChild(doc.createElement("Sgntr"));
			logger.info(appHdr.getNodeName());
			DOMSignContext dsc;
			logger.debug("Inside Sign File Under Sgntr");
			dsc = new DOMSignContext(ks.getKey(alis, pwdArray), sgntr);

			dsc.putNamespacePrefix(XMLSignature.XMLNS, "ds");

			// 4. Set up <ds:Object> with <QualifiyingProperties> inside that includes
			// SigningTime
			Element QPElement = doc.createElementNS(xadesNS, "xades:QualifyingProperties");
			Element SPElement = doc.createElementNS(xadesNS, "xades:SignedProperties");
			SPElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xades", xadesNS);
			SPElement.setAttributeNS(null, "Id", signedpropsId);
			dsc.setIdAttributeNS(SPElement, null, "Id");
			QPElement.appendChild(SPElement);
			Element SSPElement = doc.createElementNS(xadesNS, "xades:SignedSignatureProperties");
			SPElement.appendChild(SSPElement);
			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			String signingTime = df.format(new Date());
			Element STElement = doc.createElementNS(xadesNS, "xades:SigningTime");
			STElement.appendChild(doc.createTextNode(signingTime));
			SSPElement.appendChild(STElement);
			DOMStructure qualifPropStruct = new DOMStructure(QPElement);
			List<DOMStructure> xmlObj = new ArrayList<DOMStructure>();
			xmlObj.add(qualifPropStruct);
			XMLObject object = fac.newXMLObject(xmlObj, null, null, null);
			List<XMLObject> objects = Collections.singletonList(object);

			// 5. Set up custom URIDereferencer to process Reference without URI.
			// This Reference points to element <Document> of MX message
			final NodeList docNodes = doc.getElementsByTagName("Document");

			final Node docNode = docNodes.item(0);
			ByteArrayOutputStream refOutputStream = new ByteArrayOutputStream();
			Transformer xform = TransformerFactory.newInstance().newTransformer();
			xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			xform.transform(new DOMSource(docNode), new StreamResult(refOutputStream));
			InputStream refInputStream = new ByteArrayInputStream(refOutputStream.toByteArray());
			dsc.setURIDereferencer(new NoUriDereferencer(refInputStream));
			// 6. sign it!
			XMLSignature signature = fac.newXMLSignature(si, ki, objects, null, null);
			signature.sign(dsc);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();

		try {
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return writer.toString().replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
	}
	
	public Boolean verifySignParseDoc(String document) {
		DocumentBuilderFactory db = null;
		Document doc = null;
		try {
			 //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            dbf.setNamespaceAware(true);
	            DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(document)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		Boolean signedString = false;
		try {
			signedString = verify(doc);
			return signedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signedString;
	}

	public Boolean verify(Document doc) throws Exception {
		
		/*try{
	        if (doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").getLength() == 0)
	            throw new Exception("Cannot find Signature element");

	        DOMValidateContext valContext = new DOMValidateContext(cronJobScheduler.getLdapCert().get(0).getPublicKey(), doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0));

	        XMLSignature signature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(valContext);

	        logger.info(String.valueOf(signature.validate(valContext)));
	       // return signature.validate(valContext); 
	    }catch(Exception e){e.printStackTrace();}*/
		
		
		XPath xpath = XPathFactory.newInstance().newXPath();
        String xpathExpression = "//*[local-name()='Signature']";
        NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, doc.getDocumentElement(), XPathConstants.NODESET);
        if (nodes == null || nodes.getLength() == 0) {
        	logger.info("Signature is missing in the document");
        	return false;
        	//throw new Exception("Signature is missing in signature");
        }
        Node nodeSignature = nodes.item(0);
        
       // logger.info(String.valueOf(cronJobScheduler.getLdapCert().size()));

        final KeySelector mockKeySelector = new KeySelector() {

            @Override
            public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {

                return new KeySelectorResult() {
                    @Override
                    public Key getKey() {
                        return cronJobScheduler.getLdapCert().get(0).getPublicKey();
                    }
                };
            }
        };

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        DOMValidateContext valContext = new DOMValidateContext(mockKeySelector, nodeSignature);

        final NodeList docNodes = doc.getElementsByTagName("Document");
        final Node docNode = docNodes.item(0);

        ByteArrayOutputStream refOutputStream = new ByteArrayOutputStream();
        //refOutputStream.reset();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(docNode), new StreamResult(refOutputStream));
        InputStream refInputStream = null;
        refInputStream=new ByteArrayInputStream(refOutputStream.toByteArray());
        DOMSignContext dsc = new DOMSignContext(cronJobScheduler.getLdapCert().get(0).getPublicKey(), doc.getDocumentElement()); //added ----------
       // dsc.putNamespacePrefix(XMLSignature.XMLNS, "ds"); //  ---------added
        dsc.setURIDereferencer(new NoUriDereferencer(refInputStream));

        NodeList nl = doc.getElementsByTagNameNS("http://uri.etsi.org/01903/v1.3.2#", "SignedProperties");
        if (nl.getLength() == 0) {
        	logger.info("Signature is missing in signature");
        	return false;
           // throw new Exception("Signature is missing in signature");
        }
        
       
        
        Element elemSignedProps = (Element) nl.item(0);
        valContext.setIdAttributeNS(elemSignedProps, null, "Id");
        valContext.setURIDereferencer(dsc.getURIDereferencer());

        XMLSignature signature = fac.unmarshalXMLSignature(valContext);
        boolean coreValidity=false;
        try {
            if (signature.validate(valContext)) {
            	logger.info("signature verified");
                // signature verified
            	coreValidity=true;
            } else {
            	coreValidity=false;
                // signature verification failed
            	logger.info("Signature failed core validation");
                boolean sv = signature.getSignatureValue().validate(valContext);
                logger.info("signature validation status: " + sv);
                // check the validation status of each Reference
                Iterator i = signature.getSignedInfo().getReferences().iterator();
                for (int j = 0; i.hasNext(); j++) {
                    final Reference ref = (Reference) i.next();
                    final String refURI = ref.getURI();
                    boolean refValid = ref.validate(valContext);
                    logger.info("ref[" + j + "] validity status: " + refValid + ", ref URI: [" + refURI + "]");
                }
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
       
		return coreValidity;
		
	}

	/*
	 * private Boolean isRevoked(Document doc) { Boolean isRevoked = false; String
	 * signSrlNo = "";
	 * 
	 * NodeList nl = doc.getElementsByTagName("Signature"); for (int i = 0; i <
	 * nl.getLength(); i++) { Node node = nl.item(i); if (node.getNodeType() ==
	 * Node.ELEMENT_NODE) { Element element = (Element) node;
	 * 
	 * signSrlNo = getValue("X509SerialNumber", element);
	 * System.out.println("Issuer Name " + getValue("X509IssuerName", element));
	 * System.out.println("Serial Number " + getValue("X509SerialNumber", element));
	 * 
	 * } }
	 * 
	 * for (LDAPCrl crl : cronJobScheduler.getLdapCrl()) { if
	 * (!crl.getSerialNumber().equals(signSrlNo)) { isRevoked = false; continue; }
	 * else { isRevoked = true; break; } }
	 * 
	 * return isRevoked;
	 * 
	 * }
	 */

	/*
	 * private Boolean verifyCertificate(Document doc) { Boolean isRevoked = false;
	 * String signSrlNo = "";
	 * 
	 * NodeList nl = doc.getElementsByTagName("Signature"); for (int i = 0; i <
	 * nl.getLength(); i++) { Node node = nl.item(i); if (node.getNodeType() ==
	 * Node.ELEMENT_NODE) { Element element = (Element) node;
	 * 
	 * signSrlNo = getValue("X509SerialNumber", element);
	 * System.out.println("Issuer Name " + getValue("X509IssuerName", element));
	 * System.out.println("Serial Number " + getValue("X509SerialNumber", element));
	 * 
	 * } }
	 * 
	 * for (LDAPCrl crl : cronJobScheduler.getLdapCrl()) { if
	 * (!crl.getSerialNumber().equals(signSrlNo)) { isRevoked = false; continue; }
	 * else { isRevoked = true; break; } }
	 * 
	 * return isRevoked;
	 * 
	 * }
	 */

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodes.item(0);
		return node.getNodeValue();
	}
	
	
	

}
