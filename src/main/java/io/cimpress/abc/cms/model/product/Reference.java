package io.cimpress.abc.cms.model.product;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Reference<T> {

	private String typeId;
    private String id;
    @JsonIgnore
    @Nullable
    private String key;

//    @JsonCreator
//    public Reference(final String typeId, final String id, @Nullable final T obj) {
//        this.id = id;
//        this.typeId = typeId;
//        this.obj = obj;
//    }

	public String getTypeId() {
		return typeId;
	}

	public String getId() {
		return id;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}