package io.cimpress.abc.cms.model.product;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;


public class Image {
	
	private String url;
    private JsonNode dimensions;
    
    @Nullable
    private String label;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public JsonNode getDimensions() {
		return dimensions;
	}
	public void setDimensions(JsonNode dimensions) {
		this.dimensions = dimensions;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
