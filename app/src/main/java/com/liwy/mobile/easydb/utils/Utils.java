package com.liwy.mobile.easydb.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by liwy on 2017/4/26.
 */

public class Utils {

    @SuppressLint({"SimpleDateFormat"})
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static java.util.Date stringToDateTime(String strDate)
    {
        if (strDate != null) {
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
