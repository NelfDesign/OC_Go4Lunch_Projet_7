package fr.nelfdesign.go4lunch.base;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Nelfdesign at 05/12/2019
 * fr.nelfdesign.go4lunch.base
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

    }
}
