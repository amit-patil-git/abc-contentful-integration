package io.cimpress.abc.cms;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.CollectionUtils;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAField;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cimpress.abc.cms.model.product.Attribute;
import io.cimpress.abc.cms.model.product.AttributeValue;
import io.cimpress.abc.cms.model.product.AttributeValueInValue;
import io.cimpress.abc.cms.model.product.LocalizedEnumValue;
import io.cimpress.abc.cms.model.product.ProductProjection;

public class CmsLambdaHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3;
	private TransferManager transferManager;
	private Map<String, String> ctoolsToContentfulLang = new HashMap<>();;
    
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
        context.getLogger().log("Received event: " + event);
        
        ctoolsToContentfulLang.put("en", "en");
        ctoolsToContentfulLang.put("fr", "fr");
        ctoolsToContentfulLang.put("nl-nl", "nl-NL");
        
        ObjectMapper mapper = new ObjectMapper();
        String cmaAccessToken = System.getenv("CMA_ACCESS_TOKEN");
		String spaceId = System.getenv("SPACE_ID");
		String environmentId = System.getenv("ENVIRONMENT_ID");
		String defaultLanguage = System.getenv("DEFAULT_LANGUAGE");
		
        // Get the object from the event and show its content type
        S3EventNotificationRecord s3EventNotificationRecord = event.getRecords().get(0);
        context.getLogger().log("Received event: " + s3EventNotificationRecord.getEventName());
        // "ObjectCreated:*" && ObjectRemoved:*
		String bucket = s3EventNotificationRecord.getS3().getBucket().getName();
