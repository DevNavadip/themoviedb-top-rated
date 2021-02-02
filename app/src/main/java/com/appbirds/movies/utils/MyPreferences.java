package com.appbirds.movies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;


//Preference Class..
public class MyPreferences {

    private static final String TAG = MyPreferences.class.getSimpleName();

    private static MyPreferences instance;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFS_NAME = "com.appbirds.movies";

    public static final String PREF_KEY_STAGE1 = "pref_stage1";

    public static final String prefId = "prefIdAppBirdsMovies";

    public static final String MODEL_MOVIES = "MODEL_MOVIES";

    public static String getPref(Context mContext, String prefkey) {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE);
            return preferences.getString(prefkey, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setPref(Context mContext, String key, Object value) {
        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Enum) {
                editor.putString(key, value.toString());
            } else if (value != null) {
                throw new RuntimeException("Attempting to save non-supported preference");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T get(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(prefId, Context.MODE_PRIVATE);
        return (T) preferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Context context, String key, T defValue) {
        SharedPreferences preferences = context.getSharedPreferences(prefId, Context.MODE_PRIVATE);
        T returnValue = (T) preferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public static void setObject(Context mContext, String key, Object object) {
        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

            if (object != null) {
                editor.remove(key);
                editor.putString(key, new Gson().toJson(object));
            } else {
                editor.remove(key);
                editor.putString(key, "");
            }
            editor.apply();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static <T> T getObject(Context mContext, String key, Class<T> a) {

        try {
            SharedPreferences preferences = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE);
            String gson = preferences.getString(key, null);
            return new Gson().fromJson(gson, a);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getObject(Context mContext, String key, Type type) {

        try {
            SharedPreferences preferences = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE);
            String gson = preferences.getString(key, null);
            return new Gson().fromJson(gson, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void clearPref(Context mContext) {
        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

            editor.clear();
            editor.apply();
            editor.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void setList(Context mContext, String key, List<T> list) {


        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

            Gson gson = new Gson();
            String json = gson.toJson(list);

            editor.putString(key, json);
            editor.apply();
            editor.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static synchronized MyPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MyPreferences(context);
        }
        return instance;
    }

    private MyPreferences(Context context) {
        instance = this;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void clearSharedPreferences(Context ctx) {
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        String id = prefId + ".xml";
        if (children != null && children.length > 0) {
            for (String child : children) {

                if (id.equalsIgnoreCase(child)) {
                    // clear preference file
                    clearPref(ctx);
                    //delete the file
                    String filePath = ctx.getFilesDir().getParent() + "/shared_prefs/prefIdAppBirdsMovies.xml";
                    File deletePrefFile = new File(filePath);
                    boolean isFile = deletePrefFile.delete();
                    Log.d(TAG, "clearSharedPreferences: " + isFile);
                    break;
                }

            }
        }
    }

    public static void clearSharedPreferences2(Context ctx) {
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        if (children != null && children.length > 0) {
            for (String child : children) {
                // clear each preference file
                Log.d(TAG, "clearSharedPreferences2: " + child);
            }
        }
    }

}
