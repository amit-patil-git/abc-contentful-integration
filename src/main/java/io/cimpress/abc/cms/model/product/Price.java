package io.cimpress.abc.cms.model.product;

import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

public class Price {
	@Nullable
	private JsonNode channel;

	@Nullable
	private String country;
	
	@Nullable
	private JsonNode custom;
	
	@Nullable
	private JsonNode customerGroup;

	@Nullable
	private DiscountedPrice discounted;

	@Nullable
	private String id;

	@Nullable
	private List<PriceTier> tiers;

	@Nullable
	private ZonedDateTime validFrom;

	@Nullable
	private ZonedDateTime validUntil;

	private JsonNode value;

	@Nullable
	public String getId() {
		return id;
	}

	@Nullable
	public List<PriceTier> getTiers() {
		return tiers;
	}

	@Nullable
	public ZonedDateTime getValidFrom() {
		return validFrom;
	}

	@Nullable
	public ZonedDateTime getValidUntil() {
		return validUntil;
	}

	public JsonNode getValue() {
		return value;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public DiscountedPrice getDiscounted() {
		return discounted;
	}

	public void setDiscounted(DiscountedPrice discounted) {
		this.discounted = discounted;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTiers(List<PriceTier> tiers) {
		this.tiers = tiers;
	}

	public void setValidFrom(ZonedDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public void setValidUntil(ZonedDateTime validUntil) {
		this.validUntil = validUntil;
	}

	public void setValue(JsonNode value) {
		this.value = value;
	}

	public JsonNode getChannel() {
		return channel;
	}

	public void setChannel(JsonNode channel) {
		this.channel = channel;
	}

	public JsonNode getCustom() {
		return custom;
	}

	public void setCustom(JsonNode custom) {
		this.custom = custom;
	}

	public JsonNode getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(JsonNode customerGroup) {
		this.customerGroup = customerGroup;
	}
}
