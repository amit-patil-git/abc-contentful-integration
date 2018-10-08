package io.cimpress.abc.cms.model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
    @Type(value = QuantityValue.class, name = "value"),
    @Type(value = QuantityRange.class, name = "range")
})
public interface Quantity {

  static Quantity ofRange(String type, int minimum, int maximum, int increment) {
    return new QuantityRange(type, minimum, maximum, increment);
  }

  static Quantity ofValue(String type, int value) {
    return new QuantityValue(type, value);
  }
}
