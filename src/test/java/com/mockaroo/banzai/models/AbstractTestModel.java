package com.mockaroo.banzai.models;

import com.mockaroo.banzai.constraints.Type;

public class AbstractTestModel 
{
	@Type(value="Phone")
	String telephone;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
}
