package io.cimpress.abc.cms.model.product;

public final class QuantityRange implements Quantity {

	private String type;
	private int minimum;
	private int maximum;
	private int increment;
	
	public QuantityRange() {
	}
	
	public QuantityRange(String type, int minimum, int maximum, int increment) {
		super();
		this.type = type;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

}
