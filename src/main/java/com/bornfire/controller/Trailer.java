package com.bornfire.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Trailer {

	public static void main(String[] args) {
			StringBuilder st = new StringBuilder();
					st.append("TR"); /// Record Type
					
					///Record Sequence
					int RecSequ = Integer.valueOf("16580");
		            String RecordSequence = String.format("%019d", RecSequ);
		            st.append(RecordSequence);
		            
		            ///Record Count
					int Reccount = Integer.valueOf("16580");
		            String RecordCount = String.format("%019d", Reccount);
		            st.append(RecordCount);
		            
		            ///Total debit amount 
		            int Totdebamt = Integer.valueOf("1053695803");
		            String Totaldebitamount = String.format("%018d", Totdebamt);
		            st.append(Totaldebitamount);
		            
		            ///Total credit amount
		            int Totcreditamt = Integer.valueOf("1053695803");
		            String Totalcreditamount = String.format("%018d", Totcreditamt);
		            st.append(Totalcreditamount);
		       
			 System.out.println(st);
		}

	}

