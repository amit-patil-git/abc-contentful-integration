package io.cimpress.abc.cms.model.product;

import java.util.Set;

public final class AttributeValueInValue implements AttributeValue {

	private String type;
	private Set<LocalizedEnumValue> value;
	
	public AttributeValueInValue() {
	}
	
	public AttributeValueInValue(String type, Set<LocalizedEnumValue> value) {
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
	public Set<LocalizedEnumValue> getValue() {
		return value;
	}
	public void setValue(Set<LocalizedEnumValue> value) {
		this.value = value;
	}

}
