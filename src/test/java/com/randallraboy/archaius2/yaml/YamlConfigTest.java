package com.randallraboy.archaius2.yaml;

import com.netflix.archaius.api.Config;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class YamlConfigTest {
    @Test
    void SimpleMapWithStringValue() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("hello: world"));

        assertEquals("world", result.getString("hello"));
    }

    @Test
    void SimpleMapWithIntValue() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("int: 1234"));

        assertEquals(1234, result.getInteger("int"));
    }

    @Test
    void SimpleMapWithListValue() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("list: [1,2,3,4]"));

        assertEquals("[1, 2, 3, 4]", result.getString("list"));
    }

    @Test
    void SimpleMapWithNullValue() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("un: null"));

        assertNull(result.getRawProperty("un"));
    }

    @Test
    void SimpleMapWithNullValue2() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("un: ~"));

        assertNull(result.getRawProperty("un"));
    }

    @Test
    void NestedNestedString() throws Exception {
        final var result = YamlConfig.fromReader("test", fromString("hello:\n  planet:\n    earth"));

        assertEquals("earth", result.getString("hello.planet"));
    }

    @Test
    void ExampleFile() throws Exception {
        final Config result;
        try (InputStream testResource = getClass().getResourceAsStream("/example-log4j2.yaml")) {
            result = YamlConfig.fromInputStream("test", testResource);
        }

        // simple string
        assertEquals("%d %p %C{1.} [%t] %m%n", result.getString("Configuration.appenders.File.PatternLayout.Pattern"));

        // arrays of map
        assertEquals("[{name: org.apache.logging.log4j.test1, level: debug, additivity: false, ThreadContextMapFilter: {KeyValuePair: {key: test, value: 123}}, AppenderRef: {ref: STDOUT}}, {name: org.apache.logging.log4j.test2, level: debug, additivity: false, AppenderRef: {ref: File}}]",
                result.getString("Configuration.Loggers.logger"));
    }

    private static Reader fromString(String yamlValue) {
        return new StringReader(yamlValue);
    }
}