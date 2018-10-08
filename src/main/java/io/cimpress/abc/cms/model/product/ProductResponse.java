package io.cimpress.abc.cms.model.product;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "key", "type", "productProjection", "version" })
public class ProductResponse {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("key")
	private String key;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("productProjection")
	@Nullable
	private ProductProjection productProjection;
	
	@JsonProperty("version")
	private Long version;
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("productProjection")
	public ProductProjection getProductProjection() {
		return productProjection;
	}

	@JsonProperty("productProjection")
	public void setProductProjection(ProductProjection productProjection) {
		this.productProjection = productProjection;
	}

	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}