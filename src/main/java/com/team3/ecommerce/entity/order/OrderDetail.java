package com.team3.ecommerce.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.IdBasedEntity;
import com.team3.ecommerce.entity.product.Product;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail extends IdBasedEntity {
	private int quantity;
	private float productCost;
	private float shippingCost;
	private float unitPrice;
	private float subtotal;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;


	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public OrderDetail() {

	}


	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public OrderDetail(int quantity, float productCost, float shippingCost, float unitPrice, float subtotal, OrderStatus status, Customer customer, Product product, Order order) {
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.unitPrice = unitPrice;
		this.subtotal = subtotal;
		this.status = status;
		this.customer = customer;
		this.product = product;
		this.order = order;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "order_id")
	private Order order;

	private boolean hasReviewed = false;

	public boolean isHasReviewed() {
		return hasReviewed;
	}

	public void setHasReviewed(boolean hasReviewed) {
		this.hasReviewed = hasReviewed;
	}

	public OrderDetail(Customer customer) {
		this.customer = customer;
	}

	public OrderDetail(int quantity, float productCost, float shippingCost, float unitPrice, float subtotal, Product product, Order order) {
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.unitPrice = unitPrice;
		this.subtotal = subtotal;
		this.product = product;
		this.order = order;
	}



	public OrderDetail(String categoryName, int quantity, float productCost, float shippingCost, float subtotal, Customer customer) {
		this.customer = customer;
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.productCost = productCost * quantity;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}

	public OrderDetail(int quantity, String productName, float productCost, float shippingCost, float subtotal, Customer customer) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.customer = customer;
		this.productCost = productCost * quantity;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}


}

