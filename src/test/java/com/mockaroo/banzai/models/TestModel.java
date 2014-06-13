package com.mockaroo.banzai.models;
import com.mockaroo.banzai.constraints.RegularExpression;
import com.mockaroo.banzai.constraints.Type;

import java.util.Date;

public class TestModel 
{
	@RegularExpression(regexp="\\d{7}")
	Integer number;
	
	@Type(value="Full Name")
	String name;
	
	@RegularExpression(regexp="\\d{6}/\\d{3}-\\d{2}")
	String INZ;
	
	Date born;
	
	@Type(value="Language")
	String language;
	
	Integer nr;
	
	NestedTestModel nestedTestModel = new NestedTestModel();

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getINZ() {
		return INZ;
	}

	public void setINZ(String iNZ) {
		INZ = iNZ;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getNr() {
		return nr;
	}

	public void setNr(Integer nr) {
		this.nr = nr;
	}

	public NestedTestModel getNestedTestModel() {
		return nestedTestModel;
	}

	public void setNestedTestModel(NestedTestModel nestedTestModel) {
		this.nestedTestModel = nestedTestModel;
	}
	
	
}
