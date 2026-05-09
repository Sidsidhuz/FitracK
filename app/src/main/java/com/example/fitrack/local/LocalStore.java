package com.example.fitrack.local;

import android.content.Context;

import com.example.fitrack.models.Admin;
import com.example.fitrack.models.Attendance;
import com.example.fitrack.models.Client;
import com.example.fitrack.models.PersonalRecord;
import com.example.fitrack.models.ProgressPhoto;
import com.example.fitrack.models.Schedule;
import com.example.fitrack.models.WeightProgress;
import com.example.fitrack.models.Workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public final class LocalStore {
    private static final String STORE_DIR_NAME = "local_store";
    private static final Map<Class<?>, Class<?>> TYPE_MARKERS = new HashMap<>();
    private static Context appContext;

    private LocalStore() {
    }

    public static synchronized void initialize(Context context) {
        appContext = context.getApplicationContext();
    }

    public static String generateId(String collection) {
        return UUID.randomUUID().toString();
    }

    public static synchronized void save(String collection, String id, Object obj) {
        Map<String, Object> records = readCollection(collection);
        records.put(id, obj);
        writeCollection(collection, records);
    }

    public static synchronized void delete(String collection, String id) {
        Map<String, Object> records = readCollection(collection);
        records.remove(id);
        writeCollection(collection, records);
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> List<T> getAll(String collection) {
        Map<String, Object> records = readCollection(collection);
        if (records.isEmpty())
            return Collections.emptyList();
        return new ArrayList<>((List<T>) new ArrayList<>(records.values()));
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> List<T> whereEqualTo(String collection, String fieldName, Object value) {
        Map<String, Object> records = readCollection(collection);
        if (records.isEmpty())
            return Collections.emptyList();
        List<T> out = new ArrayList<>();
        for (Object o : records.values()) {
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

    private static Map<String, Object> readCollection(String collection) {
        ensureInitialized();
        File file = collectionFile(collection);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Object stored = inputStream.readObject();
            if (stored instanceof Map) {
                return new HashMap<>((Map<String, Object>) stored);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            // Fall through to empty store if the file cannot be read.
        }
        return new HashMap<>();
    }

    private static void writeCollection(String collection, Map<String, Object> records) {
        ensureInitialized();
        File file = collectionFile(collection);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject((Serializable) new HashMap<>(records));
        } catch (IOException ignored) {
            // Storage stays best-effort in demo mode.
        }
    }

    private static File collectionFile(String collection) {
        return new File(new File(appContext.getFilesDir(), STORE_DIR_NAME), collection + ".ser");
    }

    private static void ensureInitialized() {
        if (appContext == null) {
            throw new IllegalStateException("LocalStore has not been initialized");
        }
    }
}
