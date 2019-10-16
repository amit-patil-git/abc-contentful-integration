package io.cimpress.abc.cms.model.product;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "sku",
    "prices",
    "images",
    "attributes",
    "assets",
    "quantity",
    "key",
    "isMatchingVariant",
    "identifier"
})
public class ProductVariant {
	@JsonProperty("id")
	private Integer id;
	
	@Nullable
	@JsonProperty("sku")
	private String sku;
	
	@Nullable
	@JsonProperty("prices")
	private List<Price> prices;
	
	@Nullable
	@JsonProperty("images")
	private List<Image> images;
	
	@JsonProperty("attributes")
	private List<Attribute> attributes;
	
	@Nullable
	@JsonProperty("assets")
	private List<Asset> assets;
	
	@JsonProperty("quantity")
	private List<Quantity> quantity;

	@Nullable
	@JsonProperty("key")
	private String key;

	@Nullable
	@JsonProperty("isMatchingVariant")
	private Boolean matchingVariant;

	@Nullable
	@JsonProperty("identifier")
	private String identifier;

	
//	public ProductVariant(@Nullable final List<Asset> assets, final List<Attribute> attributes, final List<Quantity> quantity, final Integer id,
//			@Nullable final String key, @Nullable @JsonProperty("isMatchingVariant") final Boolean matchingVariant,
//			@Nullable final Price price, final List<Price> prices,
//			@Nullable @JsonProperty("scopedPriceDiscounted") final Boolean scopedPriceDiscounted,
//			@Nullable final String sku, @Nullable final List<Image> images) {
//		this.assets = assets;
//		this.attributes = attributes;
//		this.quantity = quantity;
//		this.id = id;
//		this.key = key;
//		this.matchingVariant = matchingVariant;
//		this.price = price;
//		this.prices = prices;
//		this.sku = sku;
//		this.images = images;
//	}
	
	
	
	public ProductVariant() {
		
	}

	@Nullable
	public List<Asset> getAssets() {
		return assets;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public Attribute getAttribute(String name) {
		return getAttributes().stream().filter(a -> a.getName().equals(name)).findAny().orElse(null);
	}

	public Integer getId() {
		return id;
	}

	@Nullable
	public String getKey() {
		return key;
	}

	@Nullable
	@JsonProperty("isMatchingVariant")
	public Boolean isMatchingVariant() {
		return matchingVariant;
	}


	public List<Price> getPrices() {
		return prices;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Nullable
	public String getSku() {
		return sku;
	}

	public Boolean getMatchingVariant() {
		return matchingVariant;
	}

	public void setMatchingVariant(Boolean matchingVariant) {
		this.matchingVariant = matchingVariant;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Quantity> getQuantity() {
		return quantity;
	}

	public void setQuantity(List<Quantity> quantity) {
		this.quantity = quantity;
	}
}
