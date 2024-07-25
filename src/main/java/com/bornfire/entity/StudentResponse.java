package com.bornfire.entity;



public class StudentResponse {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StudentResponse(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return "StudentResponse [name=" + name + "]";
	}

	public StudentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}