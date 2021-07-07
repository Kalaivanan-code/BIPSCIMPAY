package com.bornfire.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FrAccount {
	private String SchmType;
	private String AcctName;
	private String AcctNumber;
	
	

	public FrAccount(String schmType, String acctName, String acctNumber) {
		super();
		SchmType = schmType;
		AcctName = acctName;
		AcctNumber = acctNumber;
	}



	public FrAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public String toString() {
		return "FrAccount [SchmType=" + SchmType + ", AcctNumber=" + AcctNumber + "]";
	}

	

	public String getSchmType() {
		return SchmType;
	}



	public void setSchmType(String schmType) {
		SchmType = schmType;
	}



	public String getAcctNumber() {
		return AcctNumber;
	}

	public void setAcctNumber(String acctNumber) {
		AcctNumber = acctNumber;
	}

	public String getAcctName() {
		return AcctName;
	}

	public void setAcctName(String acctName) {
		AcctName = acctName;
	}

	
	
	
}
