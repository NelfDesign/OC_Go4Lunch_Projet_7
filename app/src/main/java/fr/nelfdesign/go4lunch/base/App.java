package fr.nelfdesign.go4lunch.base;

import android.app.Application;

import fr.nelfdesign.go4lunch.apiGoogleMap.RestaurantService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

    public static RestaurantService retrofitCall(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantService googleMapService = retrofit.create(RestaurantService.class);

        return googleMapService;
    }
}
