package com.mockaroo.banzai.models;

import com.mockaroo.banzai.constraints.CustomList;
import com.mockaroo.banzai.constraints.RegularExpression;
import com.mockaroo.banzai.constraints.Type;
import com.mockaroo.banzai.constraints.Words;

import java.util.Date;

public class Employee 
{
	@RegularExpression(regexp="\\d{7}")
	private Long id;
	
	@Type(value="Full Name")
    private String name;
	
    @RegularExpression(regexp="\\d{6}/\\d{3}-\\d{2}")
    private String inszNumber;
    
    private Date birthDate;
    private Date startEmployment;
    private Date endEmployment;
    
    @CustomList(values={"ARBEIDER", "BEDIENDE"})
    private String regiment;
    
    @Words(min=2, max=2)
    private String function;
    
    private String department;
    
    @Words(min=1, max=4)
    private String preventionProfile;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInszNumber() {
		return inszNumber;
	}
	public void setInszNumber(String inszNumber) {
		this.inszNumber = inszNumber;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getStartEmployment() {
		return startEmployment;
	}
	public void setStartEmployment(Date startEmployment) {
		this.startEmployment = startEmployment;
	}
	public Date getEndEmployment() {
		return endEmployment;
	}
	public void setEndEmployment(Date endEmployment) {
		this.endEmployment = endEmployment;
	}
	public String getRegiment() {
		return regiment;
	}
	public void setRegiment(String regiment) {
		this.regiment = regiment;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPreventionProfile() {
		return preventionProfile;
	}
	public void setPreventionProfile(String preventionProfile) {
		this.preventionProfile = preventionProfile;
	}
}
