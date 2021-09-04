package com.randallraboy.archaius2.yaml;

import com.google.common.collect.Maps;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.ConfigListener;
import com.netflix.archaius.api.Decoder;
import com.netflix.archaius.api.StrInterpolator;
import com.netflix.archaius.config.MapConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * <p>
 * Implementation of {@link Config} that can read of a YAML configuration.
 * </p>
 * <p>
 * Some implementation specific based on how to translate YAML configuration into Archaius model:
 * </p>
 * <ol>
 *     <li>Any primitives(int, string, long, float) will be represented as {@link String} value</li>
 *     <li>"null" or any form of YAML's null will be java null</li>
 *     <li>
 *         A YAML List(e.g [1,2,3], or a list of objects) will be represented as is, that is in a YAML list compact form.
 *         Example:
 *         <pre>
 *             # YAML form
 *             configs:
 *              - test1:
 *                  name: test1
 *              - test2:
 *                  name: test2
 *
 *             # when retrieving via Config.get('configs') will return:
 *                 [test1: { name: test1 }, test2: { name: test2 }
 *         </pre>
 *     </li>
 * </ol>
 *
 * @see #fromInputStream(String, InputStream)
 * @see #fromReader(String, Reader)
 */
public class YamlConfig implements Config {
    private final Config mapConfig;

    private YamlConfig(final String configName, final Map<String, String> flattenedMap) {
        this.mapConfig = new MapConfig(configName, flattenedMap);
    }

    @Override
    public void addListener(ConfigListener listener) {
        mapConfig.addListener(listener);
    }

    @Override
    public void removeListener(ConfigListener listener) {
        mapConfig.removeListener(listener);
    }

    @Override
    public Object getRawProperty(String key) {
        return mapConfig.getRawProperty(key);
    }

    @Override
    public Optional<Object> getProperty(String key) {
        return mapConfig.getProperty(key);
    }

    @Override
    public Long getLong(String key) {
        return mapConfig.getLong(key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return mapConfig.getLong(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return mapConfig.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return mapConfig.getString(key, defaultValue);
    }

    @Override
    public Double getDouble(String key) {
        return mapConfig.getDouble(key);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return mapConfig.getDouble(key, defaultValue);
    }

    @Override
    public Integer getInteger(String key) {
        return mapConfig.getInteger(key);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return mapConfig.getInteger(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return mapConfig.getBoolean(key);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return mapConfig.getBoolean(key, defaultValue);
    }

    @Override
    public Short getShort(String key) {
        return mapConfig.getShort(key);
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return mapConfig.getShort(key, defaultValue);
    }

    @Override
    public BigInteger getBigInteger(String key) {
        return mapConfig.getBigInteger(key);
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return mapConfig.getBigInteger(key, defaultValue);
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        return mapConfig.getBigDecimal(key);
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return mapConfig.getBigDecimal(key, defaultValue);
    }

    @Override
    public Float getFloat(String key) {
        return mapConfig.getFloat(key);
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return mapConfig.getFloat(key, defaultValue);
    }

    @Override
    public Byte getByte(String key) {
        return mapConfig.getByte(key);
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return mapConfig.getByte(key, defaultValue);
    }

    @Override
    public List<?> getList(String key) {
        return mapConfig.getList(key);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> type) {
        return mapConfig.getList(key, type);
    }

    @Override
    public List<?> getList(String key, List<?> defaultValue) {
        return mapConfig.getList(key, defaultValue);
    }

    @Override
    public <T> T get(Class<T> type, String key) {
        return mapConfig.get(type, key);
    }

    @Override
    public <T> T get(Class<T> type, String key, T defaultValue) {
        return mapConfig.get(type, key, defaultValue);
    }

    @Override
    public <T> T get(Type type, String key) {
        return mapConfig.get(type, key);
    }

    @Override
    public <T> T get(Type type, String key, T defaultValue) {
        return mapConfig.get(type, key, defaultValue);
    }

    @Override
    public boolean containsKey(String key) {
        return mapConfig.containsKey(key);
    }

    @Override
    public Iterator<String> getKeys() {
        return mapConfig.getKeys();
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        return mapConfig.getKeys(prefix);
    }

    @Override
    public Config getPrefixedView(String prefix) {
        return mapConfig.getPrefixedView(prefix);
    }

    @Override
    public void setStrInterpolator(StrInterpolator interpolator) {
        mapConfig.setStrInterpolator(interpolator);
    }

    @Override
    public StrInterpolator getStrInterpolator() {
        return mapConfig.getStrInterpolator();
    }

    @Override
    public void setDecoder(Decoder decoder) {
        mapConfig.setDecoder(decoder);
    }

    @Override
    public Decoder getDecoder() {
        return mapConfig.getDecoder();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return mapConfig.accept(visitor);
    }

    @Override
    public String resolve(String value) {
        return mapConfig.resolve(value);
    }

    @Override
    public <T> T resolve(String value, Class<T> type) {
        return mapConfig.resolve(value, type);
    }

    @Override
    public void forEachProperty(BiConsumer<String, Object> consumer) {
        mapConfig.forEachProperty(consumer);
    }

    @Override
    public String getName() {
        return mapConfig.getName();
    }

    @Override
    public boolean isEmpty() {
        return mapConfig.isEmpty();
    }

    /**
     * Reads a YAML configuration from the given {@link InputStream}. This will assume that the stream can be decoded
     * in UTF-8.
     *
     * @param configName  a non-null non-empty configuration name.
     * @param inputStream a non-null {@link InputStream}. It is up to caller to close this {@link InputStream}
     * @return an instance of {@link Config}
     * @throws IOException if there's an error parsing
     */
    public static Config fromInputStream(final String configName, @WillNotClose final InputStream inputStream) throws IOException {
        return fromReader(configName, new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Reads a YAML configuration from the given {@link Reader}.
     *
     * @param configName a non-null non-empty configuration name.
     * @param reader     a non-null {@link Reader}. It is up to caller to close this {@link Reader}
     * @return an instance of {@link Config}
     * @throws IOException if there's an error parsing
     */
    public static Config fromReader(final String configName, @WillNotClose Reader reader) throws IOException {
        try {
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
            dumperOptions.setWidth(Integer.MAX_VALUE);
            final Yaml yaml = new Yaml(dumperOptions);
            return new YamlConfig(configName, flattenMap(yaml, yaml.load(reader)));
        } catch (YAMLException e) {
            throw new IOException("Unable to parse yaml config of " + configName, e);
        }
    }

    private static Map<String, String> flattenMap(final Yaml yaml, final Map<String, Object> values) {
        var builder = Maps.<String, String>newHashMapWithExpectedSize(values.size());
        flattenMap(yaml, values, "", builder);
        return builder;
    }

    @SuppressWarnings("unchecked")
    private static void flattenMap(
            final Yaml yaml,
            final Map<String, Object> values,
            final String parentKey,
            final Map<String, String> builder
    ) {
        for (var entry : values.entrySet()) {
            var key = buildKey(parentKey, entry.getKey());
            var value = entry.getValue();

            if (value == null) {
                builder.put(key, null);
            } else if (value instanceof Map) {
                flattenMap(yaml, (Map<String, Object>) value, key, builder);
            } else if (value instanceof Collection) {
                builder.put(key, yaml.dump(value).trim());
            } else {
                builder.put(key, String.valueOf(value));
            }
        }
    }

    private static String buildKey(String parentKey, String key) {
        if (parentKey.isEmpty()) return key;
        return parentKey + "." + key;
    }
}
