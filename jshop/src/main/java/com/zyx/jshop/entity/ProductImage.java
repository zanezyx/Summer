package com.zyx.jshop.entity;

public class ProductImage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3158580963384372021L;
	public Integer id;
	public Integer isIndex;
	public Integer productId;
	public String imageName;
	public String imageUrl;
	public String description;

	// Constructors

	public ProductImage() {
	}

	public ProductImage(Integer isIndex, Integer productId, String imageName,
			String imageUrl, String description) {
		this.isIndex = isIndex;
		this.productId = productId;
		this.imageName = imageName;
		this.imageUrl = imageUrl;
		this.description = description;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsIndex() {
		return this.isIndex;
	}

	public void setIsIndex(Integer isIndex) {
		this.isIndex = isIndex;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}