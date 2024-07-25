package com.bornfire.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

public class HeaderFile {

	public static void main(String[] args) {
		 int totalNumbers = 1;
		StringBuilder st = new StringBuilder();
		 for (int i = 1; i <= totalNumbers; i++) {
				st.append("HD");
	            String orderedNumber = String.format("%019d", i);
	            st.append(orderedNumber);
	            
	            String Bussinessdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
	            st.append(Bussinessdate);
	            String Bussinessdatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	            st.append(Bussinessdatetime);
	            st.append("3.0.1\n");
	        }
		 System.out.println(st);
	}

}
