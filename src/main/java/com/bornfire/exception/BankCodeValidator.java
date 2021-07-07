package com.bornfire.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.controller.IPSDao;



public class BankCodeValidator implements ConstraintValidator<BankCodeVal, String>{

	@Autowired
	IPSDao ipsDao;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
	 System.out.println(value);
	 
	// System.out.println(ipsDao.invalidBankCode(value));
	 
	 
			if(ipsDao.invalidBankCode(value)){
				return false;
			}
		
		return true;
		
	}

}