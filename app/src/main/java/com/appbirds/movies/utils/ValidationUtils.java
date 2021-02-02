package com.appbirds.movies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static <T> boolean isValidObject(T object) {
        if (object != null) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> boolean isValidList(List<T> list) {
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidString(String string) {
        if (string != null && string.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidString(EditText editText) {
        String text = getStringFromEditText(editText);
        if (text != null && text.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidString(EditText editText, String error) {
        if (isValidString(getStringFromEditText(editText))) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(error);
            return false;
        }
    }

    public static String getStringFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isValidEmailAddress(EditText argEditText) {

        try {
            if (isValidString(getStringFromEditText(argEditText))) {
                argEditText.setError(null);
                Pattern pattern = Pattern.compile
                        ("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                Matcher matcher = pattern.matcher(argEditText.getText());
                if (matcher.matches()) {
                    argEditText.setError(null);
                    return true;
                } else {
//                    argEditText.setError("Please Enter valid email address.");
                    return false;
                }
            } else {
//                argEditText.setError("Please Enter email address.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidPhoneNumber(EditText editText) {
        String text = editText.getText().toString();
        if (isValidString(text) && text.length() >= 10 && text.length() <= 20)
            return true;
        return false;
    }

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isValidSpinner(Spinner spinner, String title) {

        return !title.equals(spinner.getSelectedItem().toString());
    }
}
