package lishui.service.net.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lishui.lib.base.log.LogUtils;

/**
 * author : linlishui
 * time   : 2021/11/12
 * desc   : 基于Gson框架的Json工具类
 */
public class JsonUtil {

    private static final String TAG = JsonUtil.class.getSimpleName();

    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapterFactory(new PostProcessingEnabler())
                    .disableHtmlEscaping()
                    .create();


    @Nullable
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        T result = null;
        try {
            result = gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static <T> T fromJson(String jsonString, Type typeOfT) {
        T result = null;
        try {
            result = gson.fromJson(jsonString, typeOfT);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz) {
        T result = null;
        try {
            result = gson.fromJson(jsonElement, clazz);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static <T> T fromJson(JsonElement jsonElement, Type type) {
        T result = null;
        try {
            result = gson.fromJson(jsonElement, type);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static <T> T fromJson(String jsonString, TypeToken<T> typeToken) {
        T result = null;
        try {
            result = gson.fromJson(jsonString, typeToken.getType());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static <T> List<T> fromJson(JsonArray jsonArray, TypeToken<List<T>> typeToken) {
        List<T> result = null;
        try {
            result = gson.fromJson(jsonArray, typeToken.getType());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String toJsonString(Object src) {
        String json = "";
        try {
            json = gson.toJson(src);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    public static String toJsonString(Object object, TypeToken typeToken) {
        try {
            return gson.toJson(object, typeToken.getType());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String toJsonString(JsonElement jsonElement) {
        String json = "";
        try {
            json = gson.toJson(jsonElement);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    public static <T> List<T> stringToList(String gsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        if (gson != null) {
            JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    public static <T> String toJsonString(List<T> list, TypeToken<List<T>> typeToken) {
        String json = "";
        try {
            JsonArray jsonArray = gson.toJsonTree(list, typeToken.getType()).getAsJsonArray();
            json = jsonArray.toString();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Nullable
    public static JsonObject toJsonObject(Object src) {
        JsonObject json = null;
        try {
            json = gson.toJsonTree(src).getAsJsonObject();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Nullable
    public static String getString(JsonObject jsonObject, String key) {
        String value = null;
        try {
            JsonElement jsonElement = jsonObject.has(key) ? jsonObject.get(key) : null;
            if (jsonElement != null && !jsonElement.isJsonNull()) {
                value = jsonElement.isJsonObject() ? jsonElement.toString() : jsonObject.get(key).getAsString();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "getString", e);
        }
        return value;
    }

    public static float getFloat(JsonObject jsonObject, String key, float defaultValue) {
        float value = defaultValue;
        try {
            value = jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsFloat() : defaultValue;
        } catch (Exception e) {
            LogUtils.e(TAG, "getLong", e);
        }
        return value;
    }

    public static long getLong(JsonObject jsonObject, String key, long defaultValue) {
        long value = defaultValue;
        try {
            value = jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsLong() : defaultValue;
        } catch (Exception e) {
            LogUtils.e(TAG, "getLong", e);
        }
        return value;
    }

    public static int getInt(JsonObject jsonObject, String key, int defaultValue) {
        int value = defaultValue;
        try {
            value = jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsInt() : defaultValue;
        } catch (Exception e) {
            LogUtils.e(TAG, "getInt", e);
        }
        return value;
    }

    public static boolean getBoolean(JsonObject jsonObject, String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            value = jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsBoolean() : defaultValue;
        } catch (Exception e) {
            LogUtils.e(TAG, "getInt", e);
        }
        return value;
    }

    public static <T> T getExtra(Bundle extras, String key, Type type) {
        String json = extras.getString(key);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json, type);
        } catch (Throwable t) {
            LogUtils.e(TAG, key + "-" + json, t);
        }
        return null;
    }

    public static void putExtra(Intent intent, String key, Object value) {
        if (value == null) {
            return;
        }
        try {
            String json = new Gson().toJson(value);
            intent.putExtra(key, json);
        } catch (Throwable t) {
            LogUtils.e(TAG, key, t);
        }
    }

    public static class PostProcessingEnabler implements TypeAdapterFactory {
        public interface PostProcessable {
            void postProcess();
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

            return new TypeAdapter<T>() {
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                public T read(JsonReader in) throws IOException {
                    T obj = null;
                    /**
                     * try and skipValue
                     * in case of exception when parse wrong type, such as converting String to Integer
                     */
                    try {
                        obj = delegate.read(in);
                    } catch (Exception e) {
                        in.skipValue();
                        LogUtils.d(JsonUtil.class.getSimpleName(), e.getMessage());
                    }
                    if (obj instanceof PostProcessable) {
                        ((PostProcessable) obj).postProcess();
                    }
                    return obj;
                }
            };
        }
    }
}
