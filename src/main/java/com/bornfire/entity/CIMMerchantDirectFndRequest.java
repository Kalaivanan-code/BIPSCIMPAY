package com.bornfire.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CIMMerchantDirectFndRequest {
	@NotNull(message="Remitter Account Details Required")
	@Valid
	private CIMDirectMerchantRemitterAccount RemitterAccount;
	
	@NotNull(message="Merchant Account Details Required")
	@Valid
	private CIMDirectMerchantBenAccount MerchantAccount;
	
	
	private CIMAddlDataFieldRequest AdditionalDataInformation;

	public CIMDirectMerchantRemitterAccount getRemitterAccount() {
		return RemitterAccount;
	}

	public void setRemitterAccount(CIMDirectMerchantRemitterAccount remitterAccount) {
		RemitterAccount = remitterAccount;
	}

	public CIMDirectMerchantBenAccount getMerchantAccount() {
		return MerchantAccount;
	}

	public void setMerchantAccount(CIMDirectMerchantBenAccount merchantAccount) {
		MerchantAccount = merchantAccount;
	}

	public CIMAddlDataFieldRequest getAdditionalDataInformation() {
		return AdditionalDataInformation;
	}

	public void setAdditionalDataInformation(CIMAddlDataFieldRequest additionalDataInformation) {
		AdditionalDataInformation = additionalDataInformation;
	}

	
	
	
}
