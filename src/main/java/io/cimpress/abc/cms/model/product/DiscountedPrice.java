package io.cimpress.abc.cms.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "value", "discount" })
public class DiscountedPrice {
	
	@JsonProperty("value")
	private JsonNode value;
	@JsonProperty("discount")
	private JsonNode discount;

//	@JsonCreator
//	public DiscountedPrice(final MonetaryAmount value, final String discount) {
//		this.value = value;
//		this.discount = discount;
//	}

	@JsonProperty("value")
	public JsonNode getValue() {
		return value;
	}

	@JsonProperty("discount")
	public JsonNode getDiscount() {
		return discount;
	}

	@JsonProperty("value")
	public void setValue(JsonNode value) {
		this.value = value;
	}

	@JsonProperty("discount")
	public void setDiscount(JsonNode discount) {
		this.discount = discount;
	}
}
