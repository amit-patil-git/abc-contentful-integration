package io.cimpress.abc.cms.model.product;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import io.cimpress.abc.cms.model.product.AttributeValueInRange.Range;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({ 
	@Type(value = AttributeValueInValue.class, name = "value"),
	@Type(value = AttributeValueInRange.class, name = "range") 
})
public interface AttributeValue {

	static AttributeValue ofRange(String type, Set<Range> range) {
		return new AttributeValueInRange(type, range);
	}

	static AttributeValue ofValue(String type, Set<LocalizedEnumValue> value) {
		return new AttributeValueInValue(type, value);
	}
}
