package br.com.lapic.thomas.fsm_app.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by thomas on 26/08/17.
 */

public class AppConstants {

    public static String MEDIAS_PARCEL = "medias_parcel";
    public static File FILE_PATH_DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    public static String PATH_APP = "app/";
    public static String PATH_METADATA_FILE = "app/main.json";
    public static String VIDEO = "video";
    public static String IMAGE = "image";
    public static String AUDIO = "audio";
    public static String URL = "url";
    public static String MEDIAS = "medias";
    public static String ID = "id";
    public static String SRC = "src";
    public static String TYPE = "type";
    public static String GROUPS = "groups";
    public static String ANCHORS = "anchors";
    public static String BEGIN = "begin";
    public static String END = "end";
    public static String DUR = "dur";

    public static String GET_AMOUNT_GROUPS = "get_amount_groups";
    public static String TOTAL_GROUPS = "total_groups=";
    public static String DEVICE = "device=";
}
