package com.zyx.jshop.entity;


public class Wallet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	public static final long serialVersionUID = 224540715634759758L;
	public Integer id;
	public Integer customId;
	public Integer totalMoney;
	public Integer consumptionMoney;
	public Integer availableMoney;
	public Integer lockingMoney;
	public Integer beans;

	
	private static Wallet wallet;
	// Constructors

	/** default constructor */
	public Wallet() {
	}

	
	/** full constructor */
	public Wallet(Integer customId, Integer totalMoney, Integer consumptionMoney,
			Integer availableMoney, Integer lockingMoney, Integer beans) {
		this.customId = customId;
		this.totalMoney = totalMoney;
		this.consumptionMoney = consumptionMoney;
		this.availableMoney = availableMoney;
		this.lockingMoney = lockingMoney;
		this.beans = beans;
	}

	
	public static Wallet getInstance()
	{
		if(wallet==null)
		{
			wallet = new Wallet();
			wallet.beans=0;
		}
			
		return wallet;
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

	public Integer getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getConsumptionMoney() {
		return this.consumptionMoney;
	}

	public void setConsumptionMoney(Integer consumptionMoney) {
		this.consumptionMoney = consumptionMoney;
	}

	public Integer getAvailableMoney() {
		return this.availableMoney;
	}

	public void setAvailableMoney(Integer availableMoney) {
		this.availableMoney = availableMoney;
	}

	public Integer getLockingMoney() {
		return this.lockingMoney;
	}

	public void setLockingMoney(Integer lockingMoney) {
		this.lockingMoney = lockingMoney;
	}

	public Integer getBeans() {
		return this.beans;
	}

	public void setBeans(Integer beans) {
		this.beans = beans;
	}

}