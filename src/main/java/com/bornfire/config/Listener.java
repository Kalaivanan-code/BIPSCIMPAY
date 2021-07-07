package com.bornfire.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class Listener {

	public XMLGregorianCalendar getxmlGregorianCalender(String type) {
		String dataFormat = null;
		switch(type) {
		case "0":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
			break;
		case "1":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			break;
		case "2":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(new Date());
			break;
		}
	
	

	XMLGregorianCalendar xgc = null;
	try
	{
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
	}catch(
	DatatypeConfigurationException e)
	{
		e.printStackTrace();
	}return xgc;
}
	
	
	public XMLGregorianCalendar convertDateToGreDate(Date date,String type) {
		String dataFormat = null;
		switch(type) {
		case "0":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
			break;
		case "1":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd").format(date);
			break;
		case "2":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(date);
			break;
		}
	
	

	XMLGregorianCalendar xgc = null;
	try
	{
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
	}catch(
	DatatypeConfigurationException e)
	{
		e.printStackTrace();
	}return xgc;
}
	
}
