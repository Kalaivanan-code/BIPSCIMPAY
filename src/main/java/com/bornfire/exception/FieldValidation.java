package com.bornfire.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bornfire.controller.IPSDao;
import com.bornfire.entity.MCCreditTransferRequest;

@Component
public class FieldValidation {

	@Autowired
	IPSDao ipsDao;

	public boolean validField(String psuDeviceID, String psuIpAddress, String psuID, String senderParticipantBIC,
			String participantSOL, MCCreditTransferRequest mcCreditTransferRequest) {

		Boolean valid = false;

		if (psuDeviceID.equals("")) {
			valid = false;
			throw new FieldException("Invalid Device ID");
		} /*else if (psuIpAddress.equals("")) {
			valid = false;
			throw new FieldException("Invalid IP Address");
		} else if (psuIpAddress.equals("")) {
			valid = false;
			throw new FieldException("Invalid National ID");
		} */else if (senderParticipantBIC.equals("")) {
			valid = false;
			throw new FieldException("BIC Required");
		}/* else if (!senderParticipantBIC.equals("012")) {
			valid = false;
			throw new FieldException("Invalid BIC");
		}*/ else if (participantSOL.equals("")) {
			valid = false;
			throw new FieldException("SOL Required");
		} else if (mcCreditTransferRequest.getFrAccount().getAcctName().equals("")) {
			valid = false;
			throw new FieldException("Debtor Account Name Required");
		} else if (mcCreditTransferRequest.getFrAccount().getAcctNumber().equals("")) {
			valid = false;
			throw new FieldException("Debtor Account Number Required");
		} else if (mcCreditTransferRequest.getFrAccount().getAcctNumber().length() != 14) {
			valid = false;
			throw new FieldException("Invalid Debtor Account Number");
		} else if (!participantSOL.equals(mcCreditTransferRequest.getFrAccount().getAcctNumber().substring(0, 4))) {
			valid = false;
			throw new FieldException("Invalid SOL ID");
		} else if (mcCreditTransferRequest.getToAccount().getAcctName().equals("")) {
			valid = false;
			throw new FieldException("Beneficiary Name Required");
		} else if (mcCreditTransferRequest.getToAccount().getAcctNumber().equals("")) {
			valid = false;
			throw new FieldException("Beneficiary Account Number Required");
		} else if (mcCreditTransferRequest.getCurrencyCode().equals("")) {
			valid = false;
			throw new FieldException("Currency Code Required");
		} else if (mcCreditTransferRequest.getTrAmt().equals("")) {
			valid = false;
			throw new FieldException("Transaction Amount Required");
		} else if (mcCreditTransferRequest.getToAccount().getBankCode().equals("")) {
			valid = false;
			throw new FieldException("Bank Code Required");
		} else if (ipsDao.invalidBankCode(mcCreditTransferRequest.getToAccount().getBankCode())) {
			valid = false;
			throw new FieldException("Invalid Bank Code");
		} else {
			valid = true;
		}
		return valid;
	}
}
