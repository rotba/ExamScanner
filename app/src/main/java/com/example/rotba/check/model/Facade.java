package com.example.rotba.check.model;

import android.graphics.Bitmap;

/**
 * Created by rotba on 11/01/2020.
 */

public class Facade {
    private static final Facade ourInstance = new Facade();

    public static Facade getInstance() {
        return ourInstance;
    }

    private Facade() {
    }

    public void scanExaminneSolution(Bitmap solution, int checkId) throws NotImplementedException {
        throw new NotImplementedException();
    }
}
