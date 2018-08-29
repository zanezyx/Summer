package com.zyx.duomimanager.entity;

import java.io.Serializable;

/**
 * 客户资料实体BEAN
 * @author lenovo
 *
 */

public class Custom implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207900616641380791L;
	/**
	 * 
	 */
	
	public Integer id;
	public String name;
	public String password;
	public String mobile;
	public Integer gender;
	public String realyName;
	public String province;
	public String city;
	public String country;
	public String address;
	public String logo;
	public Integer level;
	public Integer state;
	public String cardId;
	public boolean isLogined;
	public Integer score;
	// Constructors

	private static Custom custom;
	
	public static Custom getInstance()
	{
		if(custom==null)
		{
			custom = new Custom();
			custom.isLogined = false;
		}
		return custom;
	}
	
	
	/** default constructor */
	public Custom() {
	}

	/** minimal constructor */
	public Custom(String name, String mobile) {
		this.name = name;
		this.mobile = mobile;
	}

	/** full constructor */
	public Custom(String name, String password, String mobile, Integer gender,
			String realyName, String province, String city, String country,
			String address, String logo, Integer level, Integer state,
			String cardId) {
		this.name = name;
		this.password = password;
		this.mobile = mobile;
		this.gender = gender;
		this.realyName = realyName;
		this.province = province;
		this.city = city;
		this.country = country;
		this.address = address;
		this.logo = logo;
		this.level = level;
		this.state = state;
		this.cardId = cardId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getRealyName() {
		return this.realyName;
	}

	public void setRealyName(String realyName) {
		this.realyName = realyName;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

}
