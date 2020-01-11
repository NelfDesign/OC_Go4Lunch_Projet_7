package fr.nelfdesign.go4lunch.apiFirebase;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.models.Workers;
import io.reactivex.Observable;

/**
 * Created by Nelfdesign at 11/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public interface WorkersServices {

    Observable<ArrayList<Workers>> getQueryWorkersWithChoiceRestaurant(String placeIdData);
}
