package io.cimpress.abc.cms.model.product;

import java.util.List;

public class QuantityType {

	private String type;
	private List<Quantity> value;

	public QuantityType() {
	}

	public QuantityType(String type, List<Quantity> value) {
		super();
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Quantity> getValue() {
		return value;
	}

	public void setValue(List<Quantity> value) {
		this.value = value;
	}
}