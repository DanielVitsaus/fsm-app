package br.com.lapic.thomas.syncplayer.helper;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by thomas on 23/08/17.
 */

public class StringHelper {

    public static String removeAllChar(String str) {
        return str.replaceAll("[^\\d.]", "");
    }

    public static String getParam(String str) {
        return str.substring(str.lastIndexOf("=") + 1);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String incrementIp(String ip, int value) {
        String prefixIp = ip.substring(0, ip.lastIndexOf(".")+1);
        String lastIp = ip.substring(ip.lastIndexOf(".")+1);
        int val = Integer.parseInt(lastIp) + value;
        return prefixIp + String.valueOf(val);
    }

}
