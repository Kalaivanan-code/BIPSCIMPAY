package com.bornfire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.controller.IPSDao;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.DocType;
import com.bornfire.jaxb.camt_052_001_08.Document;
import com.bornfire.messagebuilder.DocumentPacks;
import com.google.gson.Gson;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Main {
	/*private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd hh:mm:ss";
	private final static DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat(TIMESTAMP_PATTERN);
	private final static TimeZone IST_TIMEZONE = TimeZone.getTimeZone("IST");

	public static String formatTimeStamp(XMLGregorianCalendar cal) {
		if (cal == null)
			return "";
		else {
			return TIMESTAMP_FORMATTER.format(cal.toGregorianCalendar(IST_TIMEZONE, Locale.FRENCH, null).getTime());
		}
	}

	public static void main(String[] args) throws DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());

		XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

		String formmatedDateTimestamp = formatTimeStamp(calendar);
		String mydatetime="2011-09-29T08:55:00Z";
		XMLGregorianCalendar xgc=DatatypeFactory.newInstance().newXMLGregorianCalendar(mydatetime);
		System.out.println(xgc.toString());
		System.out.println(formmatedDateTimestamp);
	}
*/
	  public static void main(String[] args) throws IOException  {
	
		  String name="5454,sydtfysf,,";
		  
		  System.out.println(name.replace(",", ", ").split(",")[1]+"chgd");
		/*
		 * Date dt = new Date(); Calendar c = Calendar.getInstance(); c.setTime(dt);
		 * c.add(Calendar.DATE, 1); dt = c.getTime();
		 * 
		 * System.out.println(dt); Date date = new Date() ; SimpleDateFormat dateFormat
		 * = new SimpleDateFormat("HH:mm") ; dateFormat.format(date);
		 * System.out.println(dateFormat.format(date)); //getUsersFromLdap();
		 * 
		 * NumberFormat formatter = new DecimalFormat("#0.00");
		 * 
		 * if(String.valueOf(Double.parseDouble("10000")).equals(String.valueOf(Double.
		 * parseDouble("10000.00")))){ System.out.println("okkk"); }else {
		 * System.out.println("not"); }
		 * if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse("17:01"))
		 * ) { }else{ }
		 */
		  
		  try {
			  String realmPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArMFLC7eq3rgDKiOJKtJk6CCYsQ47sZ3JhSz+tCciuXwGOzm9LDtLz/WJSDNzsG4hScBCh0EzKDzcVffknyxkkIaPH16wsZXK0axzC/rRCAstTR/Mz6p7gUFm6TkVFT11J51gyN2fObJ3hWjaNUcvQ67cmzNUjuoTK7EOMCequp2G2Kng96N2iLyBHxCMtM9U8U3cUw3JuOuaDFYYwRJyI8796JOyeThjehjOvzr8AzVorGb3rchJTOK4LZwIIkIhNBh4ONbkb8QpssA3USwBDFmS5o2XswYUn1MpaV/aPsUNRf7NAalRZvofQ3DzzOi+Jxz+3Lnh5uotD0Ps2HdkXQIDAQAB";
		        String accessTokenString = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJhbW91bnQiOiIxMDAuMDAiLCJjdXJyZW5jeSI6Ik1VUiIsImRlYnRvckFjY291bnQiOiI5MDMxMDE5MDAwNTMwOCIsImNyZWRpdG9yQWNjb3VudCI6IjAyMzA1Nzc3Nzg5NyIsImVuZFRvRW5kSWQiOiJDTUNMTVVNMDIwMjAwODIwMDA0MDMzIn0.BtBzoQ0bHYoaO1guJ_35r5tJcHwf679NKi-2o9L8r22prRdvFKxK5V2q_YC4w18JKjumQN-X8xoJdIROuDXWLcHrzqaaJbwadnQckDHMmvCJMNP7u1qtfM6vLvynf2bGxoSO0T0OlAw6-uKxbkrgkNVOeRTA29lpnyAHMb-tFAT4hT3Li0iY9l09RnjDBqJlsQ4lMZuJgjrsr-M92FTbIf6aM3QTEX2wsmqxV4wK4AH95bZekYQKCrdN1bGvUnJaOnJepYRgVaK_QwLb0ieKvm6OyD5zQvhqFDeuXlldZrarpi9EWk_EJRQw-ewse_VyRsYBSQ9Hv-GVvR1G4RIKpw";

		        String[] split_string = accessTokenString.split("\\.");
				String base64EncodedHeader = split_string[0];
				String base64EncodedBody = split_string[1];

			/*
			 * Base64 base64Url = new Base64(true); String body = new
			 * String(base64Url.decode(base64EncodedBody)); System.out.println("JWT Body : "
			 * + body);
			 */

		        
		    //    String realmPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzyis1ZjfNB0bBgKFMSvvkTtwlvBsaJq7S5wA+kzeVOVpVWwkWdVha4s38XM/pa/yr47av7+z3VTmvDRyAHcaT92whREFpLv9cj5lTeJSibyr/Mrm/YtjCZVWgaOYIhwrXwKLqPr/11inWsAkfIytvHWTxZYEcXLgAXFuUuaS3uF9gEiNQwzGTU1v0FqkqTBr4B8nW3HCN47XUu0t8Y0e+lf4s4OxQawWD79J9/5d3Ry0vbV3Am1FtGJiJvOwRsIfVChDpYStTcHTCMqtvWbV6L11BWkpzGXSW4Hv43qa+GSYOD2QU68Mb59oSk2OB+BtOLpJofmbGEGgvmwyCI9MwIDAQAB";
		      //  String accessTokenString = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJkZXZpY2VJZCI6IjgyMzY1ODk3MzQ1Njc5ODMyMzA5IiwiYW1vdW50IjoiMzQ3LjAwIiwiY3VycmVuY3kiOiJNVVIiLCJkZWJ0b3JBY2NvdW50IjoiOTAzMTAxOTAwMDQzNjMiLCJjcmVkaXRvckFjY291bnQiOiI5MDMxMDE5MDAwMzUxNSIsImVuZFRvRW5kSWQiOiJCQVJCMDA3In0.RNYXDruZlrUju_oyhU6fYpKz2Ab0gEPfBkhGLt2qjGzvu3GRO-VCqrmS5dAdZos2h0TArBK2va1mCzxOsW7khtKlfizRPT5ai_gQjhKdcGb_pXaIfRXq9wDMCCsxJ3CBKyNj5W5jDUoVPMF_eiqIeOUCMNnSeccmcgjIHdOrE69SeHlcMX5yiNLHhzc8m4v4kQpJ5N31ZiBo_TJZsZ6QSij_OpFLgXSwBUEgVpH7BW4W-bWniSXFSUsmDsC_vsTbzhbOK13cqSCq31YQhPCOvQfB6FjX_wufSrk5Mqqkr_SNlFJRwPofsRzqfNUdBmtxjBjBhA-CRMKB7Sutf_7eOg";

		        
		        PublicKey publicKey = decodePublicKey(pemToDer(realmPublicKey));

		        Jws<Claims> claimsJws = Jwts.parser() //
		                .setSigningKey(publicKey) //
		                .parseClaimsJws(accessTokenString) //
		                ;
		        System.out.println(claimsJws.getBody().get("deviceId")); 
		        System.out.println(claimsJws);
		        
		        CryptogramResponse cryptogramResponse = new Gson().fromJson(claimsJws.getBody().toString(), CryptogramResponse.class);
		        System.out.println(cryptogramResponse.toString());
		        System.out.println(cryptogramResponse.getAmount());
		  }catch(Exception e){
			  System.out.println("iokk");
		  }
		  
	       // SequenceGenerator sms=new SequenceGenerator();
	       // sms.sms1("32457", "23057777897");
	        
	    }
	  
	  public static byte[] pemToDer(String pem) throws IOException {
	        return Base64.getDecoder().decode(stripBeginEnd(pem));
	    }

	    public static String stripBeginEnd(String pem) {

	        String stripped = pem.replaceAll("-----BEGIN (.*)-----", "");
	        stripped = stripped.replaceAll("-----END (.*)----", "");
	        stripped = stripped.replaceAll("\r\n", "");
	        stripped = stripped.replaceAll("\n", "");

	        return stripped.trim();
	    }


	    public static PublicKey decodePublicKey(byte[] der) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

	        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);

	        KeyFactory kf = KeyFactory.getInstance("RSA"
	                //        , "BC" //use provider BouncyCastle if available.
	        );

	        return kf.generatePublic(spec);
	    }
	  
	

	    public static List<SearchResultEntry> getUsersFromLdap() throws LDAPException, LDAPSearchException{
	        String searchBaseDN = "dc=IPS,dc=MAUCAS,dc=MU"; //your-domain.com
	        String searchFilter = "(&(objectClass=user)(sn=*))"; //see e.g. https://confluence.atlassian.com/kb/how-to-write-ldap-search-filters-792496933.html


	        LDAPConnection connection = new LDAPConnection("http://10.200.225.22", 389);
	        try {
	            connection.bind("BARB_ldap_browser@ips.maucas.mu", "YjgH50Nh6vCj");
	            SearchRequest request = new SearchRequest(searchBaseDN, SearchScope.SUB, searchFilter);
	            request.setSizeLimit(0);
	            com.unboundid.ldap.sdk.SearchResult searchResult = connection.search(request);

	            List<SearchResultEntry> result = searchResult.getSearchEntries();
	            return result;
	        } finally {
	            if (connection != null) {
	                connection.close();
	            }
	        }
	    }
	


}
