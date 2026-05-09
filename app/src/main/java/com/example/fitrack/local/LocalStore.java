package com.example.fitrack.local;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class LocalStore {
    private static final Map<String, Map<String, Object>> STORE = new HashMap<>();

    private LocalStore() {
    }

    public static String generateId(String collection) {
        return UUID.randomUUID().toString();
    }

    public static void save(String collection, String id, Object obj) {
        STORE.computeIfAbsent(collection, k -> new HashMap<>()).put(id, obj);
    }

    public static void delete(String collection, String id) {
        Map<String, Object> col = STORE.get(collection);
        if (col != null)
            col.remove(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll(String collection) {
        Map<String, Object> col = STORE.get(collection);
        if (col == null)
            return Collections.emptyList();
        return new ArrayList<>((List<T>) (new ArrayList<>(col.values())));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> whereEqualTo(String collection, String fieldName, Object value) {
        Map<String, Object> col = STORE.get(collection);
        if (col == null)
            return Collections.emptyList();
        List<T> out = new ArrayList<>();
        for (Object o : col.values()) {
            try {
                Field f = null;
                Class<?> cls = o.getClass();
                // try direct field first
                try {
                    f = cls.getDeclaredField(fieldName);
                } catch (NoSuchFieldException nsf) {
                    // try camelCase getter fallback by fieldName
                }
                if (f != null) {
                    f.setAccessible(true);
                    Object val = f.get(o);
                    if (value == null ? val == null : value.equals(val)) {
                        out.add((T) o);
                    }
                } else {
                    // fallback: try getter get<FieldName>()
                    String getter = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                    try {
                        Object val = cls.getMethod(getter).invoke(o);
                        if (value == null ? val == null : value.equals(val)) {
                            out.add((T) o);
                        }
                    } catch (Exception ignore) {
                        // ignore
                    }
                }
            } catch (Exception ignore) {
                // ignore reflection errors per-object
            }
        }
        return out;
    }
}
