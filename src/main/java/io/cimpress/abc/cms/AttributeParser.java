package io.cimpress.abc.cms;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.CollectionUtils;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMAEntry;
import io.cimpress.abc.cms.model.product.Attribute;
import io.cimpress.abc.cms.model.product.AttributeValue;
import io.cimpress.abc.cms.model.product.AttributeValueInValue;
import io.cimpress.abc.cms.model.product.LocalizedEnumValue;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttributeParser {
    private final String defaultLanguage;
    private final String productKey;
    private final CMAClient client;
    private final CMAContentType attributeContentType;
    private final CMAContentType attributeValueContentType;
    private final Map<String, String> ctoolsToContentfulLang;
    private final LambdaLogger logger;
    private static final String DEFAULT_COCKPIT_LOCALE = Locale.ENGLISH.toString();

    AttributeParser(String defaultLanguage, String productKey, CMAClient client, CMAContentType attributeContentType,
                    CMAContentType attributeValueContentType, Map<String, String> ctoolsToContentfulLang, LambdaLogger logger) {
        this.defaultLanguage = defaultLanguage;
        this.productKey = productKey;
        this.client = client;
        this.attributeContentType = attributeContentType;
        this.attributeValueContentType = attributeValueContentType;
        this.ctoolsToContentfulLang = ctoolsToContentfulLang;
        this.logger = logger;
    }

    CMAEntry parse(Attribute attribute) {
        CMAEntry attributeEntry;
        boolean isNewAttribute = false;
        try {
            attributeEntry = client.entries().fetchOne(productKey + "_" + attribute.getName());
        } catch (Exception e) {
            attributeEntry = new CMAEntry();
            isNewAttribute = true;
            attributeEntry.setId(productKey + "_" + attribute.getName());
        }
        attributeEntry.setField("pimAttributeName", defaultLanguage,
                attribute.getName());

        attributeEntry.setField("attributeLabel", defaultLanguage,
                attribute.getLabel().get(DEFAULT_COCKPIT_LOCALE));

        for (String locale : attribute.getLabel().getLocales()) {
            Object attributeAliasEntry = attributeEntry.getField("attributeAlias", locale);
            if (attributeAliasEntry == null || "".equals(attributeAliasEntry.toString().trim())) {
                attributeEntry.setField("attributeAlias", ctoolsToContentfulLang.get(locale),
                        attribute.getLabel().get(locale));
            }
        }

        attributeEntry.setField("hidden", defaultLanguage, false);

        Stream<String> entryAttributeIds = getAttributeValueEntryIdList(attributeEntry);

        List<CMAEntry> valuesEntries = Collections.emptyList();
        AttributeValue attributeValue = attribute.getValue();
        if (attributeValue != null) {
            if (attributeValue instanceof AttributeValueInValue) {
                AttributeValueInValue inValue = (AttributeValueInValue) attributeValue;
                valuesEntries = parseValueInValue(attribute, entryAttributeIds, inValue);
            }
        }

        attributeEntry.setField("attributeValue", defaultLanguage, valuesEntries);

        CMAEntry createdAttrEntry;
        if (isNewAttribute) {
            createdAttrEntry = client.entries().create(attributeContentType.getId(), attributeEntry);
        } else {
            createdAttrEntry = client.entries().update(attributeEntry);
        }
        client.entries().publish(createdAttrEntry);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }

        return attributeEntry;
    }

    private List<CMAEntry> parseValueInValue(Attribute attribute, Stream<String> entryValueIds,
                                             AttributeValueInValue inValue) {
        List<CMAEntry> valuesEntries = new ArrayList<>();
        if (entryValueIds == null) {
            entryValueIds = Stream.empty();
        }

        if (!CollectionUtils.isNullOrEmpty(inValue.getValue())) {
            Stream<String> availableValueIds = inValue.getValue().stream()
                    .map(v -> getAttributeValueKey(productKey, v));

            Map<String, LocalizedEnumValue> availableValues = inValue.getValue().stream()
                    .collect(Collectors.toMap(k -> getAttributeValueKey(productKey, k), Function.identity()));

            Collection<String> availableValueIdList = availableValueIds.collect(Collectors.toList());

            List<String> attributeValueIds = Stream.concat(entryValueIds, availableValueIdList.stream()).distinct()
                    .filter(availableValueIdList::contains).collect(Collectors.toList());

            for (String attrValId : attributeValueIds) {
                LocalizedEnumValue localizedEnumValue = availableValues.get(attrValId);

                CMAEntry attributeValueEntry;
                boolean isNewAttributeValue = true;
                try {
                    attributeValueEntry = client.entries().fetchOne(attrValId);
                    isNewAttributeValue = false;
                } catch (Exception e) {
                    attributeValueEntry = new CMAEntry();
                    attributeValueEntry.setId(attrValId);
                }

                attributeValueEntry.setField("attributeKey", ctoolsToContentfulLang.get(defaultLanguage),
                        localizedEnumValue.getKey());

                attributeValueEntry.setField("pimAttributeValue", ctoolsToContentfulLang.get(defaultLanguage),
                        localizedEnumValue.getLabel().get(DEFAULT_COCKPIT_LOCALE));

                for (String locale : localizedEnumValue.getLabel().getLocales()) {
                    Object attributeValueAliasEntry = attributeValueEntry.getField("attributeValueAlias", locale);
                    if (attributeValueAliasEntry == null || "".equals(attributeValueAliasEntry.toString().trim())) {
                        attributeValueEntry.setField("attributeValueAlias", ctoolsToContentfulLang.get(locale),
                                localizedEnumValue.getLabel().get(locale));
                    }
                }

                attributeValueEntry.setField("hidden", defaultLanguage, false);

                CMAEntry createdAttrValEntry;
                try {
                    if (isNewAttributeValue) {
                        createdAttrValEntry = client.entries().create(attributeValueContentType.getId(),
                                attributeValueEntry);
                    } else {
                        createdAttrValEntry = client.entries().update(attributeValueEntry);
                    }
                    client.entries().publish(createdAttrValEntry);
                    Thread.sleep(100);
                    valuesEntries.add(createdAttrValEntry);
                } catch (Exception e) {
                    logger.log("Failed to save attribute value with id: " + attrValId);
                    logger.log(e.getMessage());
                    e.printStackTrace();
                    // Ignore failures, attribute value id invalid
                }
            }
        }

        return valuesEntries;
    }

    private String getAttributeValueKey(String productKey, LocalizedEnumValue enumValue) {
        return productKey + "_" + enumValue.getKey().replaceAll("[^a-zA-Z0-9_-]", "");
    }

    public Stream<String> getAttributeEntryIdList(CMAEntry product) {
        return getEntrySubfieldIds("attributes", product);
    }

    private Stream<String> getAttributeValueEntryIdList(CMAEntry attribute) {
        return getEntrySubfieldIds("attributeValue", attribute);
    }

    private Stream<String> getEntrySubfieldIds(String fieldName, CMAEntry entry) {
        List<Map<String, Map<String, String>>> subFields = entry.getField(fieldName, defaultLanguage);

        if (subFields == null) {
            return Stream.empty();
        }

        return subFields.stream().map(Map::values).flatMap(Collection::stream)
                .filter(item -> item.get("type").equals("Link")).map(item -> item.get("id")).distinct();
    }
}
