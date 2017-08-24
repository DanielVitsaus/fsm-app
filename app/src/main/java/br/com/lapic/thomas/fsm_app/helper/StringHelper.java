package br.com.lapic.thomas.fsm_app.helper;

/**
 * Created by thomas on 23/08/17.
 */

public class StringHelper {

    public static String removeAllChar(String str) {
        return str.replaceAll("[^\\d.]", "");
    }

}
