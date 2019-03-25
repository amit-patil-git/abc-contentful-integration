package io.cimpress.abc.cms.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Range {
		private float minimum;
		private float maximum;
		private float increment;
		private String description;
		private String unitOfMeasure;
		
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

		public String getUnitOfMeasure() {
			return unitOfMeasure;
		}

		public void setUnitOfMeasure(String unitOfMeasure) {
			this.unitOfMeasure = unitOfMeasure;
		}
		
	}
}
