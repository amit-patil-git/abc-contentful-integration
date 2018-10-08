package io.cimpress.abc.cms.model.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingMethod {

    @JsonProperty("id")
    private String id;
    @Nullable
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("name")
    private String name;
    @Nullable
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    @Nullable
    @JsonProperty("taxCategory")
    private TaxCategory taxCategory;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("zoneRates")
    private List<ZoneRate> zoneRates = null;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("zones")
    private List<Zone> zones = null;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("isDefault")
    private Boolean isDefault;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("lastModifiedAt")
    private String lastModifiedAt;
    
    @JsonIgnore
    @Nullable
    @JsonProperty("key")
    private String key;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("taxCategory")
    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    @JsonProperty("taxCategory")
    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }

    @JsonProperty("zoneRates")
    public List<ZoneRate> getZoneRates() {
        return zoneRates;
    }

    @JsonProperty("zoneRates")
    public void setZoneRates(List<ZoneRate> zoneRates) {
        this.zoneRates = zoneRates;
    }

    @JsonProperty("isDefault")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @JsonProperty("isDefault")
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("lastModifiedAt")
    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    @JsonProperty("lastModifiedAt")
    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

}
