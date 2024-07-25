package com.bornfire.controller;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bornfire.config.Listener;
import com.bornfire.entity.Feestranparition;
import com.bornfire.entity.Feestranpartion;

@Component
public class Documentsettelment {

	
	@Autowired
	Listener listener;
	
	@Autowired
	Feestranpartion Feestran;
	
	
	
	public String createsettlementfile(String id) {
		
		

		 int totalNumbers = 1;
		StringBuilder st = new StringBuilder();
		 for (int i = 1; i <= totalNumbers; i++) {
				st.append("HD");
	            String orderedNumber = String.format("%019d", 1);
	            st.append(orderedNumber);
	            
	            String Bussinessdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
	            st.append(Bussinessdate);
	            String Bussinessdatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	            st.append(Bussinessdatetime);
	            st.append("3.0.1\n");
	        }
	
		 
		 

			
		 	int totalNumbers1 = 10;
		//	StringBuilder st = new StringBuilder();
			String bankcode= "1";
			String accountingentry= "001";
			String accountingbatch = "228135";
			String Seqaccentry = "2335008";
			
			List<Object[]> list = Feestran.feestran(id);
			List<Feestranparition> feestran = new ArrayList<Feestranparition>();
			for (Object[] item : list) {
				Feestranparition Feestranparition = new Feestranparition();
				
				Feestranparition.setMerchant_id((String) item[0]);
				System.out.println((String) item[0]);
				Feestranparition.setTran_amt_loc((BigDecimal) item[1]);
				Feestranparition.setPartition_detail((String) item[2]);
				System.out.println((String) item[2]);
				Feestranparition.setTran_date((Date) item[3]);
				Feestranparition.setIpsx_account_number((String) item[4]);
				Feestranparition.setPart_tran_type((String) item[5]);
				Feestranparition.setTran_ref_cur((String) item[6]);
				Feestranparition.setValue_date((Date) item[7]);
				Feestranparition.setIpsx_acct_name((String) item[8]);
				Feestranparition.setPart_tran_id((BigDecimal) item[10]);
				Feestranparition.setParticipant_bank(((String) item[11]));
				Feestranparition.setSrl_num(((String) item[12]));
				Feestranparition.setBusiness_date((Date) item[13]);
				Feestranparition.setPosting((Date) item[14]);
				
				System.out.println(item[10]);
				feestran.add(Feestranparition);
			}
			
			System.out.println(feestran.size());
			 for (int i = 0; i < feestran.size(); i++) {
					st.append("DT");///Record Type

		            String orderedNumber = String.format("%019d", i+2);
		            st.append(orderedNumber);///Record Sequence
		            
		            st.append(String.format("%06d", 1));//Bank Code
		            st.append(feestran.get(i).getParticipant_bank().toString());///Operation Code
		            st.append(String.format("%03d", Integer.valueOf(feestran.get(i).getPart_tran_id().toString())));//Operation Sequence
		            
		          //Settlement Account Cutoff Id
		            st.append(String.format("%08d", Integer.valueOf(accountingbatch)));/// Need to enter the values from table
		            
		          ///GL Reference 
		            String Sequenceaccentry = String.format("%08d", Integer.valueOf(feestran.get(i).getSrl_num().toString()));
		            String spaces = "";
		            for (int j = 0; j < 32; j++) {	
		                spaces += " ";
		            }
		            st.append(Sequenceaccentry+spaces);/// Need to enter the values from table
		            
		            String Merchantid = feestran.get(i).getMerchant_id();
		            System.out.println(Merchantid);
		            int Merchantlen = Merchantid.length();
		            
		            int spc = 512 - Merchantlen; 
		            String Space2 = "";
		            for(int a=0;a< spc;a++) {
		            	Space2+=" ";
		            }
		            st.append(Merchantid+Space2);///Reference Of Document
		            
		            //Entry Label
		            String Descrp = null ;
		            if(feestran.get(i).getPartition_detail() == null) {
		            	 if(feestran.get(i).getPart_tran_type().equals("C")) {
		            		 Descrp = "INCOMING TRANSACTION";
				            }else {
				            	Descrp = " ";
				            }
		            }else {
		            	Descrp = feestran.get(i).getPartition_detail();
		            }
		            System.out.println(Descrp+"val");
		            int desclen = Descrp.length();
		            int deslen = 80 - desclen;
		            String space3 = "";
		            for(int b=0;b< deslen;b++) {
		            	space3+=" ";
		            }
		            st.append(Descrp+space3);///Entry Label
		           
		           ////Posting Date
		            String julianDate = listener.generatejuliandateSettlement("3",feestran.get(i).getPosting());
		            System.out.println(julianDate+"julianDate");
		            st.append(julianDate);///Entry Label
		            ///Entry Date
					String julianDate1 = listener.generatejuliandateSettlement("3",feestran.get(i).getTran_date());
					st.append(julianDate1);
					System.out.println(julianDate1+"julianDate1");
		            
		            ///Entry Account Number
		            String Navisioncode = feestran.get(i).getIpsx_account_number() == null ? "" : feestran.get(i).getIpsx_account_number();
		            int Navcodlen = Navisioncode.length();
		            int Navcodlenval = 24 - Navcodlen;
		           // System.out.println(Navcodlenval);
		            String spaces4 ="";
		            for(int c = 0; c< Navcodlenval; c++) { 
		            	spaces4+=" ";
		            }
		            st.append(Navisioncode+spaces4);///Entry Account Number
		            
		            ///Key value
		            st.append("1 ");// Need to check and change the values
		            
		            ///Amount of the accounting entry
		            
		            String Totdebamt = feestran.get(i).getTran_amt_loc().toString();
		            System.out.println(Totdebamt+"org amt");
		            double data  = Double.parseDouble(Totdebamt);
		            /*System.out.println(data);
		            int value = (int)data;
		            
		            System.out.println(value);*/
		            Double amount;
					if(Totdebamt != null) {
						amount = data*100;
		            }else {
		            	amount= 0.00;
		            }
					int value =amount.intValue();
					System.out.println(value + "changed amount");
//		            Double amount = Integer.valueOf(feestran.get(i).getTran_amt_loc());
		            String amountaccentry = String.format("%018d", value);
		            st.append(amountaccentry);///Entry Key Account
		            
		            ///Entry Sign
		            String Entrysign;
		            
		            Entrysign = feestran.get(i).getPart_tran_type();
		            
		            if(Entrysign.equals("QR")) {
		            	Entrysign = "C";
		            }
		            System.out.println(Entrysign);
		            st.append(Entrysign);
		            
		            ///ISO Currencycode
		        //    String currcode = feestran.get(i).getTran_ref_cur() == "MUR" ? "480" : feestran.get(i).getTran_ref_cur();
		            String currcode = "480";
		            System.out.println(currcode);
		            st.append(currcode);
		            
		            ///Flag indicating if the accounting entry was posted or not:
		            st.append("N");/// Need to check and change the values
		            
		            ///The Business date used to generate the accounting entry.
					String BussjulianDate = listener.generatejuliandateSettlement("0",feestran.get(i).getBusiness_date());
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
		            String Mastercardnum = "51";
		            st.append(Mastercardnum);
		            
		            //23-digit identification number assigned by the acquirer or the issuer to every transaction.
		            
		            String Identificationnum = "72303513106100315289427";
		            st.append(Identificationnum);
		            
		            ///A 4-digit sequence number assigned to the transaction. IS it for grouping of transaction
		            String grpoftrn = "0002";
		            st.append(grpoftrn);
		            
		            ///Label of the destination account where the accounting entry shall be posted.
		            String Labelofdest = feestran.get(i).getIpsx_acct_name() == null ? "" : feestran.get(i).getIpsx_acct_name();
		            
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
			

			//	StringBuilder st = new StringBuilder();
						st.append("TR"); /// Record Type
						
						///Record Sequence
						int RecSequ = Feestran.countvalues(id);
			            String RecordSequence = String.format("%019d", RecSequ+2);
			            st.append(RecordSequence);
			            
			            ///Record Count
						int Reccount = Feestran.countvalues(id);
			            String RecordCount = String.format("%019d", Reccount);
			            st.append(RecordCount);
			            
			            ///Total debit amount 
			            String Totdebamt = Feestran.sumofdebit(id);
			            
			            double data  = Double.parseDouble(Totdebamt);
			            System.out.println(data);
			            /*int value = (int)data;*/
			            
			           /* System.out.println(value);*/
			            Double Totdebditamt;
						if(Totdebamt != null) {
							Totdebditamt = data*100;
			            }else {
			            	Totdebditamt= 0.00;
			            }
						int value = Totdebditamt.intValue();
			            String Totaldebitamount = String.format("%018d", value);
			            st.append(Totaldebitamount);
			            
			          //  (TransactionMonitor.getBob_account() == null) ? "" : TransactionMonitor.getBob_account()
			            ///Total credit amount
			            String creditval= Feestran.sumofcredit(id);
			            double data1  = Double.parseDouble(creditval);
			            /*int value1 = (int)data1;*/
			            
			            Double Totcreditamt;
						if(creditval != null) {
			            	 Totcreditamt = data1*100;
			            }else {
			            	 Totcreditamt= 0.00;
			            }
						int value1 = Totcreditamt.intValue();
			            String Totalcreditamount = String.format("%018d", value1);
			            st.append(Totalcreditamount);
			       
				 System.out.println(st);
				return st.toString();
			
		
	
	}
}
