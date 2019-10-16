package io.cimpress.abc.cms.model.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Nullable
public class ZoneRate {

    @JsonProperty("zone")
    private Zone zone;
    @JsonProperty("shippingRates")
    private List<ShippingRate> shippingRates = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("zone")
    public Zone getZone() {
        return zone;
    }

    @JsonProperty("zone")
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @JsonProperty("shippingRates")
    public List<ShippingRate> getShippingRates() {
        return shippingRates;
    }

    @JsonProperty("shippingRates")
    public void setShippingRates(List<ShippingRate> shippingRates) {
        this.shippingRates = shippingRates;
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
