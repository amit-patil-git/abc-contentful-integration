package io.cimpress.abc.cms.model.product;

import javax.annotation.Generated;

import com.fasterxml.jackson.databind.JsonNode;

@Generated(value = "io.sphere.sdk.annotations.processors.generators.ResourceValueImplGenerator", comments = "Generated from: io.sphere.sdk.products.PriceTier")
public class PriceTier {
	private Integer minimumQuantity;

	private JsonNode value;

	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public JsonNode getValue() {
		return value;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public void setValue(JsonNode value) {
		this.value = value;
	}
}
