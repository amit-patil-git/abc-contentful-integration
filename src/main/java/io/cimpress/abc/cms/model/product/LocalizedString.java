package io.cimpress.abc.cms.model.product;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * A localized string is a object where the keys are {@link Locale}s (HTTP API: ISO language tags),
 * and the values are the corresponding strings used for that language.
 *
 * {@include.example io.sphere.sdk.models.LocalizedStringTest#defaultUseCases()}
 */
public final class LocalizedString {

    @JsonIgnore
    private final Map<String, String> translations;

    @JsonCreator
    private LocalizedString(final Map<String, String> translations) {
        //the Jackson mapper may passes null here and it is not possible to use an immutable map
        this.translations = Optional.ofNullable(translations).orElse(Collections.emptyMap());
    }

    /**
     * Creates an instance without any value.
     *
     * @return instance without any value
     */
    @JsonIgnore
    public static LocalizedString of() {
        return of(emptyMap());
    }

    /**
     * Creates an instance without any value.
     *
     * @return instance without any value
     */
    public static LocalizedString empty() {
        return of();
    }

    /**
     * Creates an instance with one locale translation pair.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#createFromOneValue()}
     *
     * @param locale the locale for the one translation
     * @param value the translation for the specified locale
     * @return translation for one language
     */
    @JsonIgnore
    public static LocalizedString of(final String locale, final String value) {
        requireNonNull(locale);
        requireNonNull(value);
        return of(mapOf(locale, value));
    }

    /**
     * Creates an instance for two different locales.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#createFromTwoValues()}
     *
     * @param locale1 the first locale
     * @param value1 the translation corresponding to {@code locale1}
     * @param locale2 the second locale which differs from {@code locale1}
     * @param value2 the translation corresponding to {@code locale2}
     * @return new instance for two key value pairs
     */
    @JsonIgnore
    public static LocalizedString of(final String locale1, final String value1, final String locale2, final String value2) {
        return of(mapOf(locale1, value1, locale2, value2));
    }

    /**
     * Creates an instance by supplying a map of {@link Locale} and {@link String}. Changes to the map won't affect the instance.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#createByMap()}
     *
     * @param translations the key value pairs for the translation
     * @return a new instance which has the same key value pairs as {@code translation} at creation time
     */
    @JsonIgnore
    public static LocalizedString of(final Map<String, String> translations) {
        requireNonNull(translations);
        return new LocalizedString(translations);
    }

    /**
     * Creates a new {@link LocalizedString} containing the given entries and the new one.
     * It is not allowed to override existing entries.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#createANewLocalizedStringByAddingALocale()}
     *
     * @param locale the additional locale of the new entry
     * @param value the value for the <code>locale</code>
     * @return a LocalizedString containing this data and the from the parameters.
     * @throws IllegalArgumentException if duplicate locales are provided
     */
    public LocalizedString plus(final String locale, final String value) {
        if (translations.containsKey(locale)) {
            throw new IllegalArgumentException(format("Duplicate keys (%s) for map creation.", locale));
        }
        final Map<String, String> newMap = new HashMap<>();
        newMap.putAll(translations);
        newMap.put(locale, value);
        return new LocalizedString(newMap);
    }

    /**
     * Searches the translation for an exact locale and returning the result in an {@link Optional}.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#find()}
     *
     * @param locale the locale which should be searched
     * @return A filled optional with the translation belonging to {@code locale} or an empty optional if the locale is not present.
     */
    @Nonnull
    public Optional<String> find(final String locale) {
        return Optional.ofNullable(get(locale));
    }

    /**
     * Searches the translation for an exact locale by using {@code null} in the case the locale ist not present.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#getByOneLocale()}
     *
     * @param locale the locale which should be searched
     * @return the translation belonging to {@code locale} or null if the locale is not present.
     */
    @Nullable
    public String get(final String locale) {
        return translations.get(locale);
    }


    /**
     * Searches the translation for some exact locales in the order they appear and returning the result in an {@link Optional}.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#findByMultipleLocales()}
     *
     * @param locales the locale which should be searched, the first exact match wins
     * @return A filled optional with the translation belonging to one of the {@code locales} or an empty optional if none of the locales is not present.
     */
    @Nonnull
    public Optional<String> find(final Iterable<String> locales) {
        final Optional<String> firstFoundLocale = toStream(locales).filter(locale -> translations.containsKey(locale)).findFirst();
        return firstFoundLocale.map(foundLocale -> get(foundLocale));
    }

    /**
     * Searches the translation for some exact locales in the order they appear and using null as result if no match could be found.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#getByMultipleLocales()}
     *
     * @param locales the locale which should be searched, the first exact match wins
     * @return the translation belonging to one of the {@code locales} or null if none of the locales is not present.
     */
    @Nullable
    public String get(final Iterable<String> locales) {
        return find(locales).orElse(null);
    }

    /**
     * Searches a translation which matches a locale in {@code locales} and uses language fallbackes.
     * If locales which countries are used then the algorithm searches also for the pure language locale.
     * So if "en_US" could not be found then "en" will be tried.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#getTranslation()}
     *
     * @param locales the locales to try out
     * @return a translation matching one of the locales or null
     */
    @Nullable
    public String getTranslation(final Iterable<String> locales) {
        return StreamSupport.stream(locales.spliterator(), false)
                .map(localeToFind -> {
                    String match = get(localeToFind);
                    if (match == null) {
                        final String pureLanguageLocale = new String(localeToFind);
                        match = get(pureLanguageLocale);
                    }
                    return match;
                })
                .filter(x -> x != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new Stream of entries.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#streamAndCollector()}
     *
     * @return stream of all entries
     */
    public Stream<LocalizedStringEntry> stream() {
        return translations.entrySet().stream().map(entry -> LocalizedStringEntry.of(entry.getKey(), entry.getValue()));
    }

    /**
     * Returns all locales included in this instance.
     *
     * {@include.example io.sphere.sdk.models.LocalizedStringTest#getLocales()}
     * @return locales
     */
    @JsonIgnore
    @Nonnull
    public Set<String> getLocales() {
        return translations.keySet();
    }

    /**
     * Delivers an immutable map of the translation.
     *
     * @return the key-value pairs for the translation
     */
    @JsonAnyGetter//@JsonUnwrap supports not maps, but this construct puts map content on top level
    private Map<String, String> getTranslations() {
        return immutableCopyOf(translations);
    }

    /**
     * Creates a container which contains the full Java type information to deserialize this class from JSON.
     *
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(byte[], TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(String, TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(com.fasterxml.jackson.databind.JsonNode, TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObjectFromResource(String, TypeReference)
     *
     * @return type reference
     */
    public static TypeReference<LocalizedString> typeReference() {
        return new TypeReference<LocalizedString>() {
            @Override
            public String toString() {
                return "TypeReference<LocalizedString>";
            }
        };
    }

    public static LocalizedString ofEnglish(final String translationForEnglish) {
        requireNonNull(translationForEnglish);
        return of("en", translationForEnglish);
    }
    
    
    public static <K, V> Map<K, V> immutableCopyOf(final Map<K, V> map) {
        return Collections.unmodifiableMap(copyOf(map));
    }

    public static <K, V> Map<K, V> copyOf(final Map<K, V> map) {
        final Map<K, V> copy = new LinkedHashMap<>();
        copy.putAll(map);
        return copy;
    }

    public static <K, V> Map<K, V> mapOf(final K key, final V value) {
        final Map<K, V> result = new LinkedHashMap<>();
        result.put(key, value);
        return result;
    }

    public static <K, V> Map<K, V> mapOf(final K key1, final V value1, final K key2, final V value2) {
        if (key1.equals(key2)) {
            throw new IllegalArgumentException(format("Duplicate keys (%s) for map creation.", key1));
        }
        final Map<K, V> result = new LinkedHashMap<>();
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }
    
    public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        return toList(iterable).stream();
    }
    
    public static <T> List<T> toList(final Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (final T item : iterable) {
            list.add(item);
        }
        return list;
    }

}
