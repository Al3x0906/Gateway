package org.project.gateway.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
public class ObjectSerializer {

    private static final Map<String, Class<?>> clazzCache = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    private ObjectSerializer() {
        // Helper class
    }

    public static <T> T fromJson(String data, Class<T> clazz) {
        T obj = null;
        try {
            obj = mapper.readValue(data, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return obj;
    }

    public static String toJson(String data) {
        String o = null;
        try {
            o = mapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(String clazz) {
        return (Class<T>) clazzCache.computeIfAbsent(clazz, k -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage());
            }
            return null;
        });
    }
}
