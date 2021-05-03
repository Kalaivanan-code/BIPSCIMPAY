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
		}
		XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(dataFormat);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xgc;
	}
}
