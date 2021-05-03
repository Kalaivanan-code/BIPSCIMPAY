package com.bornfire.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.entity.LDAPCrl;

@Component
@Service
public class IpsCertificateDAO {

	private static final Logger logger = LoggerFactory.getLogger(IPSXClient.class);

	private final LdapTemplate ldapTemplate;
	
	private List<X509Certificate> LdapCert;

	private List<LDAPCrl> LdapCrl;

	public List<LDAPCrl> getLdapCrl() {
		return LdapCrl;
	}

	public void setLdapCrl(List<LDAPCrl> ldapCrl) {
		LdapCrl = ldapCrl;
	}
	
	public List<X509Certificate> getLdapCert() {
		return LdapCert;
	}

	public void setLdapCert(List<X509Certificate> ldapCert) {
		LdapCert = ldapCert;
	}

	@Autowired
	public IpsCertificateDAO(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	};

	public List<LDAPCrl> getCertRevocatinoList() {

		LdapQuery query = LdapQueryBuilder.query().searchScope(SearchScope.SUBTREE).timeLimit(3000).countLimit(10)
				.base("CN=CRL,OU=Participants_test").where("cn").is("IPS-IPSX-PCAOFF-CA");
		List<X509CRL> crl = ldapTemplate.search(query, new CrlMapper());

		List<LDAPCrl> ldapCrlList = new ArrayList<LDAPCrl>();
		for (X509CRL a : crl) {
			Set s = a.getRevokedCertificates();
			if (s != null && s.isEmpty() == false) {
				Iterator t = s.iterator();
				while (t.hasNext()) {
					X509CRLEntry entry = (X509CRLEntry) t.next();
					LDAPCrl ldapCrl = new LDAPCrl();
					ldapCrl.setSerialNumber(entry.getSerialNumber().toString());
					ldapCrl.setRevocationDate(entry.getRevocationDate().toString());
					ldapCrlList.add(ldapCrl);
				}
			}
		}

		logger.info("CRL List has been refreshed");
		this.setLdapCrl(ldapCrlList);

		return ldapCrlList;

	}

	//@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public List<LDAPCrl> getCertRevocatinoListAuto() {

		List<LDAPCrl> ldapCrlList = new ArrayList<LDAPCrl>();
		Boolean connect = false;
		int cnt=0;

		do {
			try {
				cnt++;
				logger.info("No of Retry :"+cnt);
				LdapQuery query = LdapQueryBuilder.query().searchScope(SearchScope.SUBTREE).timeLimit(3000)
						.countLimit(10).base("CN=CRL,OU=Participants_test").where("cn").is("IPS-IPSX-PCAOFF-CA");
				List<X509CRL> crl = ldapTemplate.search(query, new CrlMapper());
				connect = true;
				for (X509CRL a : crl) {
					Set s = a.getRevokedCertificates();
					if (s != null && s.isEmpty() == false) {
						Iterator t = s.iterator();
						while (t.hasNext()) {
							X509CRLEntry entry = (X509CRLEntry) t.next();
							LDAPCrl ldapCrl = new LDAPCrl();
							ldapCrl.setSerialNumber(entry.getSerialNumber().toString());
							ldapCrl.setRevocationDate(entry.getRevocationDate().toString());
							ldapCrlList.add(ldapCrl);
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				connect = false;
			}
		} while (!connect);

		this.setLdapCrl(ldapCrlList);

		return ldapCrlList;

	}

	private class CrlMapper implements AttributesMapper<X509CRL> {

		@Override
		public X509CRL mapFromAttributes(Attributes attributes) throws NamingException {

			byte[] octetString = (byte[]) attributes.get("certificaterevocationlist").get(0);

			FileOutputStream fos;
			File file = new File("D:/IPS.crl");
			X509CRL crl = null;
			try {

				fos = new FileOutputStream(file);
				fos.write(octetString);
				fos.close();

				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				FileInputStream in = new FileInputStream(file);
				crl = (X509CRL) cf.generateCRL(in);

			} catch (IOException | CertificateException | CRLException e) {

				e.printStackTrace();
			}

			// TODO Auto-generated method stub
			return crl;
		}

	}

	private class CertMapper implements AttributesMapper<X509Certificate> {

		@Override
		public X509Certificate mapFromAttributes(Attributes attributes) throws NamingException {

			byte[] octetString = (byte[]) attributes.get("userCertificate").get();

			FileOutputStream fos;
			File file = new File("D:/IPS.cer");
			X509Certificate cert = null;

			try {
				fos = new FileOutputStream(file);
				fos.write(octetString);
				fos.close();

				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				FileInputStream in = new FileInputStream(file);
				cert = (X509Certificate) cf.generateCertificate(in);
				System.out.println("public key----"+cert.getPublicKey());
				System.out.println("serial number----"+cert.getSerialNumber().toString());
			} catch (IOException | CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO Auto-generated method stub
			return cert;
		}

	}

	//@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public List<X509Certificate> getCertificates() {
		logger.info("Certificate  has been refreshed");
		LdapQuery query = LdapQueryBuilder.query().searchScope(SearchScope.SUBTREE).timeLimit(3000).countLimit(10)
				.base("CN=cert_publisher_test,OU=Participants_test").where("cn").is("cert_publisher_test");
		List<X509Certificate> cert = ldapTemplate.search(query, new CertMapper());
		
		
		logger.info("public key---->"+cert.get(0).getPublicKey().toString());
		logger.info("Serial Number---->"+cert.get(0).getSerialNumber().toString());
		this.setLdapCert(cert);

		return cert;
		
	}

	
	  /*final KeySelector mockKeySelector = new KeySelector() {
	  
	  @Override public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose,
	  AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException
	  {
	 
	  return new KeySelectorResult() {
	  
	  @Override public Key getKey() { 
		  return signerCertificate.getPublicKey(); } };
	  } };*/
	 

}
