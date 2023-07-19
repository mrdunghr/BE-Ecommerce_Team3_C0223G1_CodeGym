package com.team3.ecommerce.entity.product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team3.ecommerce.entity.IdBasedEntity;

import javax.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity {
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage() {
	}

	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}


	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Transient
	public String getImagePath() {
		return "/product-images/" + product.getId() + "/extras/" + this.name;
		//return Constants.S3_BASE_URI + "/product-images/" + product.getId() + "/extras/" + this.name;
	}
	
}

