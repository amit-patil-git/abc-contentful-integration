package io.cimpress.abc.cms.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "version", "productType", "name", "description", "metaTitle", "metaDescription", "metaKeywords",
	"categories", "slug", "masterVariant", "variants", "shippingMethods", "key", "isRuleBased", "createdAt", "lastModifiedAt" })
public class ProductProjection {

	@JsonProperty("id")
	private String id;
	@JsonProperty("version")
	private Long version;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("lastModifiedAt")
	private String lastModifiedAt;
	@JsonProperty("productType")
	private Reference<ProductType> productType;
	private LocalizedString name;
	@JsonProperty("categories")
	private Set<Reference<Category>> categories;
	@Nullable
	private LocalizedString description;
	private LocalizedString slug;
	@Nullable
	private LocalizedString metaTitle;
	@Nullable
	private LocalizedString metaDescription;
	@Nullable
	private LocalizedString metaKeywords;
	private ProductVariant masterVariant;
	private List<ProductVariant> variants;
	@Nullable
	private String key;

	private boolean ruleBased;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)

	@JsonProperty("shippingMethods")
	private List<ShippingMethod> shippingMethods;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(String lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Reference<ProductType> getProductType() {
		return productType;
	}

	public void setProductType(Reference<ProductType> productType) {
		this.productType = productType;
	}

	public LocalizedString getName() {
		return name;
	}

	public void setName(LocalizedString name) {
		this.name = name;
	}

	public Set<Reference<Category>> getCategories() {
		return categories;
	}

	public void setCategories(Set<Reference<Category>> categories) {
		this.categories = categories;
	}

	public LocalizedString getDescription() {
		return description;
	}

	public void setDescription(LocalizedString description) {
		this.description = description;
	}

	public LocalizedString getSlug() {
		return slug;
	}

	public void setSlug(LocalizedString slug) {
		this.slug = slug;
	}

	public LocalizedString getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(LocalizedString metaTitle) {
		this.metaTitle = metaTitle;
	}

	public LocalizedString getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(LocalizedString metaDescription) {
		this.metaDescription = metaDescription;
	}

	public LocalizedString getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(LocalizedString metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public ProductVariant getMasterVariant() {
		return masterVariant;
	}

	public void setMasterVariant(ProductVariant masterVariant) {
		this.masterVariant = masterVariant;
	}

	public List<ProductVariant> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariant> variants) {
		this.variants = variants;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(List<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public boolean isRuleBased() {
		return ruleBased;
	}

	public void setRuleBased(boolean ruleBased) {
		this.ruleBased = ruleBased;
	}

	public List<ProductVariant> getAllVariants() {
		final List<ProductVariant> nonMasterVariants = getVariants();
		int size = 0;
		if (nonMasterVariants != null) {
			size = nonMasterVariants.size();
		}
		final List<ProductVariant> result = new ArrayList<>(1 + size);
		result.add(getMasterVariant());
		if (nonMasterVariants != null) {
			result.addAll(nonMasterVariants);
		}
		return result;
	}

}