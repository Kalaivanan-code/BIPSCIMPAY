package com.bornfire.entity;

public class Dynamic_UPI_request {

	
	private String Transaction_note;
	private String Mode;
	private String Transaction_amt;
	private String Transaction_reference;
	private String Terminal_label;
	public String getTransaction_note() {
		return Transaction_note;
	}
	public void setTransaction_note(String transaction_note) {
		Transaction_note = transaction_note;
	}
	public String getMode() {
		return Mode;
	}
	public void setMode(String mode) {
		Mode = mode;
	}
	public String getTransaction_amt() {
		return Transaction_amt;
	}
	public void setTransaction_amt(String transaction_amt) {
		Transaction_amt = transaction_amt;
	}
	public String getTransaction_reference() {
		return Transaction_reference;
	}
	public void setTransaction_reference(String transaction_reference) {
		Transaction_reference = transaction_reference;
	}
	public String getTerminal_label() {
		return Terminal_label;
	}
	public void setTerminal_label(String terminal_label) {
		Terminal_label = terminal_label;
	}
	@Override
	public String toString() {
		return "Dynamic_UPI_request [Transaction_note=" + Transaction_note + ", Mode=" + Mode + ", Transaction_amt="
				+ Transaction_amt + ", Transaction_reference=" + Transaction_reference + ", Terminal_label="
				+ Terminal_label + "]";
	}
	public Dynamic_UPI_request(String transaction_note, String mode, String transaction_amt,
			String transaction_reference, String terminal_label) {
		super();
		Transaction_note = transaction_note;
		Mode = mode;
		Transaction_amt = transaction_amt;
		Transaction_reference = transaction_reference;
		Terminal_label = terminal_label;
	}
	
	
}
