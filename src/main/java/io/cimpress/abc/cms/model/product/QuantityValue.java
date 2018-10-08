package io.cimpress.abc.cms.model.product;

public final class QuantityValue implements Quantity {

	private String type;
	private int value;

	public QuantityValue() {
		// TODO Auto-generated constructor stub
	}
	public QuantityValue(String type, int value) {
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
