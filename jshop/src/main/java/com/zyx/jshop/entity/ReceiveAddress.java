package com.zyx.jshop.entity;

public class ReceiveAddress implements java.io.Serializable {

	private static final long serialVersionUID = -6519806932376754343L;
	public Integer id;
	public Integer customId;
	public String province;
	public String city;
	public String county;
	public String address;
	public String receiveName;
	public String email;
	public String mobile;
	public String telephone;

	// Constructors

	/** default constructor */
	public ReceiveAddress() {
	}

	/** full constructor */
	public ReceiveAddress(Integer customId, String province, String city,
			String country, String address, String receiveName, String email,
			String mobile, String telephone) {
		this.customId = customId;
		this.province = province;
		this.city = city;
		this.county = country;
		this.address = address;
		this.receiveName = receiveName;
		this.email = email;
		this.mobile = mobile;
		this.telephone = telephone;

	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomId() {
		return this.customId;
	}

	public void setCustomId(Integer customId) {
		this.customId = customId;
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
		return this.county;
	}

	public void setCountry(String country) {
		this.county = country;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReceiveName() {
		return this.receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}