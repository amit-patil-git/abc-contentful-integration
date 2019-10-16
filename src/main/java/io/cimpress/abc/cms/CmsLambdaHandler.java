package io.cimpress.abc.cms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAField;
import com.contentful.java.cma.model.CMALocale;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cimpress.abc.cms.model.product.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class CmsLambdaHandler implements RequestHandler<S3Event, String> {
	private static final String DEFAULT_COCKPIT_LOCALE = Locale.ENGLISH.toString();
	private AmazonS3 s3;
	private TransferManager transferManager;
	private Map<String, String> ctoolsToContentfulLang = new HashMap<>();

	public CmsLambdaHandler() {
		s3 = AmazonS3ClientBuilder.standard().build();
		transferManager = TransferManagerBuilder.standard().build();
	}

	public CmsLambdaHandler(AmazonS3 s3) {
		this.s3 = s3;
		this.transferManager = TransferManagerBuilder.standard().build();
	}

	@Override
	public String handleRequest(S3Event event, Context context) {
		String bucket, key = null;
		ObjectMapper mapper = new ObjectMapper();
		// Convert JSON to bucket/key
//		context.getLogger().log("JSON: " + json);
		String contentType ="";
		try {
			S3EventNotification.S3EventNotificationRecord s3EventNotificationRecord = event.getRecords().get(0);
			context.getLogger().log("Received event: " + s3EventNotificationRecord.getEventName());
			// "ObjectCreated:*" && ObjectRemoved:*
			bucket = s3EventNotificationRecord.getS3().getBucket().getName();
//        context.getLogger().log("Bucket :"+bucket);
			key = s3EventNotificationRecord.getS3().getObject().getKey();
			context.getLogger().log("key="+key);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		context.getLogger().log("BUCKET: " + bucket);
		context.getLogger().log("KEY: " + key);

//		ObjectMapper mapper = new ObjectMapper();

//		String contentType = "";
		CMAClient client = this.getContentfulClient();
		String defaultLanguage = this.getDefaultLocale(client);
		ctoolsToContentfulLang.put(DEFAULT_COCKPIT_LOCALE, defaultLanguage);
		// map default contentful language to default contentful language
		// needed to properly work with attributes inside variant
		ctoolsToContentfulLang.put(defaultLanguage, defaultLanguage);

		List<CMALocale> alllocale  = allContentfulLocale(client);
		alllocale.forEach( cmaLocale -> {
			if (!ctoolsToContentfulLang.containsKey(cmaLocale.getCode())) {
					ctoolsToContentfulLang.put(cmaLocale.getCode(), cmaLocale.getCode());
			}
		});

		S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
		contentType = response.getObjectMetadata().getContentType();
		try {
			final ProductProjection productResponse = this.getProductResponse(context, mapper, bucket, key);

			// Filter
			List<Attribute> allAttributes = new ArrayList<>();
			productResponse.getAllVariants().forEach(variant -> {
				List<Attribute> preparedAttributes = new ArrayList<>();
				variant.setAssets(null);
				variant.setPrices(null);
				variant.setImages(null);
				variant.getAttributes().stream().filter(attribute -> attribute.isSyncToContentful())
						.collect(Collectors.toList()).forEach(attribute -> {
							final AttributeValue attributeValue = attribute.getValue();
							if (attributeValue instanceof AttributeValueInValue) {
//								AttributeValueInValue preparedValue = (AttributeValueInValue) attributeValue;
//								Set<LocalizedEnumValue> preparedSet = preparedValue.getValue();
//								preparedValue.setValue(preparedSet.stream().map(value -> {
//									return new LocalizedEnumValue(value.getKey(),
//											LocalizedString.of(DEFAULT_COCKPIT_LOCALE, value.getLabel().get(value.getLabel()
//													.getLocales().stream().findAny().orElse(DEFAULT_COCKPIT_LOCALE))));
//								}).collect(Collectors.toSet()));
//								attribute.setValue(preparedValue);
								}
							allAttributes.add(attribute);
							preparedAttributes.add(attribute);
						});
				variant.setAttributes(preparedAttributes);
			});

			// Get all the content types.
			CMAContentType productContentType = client.contentTypes().fetchOne("product");
			CMAContentType attributeContentType = client.contentTypes().fetchOne("attribute");
			CMAContentType attributeValueContentType = client.contentTypes().fetchOne("attributeValue");

			String productKey = productResponse.getKey();
			Pair<CMAEntry, Boolean> pair = getOrCreateCMAEntry(productKey, client);
			CMAEntry productEntry = pair.key;
			boolean isNewProduct = pair.value;

			populateProductEntry(defaultLanguage, productEntry, productResponse, productContentType);

			AttributeParser parser = new AttributeParser(defaultLanguage, productKey, client, attributeContentType,
					attributeValueContentType, ctoolsToContentfulLang, context.getLogger());

			Stream<String> attributeIdStream = allAttributes.stream().map(a -> getAttributeKey(productKey, a));
			List<String> attributeIdList = attributeIdStream.collect(Collectors.toList());

			List<String> attributeEntryIdList = Stream
					.concat(parser.getAttributeEntryIdList(productEntry), attributeIdList.stream()).distinct()
					.filter(attributeIdList::contains).collect(Collectors.toList());

			// Parse and save in contentful only user selectable attributes or when type of
			// attribute is missed
			List<CMAEntry> attributeEntries = attributeEntryIdList.stream()
					.map(id -> allAttributes.stream().filter(a -> getAttributeKey(productKey, a).equals(id))
							.findFirst())
					.filter(Optional::isPresent).map(Optional::get).map(parser::parse).collect(Collectors.toList());

			productEntry.setField("attributes", defaultLanguage, attributeEntries);

			CMAEntry createdEntry;
			if (isNewProduct) {
				createdEntry = client.entries().create(productContentType.getId(), productEntry);
			} else {
				createdEntry = client.entries().update(productEntry);
			}
			client.entries().publish(createdEntry);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return contentType;
	}

	private String getAttributeKey(String productKey, Attribute attribute) {
		return productKey + "_" + attribute.getName();
	}

	private void populateProductEntry(String defaultLanguage, CMAEntry productEntry, ProductProjection productResponse,
			CMAContentType productContentType) {
		for (CMAField cmaField : productContentType.getFields()) {
			switch (cmaField.getId()) {
			case "productName":
				if (productResponse.getName() != null) {
					for (String locale : productResponse.getName().getLocales()) {
						productEntry.setField(cmaField.getId(), ctoolsToContentfulLang.get(locale),
								productResponse.getName().get(locale));
					}
				}
				break;
			case "key":
				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getKey());
				break;
			case "slug":
				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getSlug().get(DEFAULT_COCKPIT_LOCALE));
				break;
			case "variants":
				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getAllVariants());
				break;
			case "productTypeId":
				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getProductType().getId());
				break;

			default:
				break;
			}
		}
	}

	private Pair<CMAEntry, Boolean> getOrCreateCMAEntry(String entryId, CMAClient client) {
		CMAEntry entry;
		boolean isNewEntry = false;

		try {
			entry = client.entries().fetchOne(entryId);
		} catch (Exception e) {
			entry = new CMAEntry();
			entry.setId(entryId);
			isNewEntry = true;
		}

		return new Pair<>(entry, isNewEntry);
	}

	private class Pair<K, V> {
		final K key;
		final V value;

		Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	private String getDefaultLocale(CMAClient client, String defaultLocale) {
		final CMALocale locale = allContentfulLocale(client).stream()
				.filter(currentLocale -> currentLocale.isDefault()).findFirst().orElse(null);
		return locale instanceof CMALocale ? locale.getCode() : defaultLocale;
	}


	private List<CMALocale> allContentfulLocale(CMAClient client) {
		return client.locales().fetchAll().getItems();
	}

	private String getDefaultLocale(CMAClient client) {
		return this.getDefaultLocale(client, System.getenv("DEFAULT_LANGUAGE"));
	}

	private CMAClient getContentfulClient() {
		return this.getContentfulClient(System.getenv("CMA_ACCESS_TOKEN"), System.getenv("SPACE_ID"),
				System.getenv("ENVIRONMENT_ID"));
	}

	private CMAClient getContentfulClient(String cmaAccessToken, String spaceId, String environmentId) {
		// Create the Contentful client.
		return new CMAClient.Builder().setAccessToken(cmaAccessToken).setSpaceId(spaceId)
				.setEnvironmentId(environmentId).build();
	}

	/**
	 * @param context
	 * @param mapper
	 * @param bucket
	 * @param key
	 * @return ProductProjection
	 */
	private ProductProjection getProductResponse(Context context, ObjectMapper mapper, String bucket, String key)
			throws Exception {
		try {
			File downloadFile = null;
			try {
				downloadFile = File.createTempFile(key, ".json");
			} catch (IOException e) {
				e.printStackTrace();
				context.getLogger().log(String.format("Error on creating temporary file for %s.json.", key));
				throw e;
			}

			final GetObjectRequest request = new GetObjectRequest(bucket, key);
			try {
				Download download = transferManager.download(request, downloadFile);
				download.waitForCompletion();
			} catch (Exception e) {
				context.getLogger().log(
						"Sheet download from S3 failed for %s. Please raise a query to support team with the request. = "
								+ key);
			}
			context.getLogger().log("downloadFile.getAbsolutePath() ==" + Paths.get(downloadFile.getAbsolutePath()));
			byte[] readAllBytes = Files.readAllBytes(Paths.get(downloadFile.getAbsolutePath()));
			String currentProjection = new String(readAllBytes);
			context.getLogger().log("JSON: "+currentProjection);
			return mapper.readValue(currentProjection, ProductProjection.class);
		} catch (Exception e) {
			e.printStackTrace();
			context.getLogger().log(String.format("Error getting object %s from bucket %s. Make sure they exist and"
					+ " your bucket is in the same region as this function.", key, bucket));
			throw e;
		}
	}
}