//        context.getLogger().log("Bucket :"+bucket);
        String key = s3EventNotificationRecord.getS3().getObject().getKey();
        context.getLogger().log("key="+key);
        String contentType ="";
		try {
        	File downloadFile = File.createTempFile(key, ".json");
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            final GetObjectRequest request = new GetObjectRequest(bucket, key);
    		try {
    			Download download = transferManager.download(request, downloadFile);
    			download.waitForCompletion();
    		} catch (Exception e) {
    			context.getLogger().log("Sheet download from S3 failed for %s. Please raise a query to support team with the request. = "+key);
    		}
    		context.getLogger().log("downloadFile.getAbsolutePath() ==" +Paths.get(downloadFile.getAbsolutePath()));
    		byte[] readAllBytes = Files.readAllBytes(Paths.get(downloadFile.getAbsolutePath()));
    		String currentProjection = new String(readAllBytes);
    		ProductProjection productResponse = mapper.readValue(currentProjection,	ProductProjection.class);
//			context.getLogger().log("Id ="+ productResponse.getId());
//            context.getLogger().log("S3Object ="+response);
            contentType = response.getObjectMetadata().getContentType();
//            context.getLogger().log("CONTENT TYPE: " + contentType);
            
            // Filter
    		List<Attribute> allAttributes = new ArrayList<>();
    		productResponse.getAllVariants().forEach(variant -> { 
    			variant.setAssets(null);
    			variant.setPrices(null);
    			variant.setImages(null);
    			variant.getAttributes().forEach(attribute -> allAttributes.add(attribute));
    		});
    		// Create the Contentful client.
    		final CMAClient client = new CMAClient.Builder()
    				.setAccessToken(cmaAccessToken)
    				.setSpaceId(spaceId).setEnvironmentId(environmentId).build();
    		// Get all the content types.
    		CMAContentType productContentType = client.contentTypes().fetchOne("product");
    		CMAEntry productEntry = new CMAEntry();
    		
    		try {
    			productEntry = client.entries().fetchOne(productResponse.getKey());
    			if (productEntry.isPublished()) { 
    				client.entries().unPublish(productEntry);
    			}
    			client.entries().delete(productEntry);
    		} catch (Exception e) {
    			productEntry = new CMAEntry();
    		}
    		
    		productEntry.setId(productResponse.getKey());
    		for (CMAField cmaField : productContentType.getFields()) {
    			switch (cmaField.getId()) {
    			case "productName":
    				if (productResponse.getName() != null) {
    					for(String locale : productResponse.getName().getLocales()) {
    						productEntry.setField(cmaField.getId(), ctoolsToContentfulLang.get(locale), productResponse.getName().get(locale));
    					}
    				}
    				break;
    			case "key":
    				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getKey());
    				break;
    			case "slug":
    				if (productResponse.getSlug() != null) {
    					for(String locale : productResponse.getSlug().getLocales()) {
    						productEntry.setField(cmaField.getId(), ctoolsToContentfulLang.get(locale), productResponse.getSlug().get(locale));
    					}
    				}
    				break;
    			case "variants":
    				productEntry.setField(cmaField.getId(), defaultLanguage, productResponse.getAllVariants());
    				break;
    			case "shippingMethods":
    				List<String> shippingMethods = new ArrayList<>();
    				productResponse.getShippingMethods().forEach(shippingM -> shippingMethods.add(shippingM.getName()));
    				productEntry.setField(cmaField.getId(), defaultLanguage, shippingMethods.toArray());
    				break;

    			default:
    				break;
    			}
    		}

    		final CMAEntry createdEntry = client.entries().create(productContentType.getId(), productEntry);
    		client.entries().publish(createdEntry);
    		
    		CMAContentType attributeContentType = client.contentTypes().fetchOne("attribute");
    		CMAContentType attributeValueContentType = client.contentTypes().fetchOne("attributeValue");
    		
    		allAttributes.forEach(attribute -> {
    			List<CMAEntry> valuesEntries = new ArrayList<>();
    			AttributeValue attributeValue = attribute.getValue();
    			if (attributeValue != null) {
    				if (attributeValue instanceof AttributeValueInValue) {
    					AttributeValueInValue inValue = (AttributeValueInValue) attributeValue;
    					if (!CollectionUtils.isNullOrEmpty(inValue.getValue())) {
    						for (LocalizedEnumValue localizedEnumValue : inValue.getValue()) {
    							CMAEntry attributeValueEntry = new CMAEntry();
    							String attrValId = attribute.getName()+ "-" +localizedEnumValue.getLabel().get(defaultLanguage).replaceAll("[^a-zA-Z0-9]", "");
    							CMAEntry fetchOne = null;
    							try {
    								fetchOne = client.entries().fetchOne(attrValId);
    							} catch (Exception e) {
    							}
    							if (null != fetchOne) {
    								continue;
    							}
    						
    							attributeValueEntry.setId(attrValId);
    							for(String locale : localizedEnumValue.getLabel().getLocales()) {
    								attributeValueEntry.setField("pimAttributeValue", ctoolsToContentfulLang.get(locale), localizedEnumValue.getLabel().get(locale));
    							}
    							attributeValueEntry.setField("hidden", defaultLanguage, false);
    							final CMAEntry createdAttrValEntry = client.entries().create(attributeValueContentType.getId(), attributeValueEntry);
    							client.entries().publish(createdAttrValEntry);
    							valuesEntries.add(createdAttrValEntry);
    						}
    					}
    				}
    			}
    			CMAEntry attributeEntry = new CMAEntry();
    			CMAEntry fetchOne = null;
    			try {
    				fetchOne = client.entries().fetchOne(attribute.getName());
    			} catch (Exception e) {
    			}
    			if (null == fetchOne) {
    				attributeEntry.setId(attribute.getName());
    				for (String locale : attribute.getLabel().getLocales()) {
    					attributeEntry.setField("pimAttributeName", ctoolsToContentfulLang.get(locale), attribute.getLabel().get(locale));
    				}
    				attributeEntry.setField("hidden", defaultLanguage, false);
    				attributeEntry.setField("attributeValue", defaultLanguage, valuesEntries);
    				final CMAEntry createdAttrEntry = client.entries().create(attributeContentType.getId(), attributeEntry);
    				client.entries().publish(createdAttrEntry);
    			}
    		});
            
        } catch (Exception e) {
        	e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
        }
        return contentType;
    }
}