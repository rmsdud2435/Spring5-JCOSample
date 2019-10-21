package com.spring5.sample.jco.service.model1;

public class OutputModel {
	private String S25TLN;
	private String METC;
	public String getS25TLN() {
		return S25TLN;
	}
	public void setS25TLN(String s25tln) {
		S25TLN = s25tln;
	}
	public String getMETC() {
		return METC;
	}
	public void setMETC(String mETC) {
		METC = mETC;
	}
	@Override
	public String toString() {
		return "OutputModel [S25TLN=" + S25TLN + ", METC=" + METC + "]";
	}
	
}
