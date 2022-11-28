package com.bornfire.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.entity.CIMMerchantQRAddlInfo;
import com.bornfire.entity.CIMMerchantQRcodeAcctInfo;
import com.bornfire.entity.CIMMerchantQRcodeRequest;
import com.bornfire.entity.CimMerchantResponse;
import com.bornfire.entity.MerchantMaster;
import com.bornfire.entity.MerchantMasterRep;
import com.bornfire.entity.MerchantQRRegistration;
import com.bornfire.entity.QRUrlGobalEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@RestController
@Validated
public class IPSQRCODEController {

	
	private static final Logger logger = LoggerFactory.getLogger(IPSQRCODEController.class);
	
	@Autowired
	ErrorResponseCode errorCode;
	
	@Autowired
	IPSConnection ipsConnection;
	
	@Autowired
	IPSDao ipsDao;
	
	@Autowired
	MerchantMasterRep merchantmasterRep;
	
	@Autowired
	Environment env;
	
	@PostMapping(path = "/api/ws/StaticMaucas", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CimMerchantResponse> genMerchantQRcode(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "Merchant_ID", required = true) String acct_num,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2
			)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts generate QR Code");
		//MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);
		CimMerchantResponse response = null;
		MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);
		MerchantQRRegistration merchantQRgenerator = new MerchantQRRegistration();
		String paycode = env.getProperty("ipsx.qr.payeecode");
		String globalUnique = env.getProperty("ipsx.qr.globalUnique");
		String payload = env.getProperty("ipsx.qr.payload");
		String poiMethod_static = env.getProperty("ipsx.qr.poiMethod_static");
		merchantQRgenerator.setPoi_method(poiMethod_static);
		merchantQRgenerator.setPayee_participant_code(paycode);
		merchantQRgenerator.setGlobal_unique_id(globalUnique);
		merchantQRgenerator.setPayload_format_indicator(payload);
		merchantQRgenerator.setMerchant_acct_no(ms.getMerchant_acc_no());
		merchantQRgenerator.setMerchant_id(ms.getMerchant_id());
		merchantQRgenerator.setMerchant_name(ms.getMerchant_name());
		merchantQRgenerator.setMerchant_category_code(ms.getMerchant_cat_code());
		merchantQRgenerator.setTransaction_crncy(ms.getCurr());
		merchantQRgenerator.setTip_or_conv_indicator(ms.getTip_or_conv_indicator());
		merchantQRgenerator.setConv_fees_type(ms.getConv_fees_type());
		merchantQRgenerator.setValue_conv_fees(ms.getValue_conv_fees());
		merchantQRgenerator.setCity(ms.getMerchant_city());
		merchantQRgenerator.setCountry("MU");
		merchantQRgenerator.setZip_code(ms.getPincode());
		merchantQRgenerator.setBill_number(ms.getTr());
		merchantQRgenerator.setMobile(ms.getMerchant_mob_no());
		merchantQRgenerator.setLoyalty_number(ms.getLoyalty_number());
		merchantQRgenerator.setCustomer_label(ms.getCustomer_label());
		merchantQRgenerator.setStore_label(ms.getStore_label());
		merchantQRgenerator.setTerminal_label(ms.getTerminal_label());
		merchantQRgenerator.setReference_label(ms.getReference_label());
		merchantQRgenerator.setPurpose_of_tran(ms.getPurpose_of_tran());
		merchantQRgenerator.setAdditional_details(ms.getAdd_details_req());
		
		merchantQRgenerator.setBill_number(ms.getTr());
		merchantQRgenerator.setCustomer_label(ms.getCustomer_label());
		
		
		CIMMerchantQRcodeRequest  cimMerchantQRcodeRequest=new CIMMerchantQRcodeRequest();
		System.out.println(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPayloadFormatIndiator(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPointOfInitiationFormat(merchantQRgenerator.getPoi_method().toString());
	
		CIMMerchantQRcodeAcctInfo merchantQRAcctInfo=new CIMMerchantQRcodeAcctInfo();
		merchantQRAcctInfo.setGlobalID(merchantQRgenerator.getGlobal_unique_id());
		merchantQRAcctInfo.setPayeeParticipantCode(merchantQRgenerator.getPayee_participant_code());
		merchantQRAcctInfo.setMerchantAcctNumber(merchantQRgenerator.getMerchant_acct_no());
		merchantQRAcctInfo.setMerchantID(merchantQRgenerator.getMerchant_id());
		cimMerchantQRcodeRequest.setMerchantAcctInformation(merchantQRAcctInfo);
		
		cimMerchantQRcodeRequest.setMCC(merchantQRgenerator.getMerchant_category_code().toString());
		cimMerchantQRcodeRequest.setCurrency(merchantQRgenerator.getTransaction_crncy().toString());
		
		
		if(!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("")) {
			cimMerchantQRcodeRequest.setTrAmt(merchantQRgenerator.getTransaction_amt().toString());
		}
		
		
		/*if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("0")){
			cimMerchantQRcodeRequest.setConvenienceIndicator(false);
		}else {
			cimMerchantQRcodeRequest.setConvenienceIndicator(true);
			cimMerchantQRcodeRequest.setConvenienceIndicatorFeeType(merchantQRgenerator.getConv_fees_type().toString());
			cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
		}*/
		
		if(merchantQRgenerator.getTip_or_conv_indicator()!=null) {
			if(!merchantQRgenerator.getTip_or_conv_indicator().toString().equals("")){

				if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("01")) {
					cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("01");
				}else if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("02")) {
					if(merchantQRgenerator.getConv_fees_type().equals("Fixed")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("02");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
					}else if(merchantQRgenerator.getConv_fees_type().equals("Percentage")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("03");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());

					}
				}
				
			}
		}
		
		
		cimMerchantQRcodeRequest.setCountryCode(merchantQRgenerator.getCountry().toString());
		cimMerchantQRcodeRequest.setMerchantName(merchantQRgenerator.getMerchant_name().toString());
		cimMerchantQRcodeRequest.setCity(merchantQRgenerator.getCity().toString());
		
		if(!String.valueOf(merchantQRgenerator.getZip_code()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getZip_code()).equals("")) {
			cimMerchantQRcodeRequest.setPostalCode(merchantQRgenerator.getZip_code().toString());
		}
		
		
		
		CIMMerchantQRAddlInfo cimMercbantQRAddlInfo=new CIMMerchantQRAddlInfo();
		cimMercbantQRAddlInfo.setBillNumber(merchantQRgenerator.getBill_number());
		cimMercbantQRAddlInfo.setMobileNumber(merchantQRgenerator.getMobile());
		cimMercbantQRAddlInfo.setStoreLabel(merchantQRgenerator.getStore_label());
		cimMercbantQRAddlInfo.setLoyaltyNumber(merchantQRgenerator.getLoyalty_number());
		cimMercbantQRAddlInfo.setCustomerLabel(merchantQRgenerator.getCustomer_label());
		cimMercbantQRAddlInfo.setTerminalLabel(merchantQRgenerator.getTerminal_label());
		cimMercbantQRAddlInfo.setReferenceLabel(merchantQRgenerator.getReference_label());
		cimMercbantQRAddlInfo.setPurposeOfTransaction(merchantQRgenerator.getPurpose_of_tran());
		cimMercbantQRAddlInfo.setAddlDataRequest(merchantQRgenerator.getAdditional_details());
		
		cimMerchantQRcodeRequest.setAdditionalDataInformation(cimMercbantQRAddlInfo);
		CimMerchantResponse merchantQRResponse=ipsConnection.createMerchantQRConnection(psuDeviceID, psuIpAddress, psuID,
				 cimMerchantQRcodeRequest, p_id,
				 channelID, acct_num, resvfield2);
		String QrImg;
		String imageAsBase64 = null;
		if(merchantQRResponse.getBase64QR()!=null) {
			QrImg=merchantQRResponse.getBase64QR();
			 imageAsBase64 = "data:image/png;base64,"+QrImg;
			
		}else {
			if(merchantQRResponse.getBase64QR()==null) {
				
				imageAsBase64="Something went wrong at server end";
			}
		}
	
		merchantQRResponse.setBase64QR(imageAsBase64);


		return new  ResponseEntity<>(merchantQRResponse, HttpStatus.OK);
	}

	
	@PostMapping(path = "/api/ws/DynamicMaucas", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CimMerchantResponse> genDynamicMerchantQRcode(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "Merchant_ID", required = true) String acct_num,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestHeader(value = "Transaction_Amt", required = false) String tran_amt,
			@RequestHeader(value = "Mobile_Number", required = false) String mob_num,
			@RequestHeader(value = "Loyality_Number", required = false) String loy_num,
			@RequestHeader(value = "Store_Label", required = false) String sto_label,
			@RequestHeader(value = "Customer_Label", required = false) String cust_label,
			@RequestHeader(value = "Reference_Label", required = false) String ref_label,
			@RequestHeader(value = "Terminal_Label", required = false) String ter_label,
			@RequestHeader(value = "Purpose_Of_Tran", required = false) String pur_tran,
			@RequestHeader(value = "Additonal_Detail", required = false) String add_det,
			@RequestHeader(value = "Bill_Number", required = false) String bill_num			
			)
	
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts generate QR Code");
		//MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);
		CimMerchantResponse response = null;
		MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);
		MerchantQRRegistration merchantQRgenerator = new MerchantQRRegistration();
		String paycode = env.getProperty("ipsx.qr.payeecode");
		String globalUnique = env.getProperty("ipsx.qr.globalUnique");
		String payload = env.getProperty("ipsx.qr.payload");
		String poiMethod_static = env.getProperty("ipsx.qr.poiMethod_dynamic");
		merchantQRgenerator.setPoi_method(poiMethod_static);
		merchantQRgenerator.setPayee_participant_code(paycode);
		merchantQRgenerator.setGlobal_unique_id(globalUnique);
		merchantQRgenerator.setPayload_format_indicator(payload);
		merchantQRgenerator.setMerchant_acct_no(ms.getMerchant_acc_no());
		merchantQRgenerator.setMerchant_id(ms.getMerchant_id());
		merchantQRgenerator.setMerchant_name(ms.getMerchant_name());
		merchantQRgenerator.setMerchant_category_code(ms.getMerchant_cat_code());
		merchantQRgenerator.setTransaction_crncy(ms.getCurr());
		merchantQRgenerator.setTip_or_conv_indicator(ms.getTip_or_conv_indicator());
		merchantQRgenerator.setConv_fees_type(ms.getConv_fees_type());
		merchantQRgenerator.setValue_conv_fees(ms.getValue_conv_fees());
		merchantQRgenerator.setCity(ms.getMerchant_city());
		merchantQRgenerator.setCountry("MU");
		merchantQRgenerator.setZip_code(ms.getPincode());
		merchantQRgenerator.setBill_number(bill_num);
		merchantQRgenerator.setMobile(mob_num);
		merchantQRgenerator.setLoyalty_number(loy_num);
		merchantQRgenerator.setCustomer_label(cust_label);
		merchantQRgenerator.setStore_label(sto_label);
		merchantQRgenerator.setTerminal_label(ter_label);
		merchantQRgenerator.setReference_label(ref_label);
		merchantQRgenerator.setPurpose_of_tran(pur_tran);
		merchantQRgenerator.setAdditional_details(add_det);
		merchantQRgenerator.setCustomer_label(cust_label);
		
		
		CIMMerchantQRcodeRequest  cimMerchantQRcodeRequest=new CIMMerchantQRcodeRequest();
		System.out.println(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPayloadFormatIndiator(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPointOfInitiationFormat(merchantQRgenerator.getPoi_method().toString());
	
		CIMMerchantQRcodeAcctInfo merchantQRAcctInfo=new CIMMerchantQRcodeAcctInfo();
		merchantQRAcctInfo.setGlobalID(merchantQRgenerator.getGlobal_unique_id());
		merchantQRAcctInfo.setPayeeParticipantCode(merchantQRgenerator.getPayee_participant_code());
		merchantQRAcctInfo.setMerchantAcctNumber(merchantQRgenerator.getMerchant_acct_no());
		merchantQRAcctInfo.setMerchantID(merchantQRgenerator.getMerchant_id());
		cimMerchantQRcodeRequest.setMerchantAcctInformation(merchantQRAcctInfo);
		
		cimMerchantQRcodeRequest.setMCC(merchantQRgenerator.getMerchant_category_code().toString());
		cimMerchantQRcodeRequest.setCurrency(merchantQRgenerator.getTransaction_crncy().toString());
		
		
		if(!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("")) {
			cimMerchantQRcodeRequest.setTrAmt(merchantQRgenerator.getTransaction_amt().toString());
		}
		
		
		/*if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("0")){
			cimMerchantQRcodeRequest.setConvenienceIndicator(false);
		}else {
			cimMerchantQRcodeRequest.setConvenienceIndicator(true);
			cimMerchantQRcodeRequest.setConvenienceIndicatorFeeType(merchantQRgenerator.getConv_fees_type().toString());
			cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
		}*/
		
		if(merchantQRgenerator.getTip_or_conv_indicator()!=null) {
			if(!merchantQRgenerator.getTip_or_conv_indicator().toString().equals("")){

				if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("01")) {
					cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("01");
				}else if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("02")) {
					if(merchantQRgenerator.getConv_fees_type().equals("Fixed")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("02");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
					}else if(merchantQRgenerator.getConv_fees_type().equals("Percentage")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("03");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());

					}
				}
				
			}
		}
		
		
		cimMerchantQRcodeRequest.setCountryCode(merchantQRgenerator.getCountry().toString());
		cimMerchantQRcodeRequest.setMerchantName(merchantQRgenerator.getMerchant_name().toString());
		cimMerchantQRcodeRequest.setCity(merchantQRgenerator.getCity().toString());
		
		if(!String.valueOf(merchantQRgenerator.getZip_code()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getZip_code()).equals("")) {
			cimMerchantQRcodeRequest.setPostalCode(merchantQRgenerator.getZip_code().toString());
		}
		
		
		
		CIMMerchantQRAddlInfo cimMercbantQRAddlInfo=new CIMMerchantQRAddlInfo();
		cimMercbantQRAddlInfo.setBillNumber(merchantQRgenerator.getBill_number());
		cimMercbantQRAddlInfo.setMobileNumber(merchantQRgenerator.getMobile());
		cimMercbantQRAddlInfo.setStoreLabel(merchantQRgenerator.getStore_label());
		cimMercbantQRAddlInfo.setLoyaltyNumber(merchantQRgenerator.getLoyalty_number());
		cimMercbantQRAddlInfo.setCustomerLabel(merchantQRgenerator.getCustomer_label());
		cimMercbantQRAddlInfo.setTerminalLabel(merchantQRgenerator.getTerminal_label());
		cimMercbantQRAddlInfo.setReferenceLabel(merchantQRgenerator.getReference_label());
		cimMercbantQRAddlInfo.setPurposeOfTransaction(merchantQRgenerator.getPurpose_of_tran());
		cimMercbantQRAddlInfo.setAddlDataRequest(merchantQRgenerator.getAdditional_details());
		
		cimMerchantQRcodeRequest.setAdditionalDataInformation(cimMercbantQRAddlInfo);
		CimMerchantResponse merchantQRResponse=ipsConnection.createMerchantQRConnection(psuDeviceID, psuIpAddress, psuID,
				 cimMerchantQRcodeRequest, p_id,
				 channelID, acct_num, resvfield2);
		String QrImg;
		String imageAsBase64 = null;
		if(merchantQRResponse.getBase64QR()!=null) {
			QrImg=merchantQRResponse.getBase64QR();
			 imageAsBase64 = "data:image/png;base64,"+QrImg;
			
		}else {
			if(merchantQRResponse.getBase64QR()==null) {
				
				imageAsBase64="Something went wrong at server end";
			}
		}
	
		merchantQRResponse.setBase64QR(imageAsBase64);


		return new  ResponseEntity<>(merchantQRResponse, HttpStatus.OK);
	}
	

	
	@PostMapping(path = "/api/ws/StaticUpi", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CimMerchantResponse> genMerchantUPIQRcode(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "Merchant_ID", required = true) String acct_num,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2
			)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, WriterException {

		logger.info("Service Starts generate QR Code");
		//MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);s
		CimMerchantResponse response = null;
		MerchantMaster ms = merchantmasterRep.findByIdCustom(acct_num);
		
		QRUrlGobalEntity merchantQRgenerator = new QRUrlGobalEntity();
		merchantQRgenerator.setVers(ms.getVersion());
		merchantQRgenerator.setModes(ms.getModes());
		merchantQRgenerator.setPurpose(ms.getPurpose());
		merchantQRgenerator.setOrgid(ms.getOrgid());
		merchantQRgenerator.setTid(ms.getTid());
		merchantQRgenerator.setTr(ms.getTr());
		merchantQRgenerator.setTn(ms.getTn());
		merchantQRgenerator.setPa(ms.getPa());
		merchantQRgenerator.setPn(ms.getMerchant_name());
		merchantQRgenerator.setMc(new BigDecimal(ms.getMerchant_cat_code()));
		merchantQRgenerator.setMid(ms.getMerchant_id());
		merchantQRgenerator.setMsid(ms.getMsid());
		merchantQRgenerator.setMtid(ms.getMtid());
		merchantQRgenerator.setTid(ms.getTid());
		merchantQRgenerator.setCcs("MU");
		merchantQRgenerator.setMtype(ms.getMerchant_type());
		merchantQRgenerator.setMgr("OFFLINE");
		merchantQRgenerator.setMerchant_onboarding("BANK");
		merchantQRgenerator.setMerchant_location(ms.getMerchant_city());
		merchantQRgenerator.setBrand("TEST");
		merchantQRgenerator.setTipsorconv(ms.getTip_or_conv_indicator());
		merchantQRgenerator.setTips_value(ms.getTip_or_conv_indicator());
		merchantQRgenerator.setCov_fee_type(ms.getConv_fees_type());
		merchantQRgenerator.setVal_con_fee(ms.getValue_conv_fees());
		if(ms.getTran_amount() !=null) {
		merchantQRgenerator.setBam(new BigDecimal(ms.getTran_amount()));
		merchantQRgenerator.setAm(new BigDecimal(ms.getTran_amount()));}
		merchantQRgenerator.setCu(ms.getCurr());
		merchantQRgenerator.setInvoiceno(ms.getInvoiceno());
		merchantQRgenerator.setInvoicedate(ms.getInvoicedate());
		merchantQRgenerator.setQrexpire(ms.getQrexpire());
		merchantQRgenerator.setQrmedium(ms.getQrmedium());
//		merchantQRgenerator.setCategorys(categorys);
//		merchantQRgenerator.setUrls(urls);

		
		
		String qrcode = createQrcode(merchantQRgenerator, "");
		
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrcode, BarcodeFormat.QR_CODE, 1500, 2000, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		int matrixHeight = byteMatrix.getHeight();
		BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixHeight);
		graphics.setColor(Color.BLACK);

		BufferedImage logoimage = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/NPCI_UPI_QR.png"));

		logoimage.createGraphics();
		Graphics2D graphi = (Graphics2D) logoimage.getGraphics();
		graphi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixHeight; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		graphi.drawImage(image, 650, 1100, matrixWidth + 300, matrixHeight + 200, null);
		graphi.setFont(new Font("Lucida Calligraphy", Font.BOLD, 250));
		graphi.setColor(Color.BLACK);
		FontMetrics fm = graphi.getFontMetrics();
		String[] displayTextQR= {merchantQRgenerator.getPn(),""};
		Integer width = 350;
		Integer height = 6500;
		int startingYposition = height + 5;
		for (String displayText : displayTextQR) {
			graphi.drawString(displayText, (image.getWidth()) - (fm.stringWidth(displayText)/2),startingYposition);
			//graphi.drawString(displayText, 500,6500);
			startingYposition += 20;
		}
		
		BufferedImage BOB_LOGO = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/CIM_logo.png"));
		graphi.drawImage(BOB_LOGO, 1250, 1905,  500, 500, null);
		
		ImageIO.write(logoimage, "png", output);
		String QrImg = Base64.getEncoder().encodeToString(output.toByteArray());

		String imageAsBase64 = null;
		imageAsBase64 = "data:image/png;base64," + QrImg;

		logger.info("imageAsBase64 :"+imageAsBase64);
		CimMerchantResponse str = new CimMerchantResponse();
		str.setBase64QR(imageAsBase64);


		return new  ResponseEntity<>(str, HttpStatus.OK);
	}

	
	public String createQrcode(QRUrlGobalEntity qrcodeen, String userid) {

		String msg = "";

		String initenturl = "";

		initenturl += "upiGlobal://pay?ver=" + qrcodeen.getVers() + "&mode=" + qrcodeen.getModes() + "&purpose="
				+ qrcodeen.getPurpose();

		if (qrcodeen.getOrgid()!=null) {
			initenturl += "&orgid=" + qrcodeen.getOrgid();
		}
		if (qrcodeen.getTr()!=null) {
			initenturl += "&tr=" + qrcodeen.getTr();
		}
		if (qrcodeen.getTn()!=null) {
			initenturl += "&tn=" + qrcodeen.getTn();
		}if (qrcodeen.getCategorys()!=null) {
			initenturl += "&category=" + qrcodeen.getCategorys();
		}if (qrcodeen.getUrls()!=null) {
			initenturl += "&url=" + qrcodeen.getUrls();
		}
		if (qrcodeen.getPa()!=null) {
			initenturl += "&pa=" + qrcodeen.getPa();
		}
		if (qrcodeen.getPn()!=null) {
			initenturl += "&pn=" + qrcodeen.getPn();
		}
		if (qrcodeen.getMc()!=null) {
			initenturl += "&mc=" + qrcodeen.getMc();
		}
		if (qrcodeen.getMid()!=null) {
			initenturl += "&mid=" + qrcodeen.getMid();
		}
		if (qrcodeen.getMsid()!=null) {
			initenturl += "&msid=" + qrcodeen.getMsid();
		}
		if (qrcodeen.getMtid()!=null) {
			initenturl += "&mtid=" + qrcodeen.getMtid();
		}
		if (qrcodeen.getMtype()!=null) {
			initenturl += "&mType=" + qrcodeen.getMtype();
		}if (qrcodeen.getMgr()!=null) {
			initenturl += "&mGr=" + qrcodeen.getMgr();
		}
		if (qrcodeen.getMerchant_onboarding()!=null) {
			initenturl += "&mOnboarding=" + qrcodeen.getMerchant_onboarding();
		}if (qrcodeen.getMerchant_location()!=null) {
			initenturl += "&mLoc=" + qrcodeen.getMerchant_location();
		}
		if (qrcodeen.getBrand()!=null) {
			initenturl += "&brand=" + qrcodeen.getBrand();
		}if (qrcodeen.getTipsorconv().equals("01")) {
			initenturl += "&enTips=Y";
		}if (qrcodeen.getTipsorconv().equals("02")) {
			if(qrcodeen.getCov_fee_type().equals("Fixed")) {
				initenturl += "&split=CONFEE:"+ qrcodeen.getVal_con_fee();
			}else if(qrcodeen.getCov_fee_type().equals("Percentage")) {
				initenturl += "&split=CONPCT:"+ qrcodeen.getVal_con_fee();
			}
			
		}
		if (!qrcodeen.getModes().equals("15")) {
			initenturl += "&am=" + qrcodeen.getAm();
		}
		if (!qrcodeen.getCcs().equals(null)) {
			initenturl += "&cc=" + qrcodeen.getCcs();
		}
		if (qrcodeen.getCurr()!=null) {
			initenturl += "&cu=" + qrcodeen.getCurr();
		}
		if (qrcodeen.getQrmedium()!=null) {
			initenturl += "&qrMedium=" + qrcodeen.getQrmedium();
		}
		if (qrcodeen.getInvoiceno()!=null) {
			initenturl += "&invoiceNo=" + qrcodeen.getInvoiceno();
		}
		if (qrcodeen.getInvoicedate()!=null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			
			initenturl += "&invoiceDate=" + dateFormat.format(qrcodeen.getInvoicedate());
		}if (qrcodeen.getQrexpire()!=null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			
			initenturl += "&QRexpire=" + dateFormat.format(qrcodeen.getQrexpire());
		}
		
		String without = initenturl.replace(" ", "%20");
		String Qrcode = getsign(without);

		return without;
	}
	
	
	public String getsign(String intentURL) {
		String finalBHIMURL ="";
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
			System.out.println(kpg.getAlgorithm());
			ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1");
			kpg.initialize(ecsp); // ecsp
			KeyPair kyp = kpg.genKeyPair();
			PublicKey pubKey = kyp.getPublic();
			PrivateKey privKey = kyp.getPrivate();
			System.out.println("\n\n");
//Signature with Sha-256
			Signature dsa = Signature.getInstance("SHA256withECDSA");
			dsa.initSign(privKey);
			//String intentURL = "upi://pay?ver=01&mode=05&orgid=700004&tid=TESTGST123456qazqwe12345hs29hnd1qa2&tr=MerRef123&tn=GST%20QR&category=02&url=https://www.test.com&pa=merchant@npci&pn=Test%20Merchant&mc=5411&am=100.00&cu=INR&mid=TST5432&msid=TSTABC123&mtid=TSTABC1234&gstBrkUp=CGST:08.45|SGST:08.45&qrMedium=02&invoiceNo=BillRef123&invoiceDate=2019-06-11T13:21:50+05:30&invoiceName=Dummy%20Customer&QRexpire=2019-06-11T13:21:50+05:30&QRts=2019-06-12T13:21:50+05:30&pinCode=400063&tier=TIER1&gstIn=GSTNUM1234567890";
			System.out.println("\nintentURL:" + intentURL);
			byte[] strByte = intentURL.getBytes("UTF-8");
			dsa.update(strByte);
//Sign with private key
			byte[] realSig = dsa.sign();
			System.out.println("\nrealSig:" + realSig.toString());
			String encodedSign = java.util.Base64.getEncoder().encodeToString(realSig);

//Append with Signed URL
			 finalBHIMURL = intentURL + "&sign=" + encodedSign;
			System.out.println("\nSignature: " + new BigInteger(1, realSig).toString(16));
			System.out.println("\nEncoded Signature:" + encodedSign);
			System.out.println("\nFinal BHIM URL:" + finalBHIMURL);
//Decode the signed URL with base 64
			String[] bhimReceivedURL = finalBHIMURL.split("&sign=");
			byte[] decodedSign = java.util.Base64.getDecoder().decode(bhimReceivedURL[1]);
			System.out.println("\nDecoded Signature: " + new BigInteger(1, decodedSign).toString(16));
//Initialise verification with public key
			dsa.initVerify(pubKey);
//Initialise using plain text bytes dsa.update(bhimReceivedURL[0].getBytes("UTF8"));
//Verify against sign
			boolean verified = dsa.verify(decodedSign);
			System.out.println("\nSignature Verify Result:" + verified);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return finalBHIMURL;
	}
	

}
