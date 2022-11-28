package com.bornfire.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

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
}
