package com.bornfire.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bornfire.entity.QRUrlGlobalEntity;
import com.bornfire.entity.UPIQREntityRep;
import com.bornfire.entity.UPI_REQ_QRCODE;
import com.bornfire.entity.UPI_REQ_REP;
import com.bornfire.upiqrcodeentity.BaseCurr;
import com.bornfire.upiqrcodeentity.Invoice;
import com.bornfire.upiqrcodeentity.Merchant;
import com.bornfire.upiqrcodeentity.MerchantIdentifier;
import com.bornfire.upiqrcodeentity.MerchantName;
import com.bornfire.upiqrcodeentity.NpciupiReqcls;
import com.bornfire.upiqrcodeentity.NpciupiReqtransaction;
import com.bornfire.upiqrcodeentity.Payee;
import com.bornfire.upiqrcodeentity.RespEntity;
import com.bornfire.upiqrcodeentity.UPIRespEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NPCIQrcodeValidation {

	private static final Logger logger = LoggerFactory.getLogger(NPCIQrcodeValidation.class);

	@Autowired
	UPIQREntityRep upiqrRep;
	
	@Autowired
	ConsentIPSXservice consentIPSXservice;
	
	@Autowired
	UPI_REQ_REP uPI_REQ_REP;
	
	@Autowired
	NPCIQRIPSXservice npciIPSXservice;
	

	
	public UPIRespEntity getreqdet(NpciupiReqcls npcireq, String pid) throws ParseException {

		UPIRespEntity response = new UPIRespEntity();
		String qrcode = npcireq.getQrPayLoad().substring(16);
		QRUrlGlobalEntity qrdet = getQrentityValue(qrcode);
		response.setQrPayLoad(npcireq.getQrPayLoad());
		String valQr = validateQr(qrcode);
		RespEntity resp = new RespEntity();
		Optional<QRUrlGlobalEntity> qr = upiqrRep.findById(qrdet.getMid());
		logger.info("DEL Value"+qr.get().getDel_flg()+valQr);
if (qr.isPresent()) {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			Date dat;
			
			dat = dateFormat.parse(qrdet.getQrexpire());
			Character del='Y';
			if(dat.compareTo(new Date()) >0) {
				if(qrdet.getMtid().equals(qr.get().getMtid())  && !qr.get().getDel_flg().equals('Y') && valQr.equals("Success")) {
					resp.setResult("SUCCESS");
				}else if(qr.get().getDel_flg().equals(del)){
					logger.info("DEL FAILURE");
					resp.setResult("FAILURE");
					resp.setErr("SD");
				}else if(valQr.equals("Failure")) {
					resp.setResult("FAILURE");
					resp.setErr("RD");
				}
					else {
					resp.setResult("FAILURE");
					resp.setErr("XW");
				}
				
				
			}else {
				resp.setResult("FAILURE");
				resp.setErr("PE");
			}
		} else {
			resp.setResult("FAILURE");
			
		}

//		resp.setReqMsgId(npcireq.getTxn().getID());
		resp.setReqMsgId(pid);
		response.setResp(resp);
		NpciupiReqtransaction TRAN = new NpciupiReqtransaction();
		TRAN.setCustRef(npcireq.getTxn().getCustRef());
		TRAN.setID(npcireq.getTxn().getID());
		TRAN.setNote(npcireq.getTxn().getNote());
		TRAN.setRefId(npcireq.getTxn().getRefId());
		TRAN.setRefUrl(npcireq.getTxn().getRefUrl());
		TRAN.setTs(npcireq.getTxn().getTs());
		response.setTxn(TRAN);
		Payee pay = new Payee();
		pay.setAddr(qr.get().getPa());
		pay.setMCC(qrdet.getMc());
		pay.setType("ENTITY");
		pay.setName(qr.get().getPn());
		pay.setSeqNum("1");
		Merchant mr = new Merchant();
MerchantName mn = new MerchantName();
		
		mn.setBrand(qr.get().getBrand());
		mn.setFranchise(qr.get().getBrand());
		mn.setLegal("01");
		
		mr.setName(mn);
		MerchantIdentifier id = new MerchantIdentifier();
		id.setSubCode("1111");
		id.setMid(qrdet.getMid());
		id.setSid(qrdet.getMsid());
		id.setTid(qrdet.getTid());
		id.setMerchantType(qrdet.getMtype());
		id.setMerchantGenre(qrdet.getMgr());
		id.setOnBoardingType(qrdet.getMerchant_onboarding());
		id.setMerchantLoc(qrdet.getMerchant_location());
		id.setMerchantInstCode("merchantLoc");
		id.setTier(qr.get().getTiers());
		id.setPinCode(new BigDecimal(qr.get().getPincode()));
		id.setRegId(qr.get().getMid());
		mr.setIdentifier(id);
		
		mr.setOwnership("PRIVATE");
		
		pay.setMerchant(mr);
		response.setPayee(pay);

		SimpleDateFormat st = new SimpleDateFormat("YYYY-MM-dd");
		String dat = st.format(new Date());
		Invoice in = new Invoice();
		in.setDate(st.format(qrdet.getInvoicedate()));
//		in.setDate(dat.toString());
		in.setNum(qr.get().getInvoiceno());
		in.setName(qr.get().getPn());
		
		/*
		 * in.setCreditBIC("BARBMUM0"); in.setCreditAccount("90310190013109");
		 */
		 List<BaseCurr> bascurv =  new ArrayList<>();
		 BaseCurr bs = new BaseCurr();
		 bs.setBaseCurr("MUR");
		 bascurv.add(0,bs);
		 in.setFxList(bascurv);
		response.setInvoice(in);
		
		npcireq.getQrPayLoad().substring(10);
		logger.info(npcireq.getQrPayLoad().substring(16));

		return response;
	}
	
	
	
	
	
public String ValidateQrcode(NpciupiReqcls npcireq,String pid) throws ParseException {
		
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(npcireq.toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("reqvalqr Request->"+jsonString);
		String qrcode = npcireq.getQrPayLoad().substring(16);
		QRUrlGlobalEntity qrdet= getQrentityValue(qrcode);
		
		String valQr = validateQr(qrcode);
		String response = "";
		Optional<QRUrlGlobalEntity> qr= upiqrRep.findById(qrdet.getMid());
		
		if (qr.isPresent()) {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			Date dat;
			
			dat = dateFormat.parse(qrdet.getQrexpire());
			
				
				
			if(dat.compareTo(new Date()) >0) {
				if(qrdet.getMtid().equals(qr.get().getMtid()) && !qr.get().getDel_flg().equals('Y') && valQr.equals("Success")) {
					response = "SUCCESS";
					npciIPSXservice.respvalQr(npcireq, pid);
				}else if(qr.get().getDel_flg().equals('Y')) {
					response = "CUSTOMER_NOT_ACTIVE";
					npciIPSXservice.respvalQr(npcireq, pid);
				}else if(valQr.equals("Failure")) {
					response = "INVALID_QRCODE";
					npciIPSXservice.respvalQr(npcireq, pid);
				}
				else {
					response = "TERMINAL_MISSMATCH";
					npciIPSXservice.respvalQr(npcireq, pid);
				}
				
			}else {
				response = "EXPIRED";
				npciIPSXservice.respvalQr(npcireq, pid);
			}
		} else {
			response = "FAILURE";
			npciIPSXservice.respvalQr(npcireq, pid);
		}

UPI_REQ_QRCODE qrreq = new UPI_REQ_QRCODE();
		
		qrreq.setCustRef(npcireq.getTxn().getCustRef());
		qrreq.setIds(npcireq.getTxn().getID());
		qrreq.setNote(npcireq.getTxn().getNote());
		qrreq.setQrPayLoad(npcireq.getQrPayLoad());
		qrreq.setRefId(npcireq.getTxn().getRefId());
		qrreq.setRefUrl(npcireq.getTxn().getRefUrl());
		qrreq.setReq_id(pid);
		qrreq.setTs(new Date());
		qrreq.setResponse(response);
		uPI_REQ_REP.save(qrreq);
				
		return response;
	}

public String  validateQr(String qrcode) {

String resp="";
//upiGlobal://pay?ver=01&mode=01&purpose=11&orgid=1800450001&tr=TR261020220008&
//pa=CIM0400704.CFSLMUM0@bombob&pn=CIM_FIN_MERCHANT_CLOSE&mc=9399&mid=CIM0400704&msid=46578765432676541212&mtid=123123&mType=SMALL&mGr=OFFLINE&mOnboarding=BANK&mLoc=PORT%20LOUIS&brand=TEST&cc=MU&cu=MUR&
//qrMedium=04&QRexpire=2022-12-12T00:00:00+04:00

if(qrcode.indexOf("ver")!=-1 ||qrcode.indexOf("mode")!=-1 ||qrcode.indexOf("orgid")!=-1 ||
qrcode.indexOf("tr")!=-1 ||qrcode.indexOf("pa=")!=-1 ||qrcode.indexOf("pn=")!=-1 ||qrcode.indexOf("mc=")!=-1 ||
qrcode.indexOf("mid")!=-1 ||qrcode.indexOf("msid")!=-1 ||qrcode.indexOf("mtid")!=-1 ||qrcode.indexOf("mType")!=-1 ||
qrcode.indexOf("mGr")!=-1 ||qrcode.indexOf("mOnboarding")!=-1 ||qrcode.indexOf("mLoc")!=-1 ||qrcode.indexOf("brand")!=-1
||qrcode.indexOf("cc=")!=-1 ||qrcode.indexOf("cu=")!=-1 ||qrcode.indexOf("QRexpire")!=-1) {
	resp="Success";
}else {
	resp="Failure";
}
		
//		if(qrdet.getVers().equals("")||qrdet.getModes().equals("")||qrdet.getPurpose().equals("")||qrdet.getOrgid().equals("")||qrdet.getTr().equals("")||
//				qrdet.getPa().equals("")||qrdet.getPn().equals("")||qrdet.getMc().equals("")||qrdet.getMid().equals("")||qrdet.getMsid().equals("")||qrdet.getMtid().equals("")||
//				qrdet.getMtype().equals("")||qrdet.getMgr().equals("")||qrdet.getMerchant_onboarding().equals("")||qrdet.getMerchant_location().equals("")||
//				qrdet.getBrand().equals("")||qrdet.getCcs().equals("")||qrdet.getCu().equals("")||qrdet.getQrmedium().equals("")||qrdet.getQrexpire().equals("")) {
//			resp="Failure";
//		}else {
//			resp="Success";
//		}


	
	return resp;
}

	public QRUrlGlobalEntity getQrentityValue(String qrcode) throws ParseException {

		QRUrlGlobalEntity qrdet = new QRUrlGlobalEntity();
		String[] strs = qrcode.split("&");
		System.out.println("Substrings length:" + strs.length);
		for (int i = 0; i < strs.length; i++) {
			logger.info("Str[" + i + "]:" + strs[i] + " " + strs[i].substring(0, 3));
			qrdet.setVers("01");
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
			}if (strs[i].substring(0, 4).equals("sid=")) {
				String mode = strs[i].substring(4);
				qrdet.setMsid(mode);
			}
			
if (strs[i].substring(0, 4).equals("tid=")) {
				String mode = strs[i].substring(4);
				qrdet.setTid(mode);
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
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
					Date dat;
					qrdet.setInvoicedate(dateFormat.parse(mode));
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
			if (strs[i].substring(0, 4).equals("mTyp")) {
				String mode = strs[i].substring(6);
				qrdet.setMtype(mode);
			}
			if (strs[i].substring(0, 3).equals("mGr")) {
				String mode = strs[i].substring(4);
				qrdet.setMgr(mode);
			}if (strs[i].substring(0, 4).equals("mOnb")) {
				String mode = strs[i].substring(12);
				qrdet.setMerchant_onboarding(mode);
			}if (strs[i].substring(0, 4).equals("mLoc")) {
				String mode = strs[i].substring(5);
				qrdet.setMerchant_location(mode);
			}if (strs[i].substring(0, 4).equals("bran")) {
				String mode = strs[i].substring(6);
				qrdet.setBrand(mode);
			}if (strs[i].substring(0, 4).equals("enTip")) {
				String mode = strs[i].substring(7);
				qrdet.setEntips(mode);
			}
		}

		return qrdet;
	}
}


/*CREATE TABLE "IPS"."BIPS_UPI_REQUEST" 
(	"QRPAYLOAD" VARCHAR2(4000 BYTE), 
	"NOTE" VARCHAR2(100 BYTE), 
	"REFID" VARCHAR2(100 BYTE), 
	"REFURL" VARCHAR2(100 BYTE), 
	"TS" DATE, 
	"CUSTREF" VARCHAR2(100 BYTE), 
	"IDS" VARCHAR2(100 BYTE), 
	"REQ_ID" VARCHAR2(100 BYTE), 
	"RESPONSE" VARCHAR2(20 BYTE)
) SEGMENT CREATION IMMEDIATE 
PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
NOCOMPRESS LOGGING
STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
TABLESPACE "USERS" ;
*/