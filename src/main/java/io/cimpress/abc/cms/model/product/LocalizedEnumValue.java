package io.cimpress.abc.cms.model.product;

public class LocalizedEnumValue {
	private String key;
	private LocalizedString label;

	public LocalizedEnumValue() {
	}

	public LocalizedEnumValue(String key, LocalizedString label) {
		this.label = label;
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public LocalizedString getLabel() {
		return this.label;
	}
}