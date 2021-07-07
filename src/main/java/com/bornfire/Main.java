package com.bornfire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.controller.IPSDao;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.DocType;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.TranIPSTable;
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
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Main {
		  public static void main(String[] args) throws IOException  {
	
		  
		  SequenceGenerator seq=new SequenceGenerator();
		 
		/*  for (int i=0;i<10;i++) {
			  System.out.println("inside"+i);
				if (i==5) {
					System.out.println("OK"+i);
					break;
				}
				System.out.println("outside"+i);
			}
		  */
		  System.out.println( "Seq:"+seq.generateRequestUUId());
		  
			String dataFormat = null;
	  dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(new Date());
			/*switch(type) {
			case "0":
				dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
				break;
			case "1":
				dataFormat=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				break;
			}*/
			XMLGregorianCalendar xgc = null;
			try {
				xgc = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(dataFormat);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			
		

			System.out.println(xgc);
	  
		  String name="5454,sydtfysf,,";
		  
		  String name1="100";
		 Integer dd= Integer.valueOf(name1);
		  System.out.println(name1);

		  
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
			  
			  KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
				keyGenerator.initialize(1024);

				KeyPair kp = keyGenerator.genKeyPair();
				PublicKey publicKey1 = (PublicKey) kp.getPublic();
				PrivateKey privateKey = (PrivateKey) kp.getPrivate();

				String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey1.getEncoded());
				System.out.println("Public Key:");
				System.out.println(convertToPublicKey(encodedPublicKey));
				String token = generateJwtToken(privateKey);
				System.out.println("TOKEN:");
				System.out.println(token);
				printStructure(token, publicKey1);
				
			 // String realmPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmZ3Tad38LTWRLXFKm1jLeJxsTwVhi3cU+w5hgJQt/pxRlems4LHVlflgXXLruOPmcfedMhktQkBCUX/g3h5UL4+JmNHxBFYhrrezgzqCC/GiufD1UqpF3T0xQLWnR8mXBWULQZBXKGjSxWXgRTtJGQUgJ2Tk4GlI12nuumqNkNwIDAQAB";
		      //  String accessTokenString = "eyJhbGciOiJSUzUxMiJ9.eyJyb2xlIjoidXNlciIsImNyZWF0ZWQiOjE2MjI0NDcxMjAyOTAsImlkIjoieHh4In0.YXW4H1Io-_O49R9sDqzjgTvMBuRjlc7kDVlYxzR4-EMq-LXB5dMSopjqOKl-XHu-RInyOgoN2BkGxboFkNXo6jLxe3SJ639tqvdPxkdtxdHGHCZmAJdttaw_82eUI66mi9rMhtG2hfzTHdms44tOB6lqKAFuW_dnra8pIXfR_us";

				String realmPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzyis1ZjfNB0bBgKFMSvvkTtwlvBsaJq7S5wA+kzeVOVpVWwkWdVha4s38XM/pa/yr47av7+z3VTmvDRyAHcaT92whREFpLv9cj5lTeJSibyr/Mrm/YtjCZVWgaOYIhwrXwKLqPr/11inWsAkfIytvHWTxZYEcXLgAXFuUuaS3uF9gEiNQwzGTU1v0FqkqTBr4B8nW3HCN47XUu0t8Y0e+lf4s4OxQawWD79J9/5d3Ry0vbV3Am1FtGJiJvOwRsIfVChDpYStTcHTCMqtvWbV6L11BWkpzGXSW4Hv43qa+GSYOD2QU68Mb59oSk2OB+BtOLpJofmbGEGgvmwyCI9MwIDAQAB";
		        String accessTokenString = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJkZXZpY2VJZCI6IjgyMzY1ODk3MzQ1Njc5ODMyMzA5IiwiYW1vdW50IjoiMzQ3LjAwIiwiY3VycmVuY3kiOiJNVVIiLCJkZWJ0b3JBY2NvdW50IjoiOTAzMTAxOTAwMDQzNjMiLCJjcmVkaXRvckFjY291bnQiOiI5MDMxMDE5MDAwMzUxNSIsImVuZFRvRW5kSWQiOiJCQVJCMDA3In0.RNYXDruZlrUju_oyhU6fYpKz2Ab0gEPfBkhGLt2qjGzvu3GRO-VCqrmS5dAdZos2h0TArBK2va1mCzxOsW7khtKlfizRPT5ai_gQjhKdcGb_pXaIfRXq9wDMCCsxJ3CBKyNj5W5jDUoVPMF_eiqIeOUCMNnSeccmcgjIHdOrE69SeHlcMX5yiNLHhzc8m4v4kQpJ5N31ZiBo_TJZsZ6QSij_OpFLgXSwBUEgVpH7BW4W-bWniSXFSUsmDsC_vsTbzhbOK13cqSCq31YQhPCOvQfB6FjX_wufSrk5Mqqkr_SNlFJRwPofsRzqfNUdBmtxjBjBhA-CRMKB7Sutf_7eOg";

		        
		        
		        String[] split_string = accessTokenString.split("\\.");
				String base64EncodedHeader = split_string[0];
				String base64EncodedBody = split_string[1];

			/*
			 * Base64 base64Url = new Base64(true); String body = new
			 * String(base64Url.decode(base64EncodedBody)); System.out.println("JWT Body : "
			 * + body);
			 */

		        
		        String savePath="blrs/gg1/home/BLRS_DOCS//BLRS_DOCS//07-06-2021/010204861LN00003/REM1";
		        
		        File folders = new File(savePath);
				 System.out.println(savePath);

				 boolean success11 = true;
				 if (!folders.exists()) {

					 System.out.println("Folder Created");
				 success11 = folders.mkdirs();
				 } else {
					 System.out.println("Folder Not Created");
					 
				 }
				 
				 System.out.println(folders.getAbsolutePath());
				 
				 
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
	    
	    
	    @SuppressWarnings("deprecation")
		public static String generateJwtToken(PrivateKey privateKey) {
	    	
	    	 Map<String, Object> claims = new HashMap<String, Object>();

	            // put your information into claim
	            claims.put("id", "xxx");
	            claims.put("role", "user");
	            claims.put("created", new Date());

	            String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.RS512, privateKey).compact();
				return token;

		
		}

		//Print structure of JWT
		public static void printStructure(String token, PublicKey publicKey) {
			Jws
	            
	               parseClaimsJws = Jwts.parser().setSigningKey(publicKey)
					.parseClaimsJws(token);

			System.out.println("Header     : " + parseClaimsJws.getHeader());
			System.out.println("Body       : " + parseClaimsJws.getBody());
			System.out.println("Signature  : " + parseClaimsJws.getSignature());
		}

		
		// Add BEGIN and END comments
		private static String convertToPublicKey(String key){
			StringBuilder result = new StringBuilder();
			result.append("-----BEGIN PUBLIC KEY-----\n");
			result.append(key);
			result.append("\n-----END PUBLIC KEY-----");
			return result.toString();
		}

	


}
