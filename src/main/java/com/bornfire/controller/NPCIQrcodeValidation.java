package com.bornfire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bornfire.upiqrcodeentity.QRUrlGlobalEntity;

@Component
public class NPCIQrcodeValidation {

	private static final Logger logger = LoggerFactory.getLogger(NPCIQrcodeValidation.class);

	
	public QRUrlGlobalEntity getQrentityValue(String qrcode) {
		
		QRUrlGlobalEntity qrdet = new QRUrlGlobalEntity();
		 String[] strs = qrcode.split("&");
	        System.out.println("Substrings length:" + strs.length);
	        for (int i = 0; i < strs.length; i++) {
	        	logger.info("Str[" + i + "]:" + strs[i]+" "+strs[i].substring(0,3));
	        	
	        	if(strs[i].substring(0,3).equals("mod")) {
	        		String mode = strs[i].substring(5);
	        		qrdet.setModes(mode);
	        		logger.info("VERSION" + mode);
	        	}
	        	if(strs[i].substring(0,3).equals("ver")) {
	        		String mode = strs[i].substring(4);
	        		qrdet.setVers(mode);
	        	}if(strs[i].substring(0,3).equals("pur")) {
	        		String mode = strs[i].substring(8);
	        		qrdet.setPurpose(mode);
	        	}if(strs[i].substring(0,3).equals("org")) {
	        		String mode = strs[i].substring(6);
	        		qrdet.setOrgid(mode);
	        	}if(strs[i].substring(0,3).equals("tr=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setTr(mode);
	        	}if(strs[i].substring(0,3).equals("tn=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setTn(mode);
	        	}if(strs[i].substring(0,3).equals("pa=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setPa(mode);
	        	}if(strs[i].substring(0,3).equals("pn=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setPn(mode);
	        	}if(strs[i].substring(0,3).equals("mc=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setMc(mode);
	        	}if(strs[i].substring(0,3).equals("mid")) {
	        		String mode = strs[i].substring(4);
	        		qrdet.setMid(mode);
	        	}if(strs[i].substring(0,4).equals("msid")) {
	        		String mode = strs[i].substring(5);
	        		qrdet.setMsid(mode);
	        	}if(strs[i].substring(0,4).equals("mtid")) {
	        		String mode = strs[i].substring(5);
	        		qrdet.setMtid(mode);
	        	}if(strs[i].substring(0,3).equals("cc=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setCcs(mode);
	        	}if(strs[i].substring(0,3).equals("cu=")) {
	        		String mode = strs[i].substring(3);
	        		qrdet.setCu(mode);
	        	}if(strs[i].substring(0,3).equals("qrM")) {
	        		String mode = strs[i].substring(9);
	        		qrdet.setQrmedium(mode);
	        	}if(strs[i].substring(0,3).equals("inv")) {
	        		if(strs[i].substring(0,9).equals("invoiceNo")) {
	        		String mode = strs[i].substring(10);
	        		qrdet.setInvoiceno(mode);
	        		}if(strs[i].substring(0,11).equals("invoiceDate")) {
		        		String mode = strs[i].substring(12);
		        		qrdet.setInvoicedate(mode);
		        		}
	        	}if(strs[i].substring(0,4).equals("QRex")) {
	        		String mode = strs[i].substring(9);
	        		qrdet.setQrexpire(mode);
	        	}if(strs[i].substring(0,4).equals("tier")) {
	        		String mode = strs[i].substring(5);
	        		qrdet.setTiers(mode);
	        	}
	        }
		
		return qrdet;
	}
}
