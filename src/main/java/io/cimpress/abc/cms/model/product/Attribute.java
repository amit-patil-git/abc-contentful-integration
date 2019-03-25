
package io.cimpress.abc.cms.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "name",
    "value",
    "label",
    "required",
    "class",
    "selectable",
    "variable",
    "type"
})
public class Attribute {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("value")
	private AttributeValue value;
	
	@JsonProperty("label")
	private LocalizedString label;
	
    @JsonProperty("required")
    private Boolean isRequired;

    @JsonProperty("class")
    private String attributeClass;

    @JsonProperty("selectable")
    private Boolean isSelectable;

    @JsonProperty("variable")
    private Boolean isVariable;

    @JsonProperty("type")
    private String type;
    
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("value")
    public AttributeValue getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(AttributeValue value) {
        this.value = value;
    }

	public LocalizedString getLabel() {
		return label;
	}

	public void setLabel(LocalizedString label) {
		this.label = label;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getAttributeClass() {
		return attributeClass;
	}

	public void setAttributeClass(String attributeClass) {
		this.attributeClass = attributeClass;
	}

	public Boolean getIsSelectable() {
		return isSelectable;
	}

	public void setIsSelectable(Boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	public Boolean getIsVariable() {
		return isVariable;
	}

	public void setIsVariable(Boolean isVariable) {
		this.isVariable = isVariable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
