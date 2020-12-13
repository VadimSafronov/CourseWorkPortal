package com.safronov.courseworksapp.Helpers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.safronov.courseworksapp.R;

public class SnackbarHelper {

    private Snackbar snackbar;

    public SnackbarHelper(View view, Context context) {
        snackbar = Snackbar.make(view, "Load...", Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showStaticSnackbar(View view, Context context) {
        Snackbar snackbar = Snackbar
                .make(view, "Turn on Internet connection", Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }

    public void showSnackbar() {
        snackbar.show();
    }

    public void closeSnackbar() {
        snackbar.dismiss();
    }
}
