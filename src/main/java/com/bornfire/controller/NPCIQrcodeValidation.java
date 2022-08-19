package com.bornfire.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bornfire.entity.QRUrlGlobalEntity;
import com.bornfire.entity.UPIQREntityRep;
import com.bornfire.upiqrcodeentity.Invoice;
import com.bornfire.upiqrcodeentity.Merchant;
import com.bornfire.upiqrcodeentity.MerchantIdentifier;
import com.bornfire.upiqrcodeentity.NpciupiReqcls;
import com.bornfire.upiqrcodeentity.Payee;
import com.bornfire.upiqrcodeentity.RespEntity;
import com.bornfire.upiqrcodeentity.UPIRespEntity;

@Component
public class NPCIQrcodeValidation {

	private static final Logger logger = LoggerFactory.getLogger(NPCIQrcodeValidation.class);

	@Autowired
	UPIQREntityRep upiqrRep;
	
	@Autowired
	ConsentIPSXservice consentIPSXservice;
	

	public UPIRespEntity getreqdet(NpciupiReqcls npcireq, String pid) {

		UPIRespEntity response = new UPIRespEntity();
		String qrcode = npcireq.getQrPayLoad().substring(16);
		QRUrlGlobalEntity qrdet = getQrentityValue(qrcode);
		response.setQrPayLoad(npcireq.getQrPayLoad());
		RespEntity resp = new RespEntity();
		Optional<QRUrlGlobalEntity> qr = upiqrRep.findById(qrdet.getMid());
		if (qr.isPresent()) {
			resp.setResult("SUCCESS");
		} else {
			resp.setResult("FAILURE");
		}

		resp.setReqMsgId(npcireq.getTxn().getID());
		response.setResp(resp);

		Payee pay = new Payee();
		pay.setAddr("HOME");
		pay.setMCC(qrdet.getMc());
		pay.setType("ENTITY");
		Merchant mr = new Merchant();
		MerchantIdentifier id = new MerchantIdentifier();
		id.setSubCode("1111");
		id.setMid(qrdet.getMid());
		id.setSid(qrdet.getMsid());
		id.setTid(qrdet.getTid());
		id.setMerchantType("SMALL");
		id.setMerchantGenre("ONLINE");
		id.setOnBoardingType("BANK");
//	id.setRegId(regId);
		mr.setIdentifier(id);

		response.setPayee(pay);

		Invoice in = new Invoice();
		in.setDate(qrdet.getInvoicedate());
		in.setNum(qrdet.getInvoiceno());
		response.setInvoice(in);
		npcireq.getQrPayLoad().substring(10);
		logger.info(npcireq.getQrPayLoad().substring(16));

		return response;
	}
	
	
	
	public String ValidateQrcode(NpciupiReqcls npcireq,String pid) {
		
		String qrcode = npcireq.getQrPayLoad().substring(16);
		QRUrlGlobalEntity qrdet= getQrentityValue(qrcode);
		String response = "";
		Optional<QRUrlGlobalEntity> qr= upiqrRep.findById(qrdet.getMid());
		if(qr.isPresent()) {
			response="SUCCESS";
			consentIPSXservice.respvalQr(npcireq, pid);
		}else {
			response=("FAILURE");
		}
				
		return response;
	}

	public QRUrlGlobalEntity getQrentityValue(String qrcode) {

		QRUrlGlobalEntity qrdet = new QRUrlGlobalEntity();
		String[] strs = qrcode.split("&");
		System.out.println("Substrings length:" + strs.length);
		for (int i = 0; i < strs.length; i++) {
			logger.info("Str[" + i + "]:" + strs[i] + " " + strs[i].substring(0, 3));

			if (strs[i].substring(0, 3).equals("mod")) {
				String mode = strs[i].substring(5);
				qrdet.setModes(mode);
				logger.info("VERSION" + mode);
			}
			if (strs[i].substring(0, 3).equals("ver")) {
				String mode = strs[i].substring(4);
				qrdet.setVers(mode);
			}
			if (strs[i].substring(0, 3).equals("pur")) {
				String mode = strs[i].substring(8);
				qrdet.setPurpose(mode);
			}
			if (strs[i].substring(0, 3).equals("org")) {
				String mode = strs[i].substring(6);
				qrdet.setOrgid(mode);
			}
			if (strs[i].substring(0, 3).equals("tr=")) {
				String mode = strs[i].substring(3);
				qrdet.setTr(mode);
			}
			if (strs[i].substring(0, 3).equals("tn=")) {
				String mode = strs[i].substring(3);
				qrdet.setTn(mode);
			}
			if (strs[i].substring(0, 3).equals("pa=")) {
				String mode = strs[i].substring(3);
				qrdet.setPa(mode);
			}
			if (strs[i].substring(0, 3).equals("pn=")) {
				String mode = strs[i].substring(3);
				qrdet.setPn(mode);
			}
			if (strs[i].substring(0, 3).equals("mc=")) {
				String mode = strs[i].substring(3);
				qrdet.setMc(mode);
			}
			if (strs[i].substring(0, 3).equals("mid")) {
				String mode = strs[i].substring(4);
				qrdet.setMid(mode);
			}
			if (strs[i].substring(0, 4).equals("msid")) {
				String mode = strs[i].substring(5);
				qrdet.setMsid(mode);
			}
			if (strs[i].substring(0, 4).equals("mtid")) {
				String mode = strs[i].substring(5);
				qrdet.setMtid(mode);
			}
			if (strs[i].substring(0, 3).equals("cc=")) {
				String mode = strs[i].substring(3);
				qrdet.setCcs(mode);
			}
			if (strs[i].substring(0, 3).equals("cu=")) {
				String mode = strs[i].substring(3);
				qrdet.setCu(mode);
			}
			if (strs[i].substring(0, 3).equals("qrM")) {
				String mode = strs[i].substring(9);
				qrdet.setQrmedium(mode);
			}
			if (strs[i].substring(0, 3).equals("inv")) {
				if (strs[i].substring(0, 9).equals("invoiceNo")) {
					String mode = strs[i].substring(10);
					qrdet.setInvoiceno(mode);
				}
				if (strs[i].substring(0, 11).equals("invoiceDate")) {
					String mode = strs[i].substring(12);
					qrdet.setInvoicedate(mode);
				}
			}
			if (strs[i].substring(0, 4).equals("QRex")) {
				String mode = strs[i].substring(9);
				qrdet.setQrexpire(mode);
			}
			if (strs[i].substring(0, 4).equals("tier")) {
				String mode = strs[i].substring(5);
				qrdet.setTiers(mode);
			}
		}

		return qrdet;
	}
}
