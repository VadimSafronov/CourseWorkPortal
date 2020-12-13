package com.safronov.courseworksapp.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkHelper {
    public static boolean isNetConn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
