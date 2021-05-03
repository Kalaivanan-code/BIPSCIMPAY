package com.bornfire.entity;

public enum DocType {
	pacs_008_001_08{
		@Override
		public String getDocs() {
			return "pacs.008.001.08";
		}
	},
	
	pacs_002_001_10{
		@Override
		public String getDocs() {
			return "pacs.002.001.10";
		}
	},
	
	admi_002_001_01{
		@Override
		public String getDocs() {
			return "admi.002.001.01";
		}
	},
	
	pain_001_001_09{
		@Override
		public String getDocs() {
			return "pain.001.001.09";
		}
	},
	pain_002_001_10{
		@Override
		public String getDocs() {
			return "pain.002.001.10";
		}
	},
	
	camt_011_001_07{
		@Override
		public String getDocs() {
			return "camt.011.001.07";
		}
	},
	camt_009_001_07{
		@Override
		public String getDocs() {
			return "camt.009.001.07";
		}
	};
	
	public abstract String getDocs();
	
}
