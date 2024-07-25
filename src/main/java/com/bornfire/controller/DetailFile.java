package com.bornfire.controller;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DetailFile {
	
	public static void main(String[] args) {
		
		 int totalNumbers = 10;
			StringBuilder st = new StringBuilder();
			String bankcode= "1";
			String accountingentry= "001";
			String accountingbatch = "228133";
			String Seqaccentry = "20001234";
			 for (int i = 2; i <= totalNumbers; i++) {
					st.append("DT");///Record Type

		            String orderedNumber = String.format("%019d", i);
		            st.append(orderedNumber);///Record Sequence
		            
		            st.append(String.format("%06d", Integer.valueOf(bankcode)));//Bank Code
		            st.append("A00001");///Operation Code
		            st.append(accountingentry);//Operation Sequence
		            st.append(String.format("%08d", Integer.valueOf(accountingbatch)));//Settlement Account Cutoff Id
		             
		            String Sequenceaccentry = String.format("%08d",Integer.valueOf(Seqaccentry)+i );
		            String spaces = "";
		            for (int j = 0; j < 32; j++) {
		                spaces += " ";
		            }
		            st.append(Sequenceaccentry+spaces);///GL Reference
		            String Merchantid = "CIM96107896";
		            String Space2 = "";
		            for(int a=0;a< 512-Merchantid.length();a++) {
		            	Space2+=" ";
		            }
		            st.append(Merchantid+Space2);///Reference Of Document
		            
		            //Entry Label
		            String Descrp = "Sales MC MUR - Non Cim Counters";
		            String space3 = "";
		            for(int b=0;b< 80-Descrp.length();b++) {
		            	space3+=" ";
		            }
		            st.append(Descrp+space3);///Entry Label
		           
		           ////Posting Date
		            String julianDate = GenerateJulianDate();
					st.append(julianDate);
		            ///Entry Date
					String julianDate1 = GenerateJulianDate();
					st.append(julianDate1);
		            
		            ///Entry Account Number
		            String Navisioncode = "273140";
		            String spaces4 ="";
		            for(int c = 0; c< 24-Navisioncode.length(); c++) {
		            	spaces4+=" ";
		            }
		            st.append(Navisioncode+spaces4);///Entry Account Number
		            st.append("1 ");
		            ///Amount of the accounting entry
		            int amount = Integer.valueOf("550");
		            String amountaccentry = String.format("%018d", amount);
		            st.append(amountaccentry);///Entry Key Account
		            
		            ///Entry Sign
		            st.append("C");
		            
		            ///ISO Currencycode
		            String currcode = "480";
		            st.append(currcode);
		            
		            ///Flag indicating if the accounting entry was posted or not:
		            st.append("N");
		            
		            ///The Business date used to generate the accounting entry.
					String BussjulianDate = GenerateJulianDate();
					st.append(BussjulianDate);
		            
		            ///Code of the Card Management System data source from which the transaction originates
		            String CCManagesys = "PMATF";
		            st.append(CCManagesys);
		            
		            ///6 digit code related to the issuing bank for which the accounting entry is generated
		            String issuingbank ="000003";
		            st.append(issuingbank);
		            
		            ///6 digit code related to the acquiring bank for which the accounting entry is generated
		            String acquiringbank = "000001";
		            st.append(acquiringbank);
		            
		            ////2 digit code representing the network from which the transaction originates (Visa, MasterCard, etc.).
		            String Mastercardnum = "02";
		            st.append(Mastercardnum);
		            
		            //23-digit identification number assigned by the acquirer or the issuer to every transaction.
		            
		            
		            String Identificationnum = "1697199068492";
		            st.append(Identificationnum);
		            
		            ///A 4-digit sequence number assigned to the transaction. IS it for grouping of transaction
		            String grpoftrn = "0002";
		            st.append(grpoftrn);
		            
		            ///Label of the destination account where the accounting entry shall be posted.
		            String Labelofdest = "VAT ACCOUNT";
		            
		            if(Labelofdest.length()>32) {
		            	 st.append(Labelofdest.subSequence(0, 32));
		            }else {
		            	int Labellen = Labelofdest.length();
		            	
		            	int val = 32-Labellen;
		            	
		            	String space5 = "";
		            	for(int d=0; d<val;d++) {
		            		space5+=" ";
		            	}
		            	st.append(Labelofdest+space5);
		            }
		            
		            ///Operation Date Label
		            String OperationDateLabel = "BUSINESS DATE";
		            
		            if(OperationDateLabel.length()>32) {
		            	 st.append(OperationDateLabel.subSequence(0, 32));
		            }else {
		            	int Labellen1 = OperationDateLabel.length();
		            	
		            	int val1 = 32-Labellen1;
		            	
		            	String space6 = "";
		            	for(int e=0; e<val1;e++) {
		            		space6+=" ";
		            	}
		            	st.append(OperationDateLabel+space6);
		            }
		            
		            ////Basic Source Label
		            String BasicSourceLabel = "VAT";
		            if(BasicSourceLabel.length()>32) {
		            	 st.append(BasicSourceLabel.subSequence(0, 32));
		            }else {
		            	int Labellen2 = BasicSourceLabel.length();
		            	
		            	int val2 = 32-Labellen2;
		            	
		            	String space7 = "";
		            	for(int f=0; f<val2;f++) {
		            		space7+=" ";
		            	}
		            	st.append(BasicSourceLabel+space7);
		            }
		            
		            ////Operation Grouping Code
		            st.append("GL");
		            
		            ////Conversion Rate
		            st.append("000000000000000000");
		            
		            ///Operation Grouping Criteria
		            String spaces8 = "";
		            for(int g=0; g<512;g++) {
		            	spaces8 += " ";
		            }
		            st.append(spaces8);
		            
		            ////Source Amount
		            st.append(amountaccentry);
		            ////Source Currency
		            st.append(currcode+"\n");
		        }
			 System.out.println(st);
			
		}
		
	// Function to generate the Julian date
    public static String GenerateJulianDate() {
		 // Create a LocalDate for the date you want to convert
            LocalDate newDate = LocalDate.now();
            // Get the year and day of the year
            int year = newDate.getYear();
            int dayOfYear = newDate.getDayOfYear();
            // Format the Julian date with the desired format
            String julianDate = String.format("%d%03d", year, dayOfYear);
			return julianDate;
	 }
	}


