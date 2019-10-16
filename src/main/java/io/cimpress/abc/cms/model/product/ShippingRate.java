
package io.cimpress.abc.cms.model.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "price",
    "tiers"
})
public class ShippingRate {

    @JsonProperty("price")
    private ShippingPrice price;
    @JsonProperty("tiers")
    private List<Object> tiers = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("price")
    public ShippingPrice getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setShippingPrice(ShippingPrice price) {
        this.price = price;
    }

    @JsonProperty("tiers")
    public List<Object> getTiers() {
        return tiers;
    }

    @JsonProperty("tiers")
    public void setTiers(List<Object> tiers) {
        this.tiers = tiers;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
