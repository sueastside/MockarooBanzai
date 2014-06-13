package com.mockaroo.banzai.models;

import com.mockaroo.banzai.constraints.Type;

public class NestedTestModel extends AbstractTestModel
{
	@Type(value="Street Name")
	String street;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	
}
