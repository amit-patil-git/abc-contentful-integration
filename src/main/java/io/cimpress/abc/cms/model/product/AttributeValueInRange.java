package io.cimpress.abc.cms.model.product;

import java.util.Set;

public final class AttributeValueInRange implements AttributeValue {

	private String type;
	
	private Set<Range> range;
	
	public AttributeValueInRange() {
	}

	public AttributeValueInRange(String type, Set<Range> range) {
		super();
		this.type = type;
		this.range = range;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public Set<Range> getRange() {
		return range;
	}

	public void setRange(Set<Range> range) {
		this.range = range;
	}
	
	public static class Range {
		private float minimum;
		private float maximum;
		private float increment;
		private String description;
		
		public float getMinimum() {
			return minimum;
		}
		
		public void setMinimum(float minimum) {
			this.minimum = minimum;
		}
		
		public float getMaximum() {
			return maximum;
		}
		
		public void setMaximum(float maximum) {
			this.maximum = maximum;
		}
		
		public float getIncrement() {
			return increment;
		}
		
		public void setIncrement(float increment) {
			this.increment = increment;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
	}
}
