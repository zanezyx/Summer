package com.zyx.jshop.entity;


public class Product implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	public static final long serialVersionUID = 5951890482733539379L;
	public Integer id;
	public String code;
	public String name;
	public String description;
	public String metaKeywords;
	public String productType;
	public Integer store;
	public Integer freezeStore;
	public Integer isMarketable;
	public String brand;
	public String logo;
	public String logoUrl;
	public String norm;
	public Integer marketPrice;
	public Integer vipPrice;
	public Integer saleCount;
	public Integer browseCount;
	public Integer attentionCount;
	public String unit;
	public Integer discountPrice;
	public Integer discountBean;
	public Integer imageCount;
	public Integer gender;
	// Constructors

	/** default constructor */
	public Product() {
	}

	/** full constructor */
	public Product(String code, String name, String description,
			String metaKeywords, String productType, Integer store,
			Integer freezeStore, Integer isMarketable, String brand,
			String logo, String logoUrl, String norm, Integer marketPrice,
			Integer vipPrice, Integer beansPrice, Integer saleCount,
			Integer browseCount, Integer attentionCount, String unit,
			Integer discountPrice, Integer discountBean) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.metaKeywords = metaKeywords;
		this.productType = productType;
		this.store = store;
		this.freezeStore = freezeStore;
		this.isMarketable = isMarketable;
		this.brand = brand;
		this.logo = logo;
		this.logoUrl = logoUrl;
		this.norm = norm;
		this.marketPrice = marketPrice;
		this.saleCount = saleCount;
		this.browseCount = browseCount;
		this.attentionCount = attentionCount;
		this.unit = unit;
		this.discountPrice = discountPrice;
		this.discountBean = discountBean;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMetaKeywords() {
		return this.metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getStore() {
		return this.store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public Integer getFreezeStore() {
		return this.freezeStore;
	}

	public void setFreezeStore(Integer freezeStore) {
		this.freezeStore = freezeStore;
	}

	public Integer getIsMarketable() {
		return this.isMarketable;
	}

	public void setIsMarketable(Integer isMarketable) {
		this.isMarketable = isMarketable;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getNorm() {
		return this.norm;
	}

	public void setNorm(String norm) {
		this.norm = norm;
	}

	public double getMarketPrice() {
		return this.marketPrice;
	}

	public void setMarketPrice(Integer marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Integer getVipPrice() {
		return this.vipPrice;
	}

	public void setVipPrice(Integer vipPrice) {
		this.vipPrice = vipPrice;
	}

	public Integer getSaleCount() {
		return this.saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

	public Integer getBrowseCount() {
		return this.browseCount;
	}

	public void setBrowseCount(Integer browseCount) {
		this.browseCount = browseCount;
	}

	public Integer getAttentionCount() {
		return this.attentionCount;
	}

	public void setAttentionCount(Integer attentionCount) {
		this.attentionCount = attentionCount;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getDiscountPrice() {
		return this.discountPrice;
	}

	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getDiscountBean() {
		return this.discountBean;
	}

	public void setDiscountBean(Integer discountBean) {
		this.discountBean = discountBean;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
}