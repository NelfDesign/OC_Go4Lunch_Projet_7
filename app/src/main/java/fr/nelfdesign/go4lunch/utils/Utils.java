package fr.nelfdesign.go4lunch.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by Nelfdesign at 01/12/2019
 * fr.nelfdesign.go4lunch.utils
 */
public abstract class Utils {

    //Show Snack Bar with a message
    public static void showSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
