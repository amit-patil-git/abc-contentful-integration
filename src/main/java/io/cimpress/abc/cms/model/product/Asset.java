
package io.cimpress.abc.cms.model.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "sources",
    "name",
    "description",
    "tags"
})
public class Asset {

    @JsonProperty("id")
    private String id;
    @JsonProperty("sources")
    private List<Source> sources = null;
    @Nullable
    @JsonProperty("name")
    private LocalizedString name;
    @Nullable
    @JsonProperty("description")
    private LocalizedString description;
    @JsonProperty("tags")
    private List<String> tags = null;
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

    @JsonProperty("sources")
    public List<Source> getSources() {
        return sources;
    }

    @JsonProperty("sources")
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @JsonProperty("name")
    public LocalizedString getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(LocalizedString name) {
        this.name = name;
    }

    @JsonProperty("description")
    public LocalizedString getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(LocalizedString description) {
        this.description = description;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
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
