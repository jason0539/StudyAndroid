package com.jason.workdemo.demo.annotation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

/**
 * Created by liuzhenhui on 2016/12/7.
 */
public class IceCreamFlavourManager {
    public static final String TAG = IceCreamFlavourManager.class.getSimpleName();

    private int flavour;
    public static final int VANILLA = 0;
    public static final int CHOCOLATE = 1;
    public static final int STRAWBERRY = 2;

    @IntDef({VANILLA, CHOCOLATE, STRAWBERRY})
    public @interface Flavour {
    }

    @Flavour
    public int getFlavour() {
        return flavour;
    }

    public void setFlavour(@Flavour int flavour) {
        this.flavour = flavour;
    }


    private String name;
    public static final String SHORT = "SHORT";
    public static final String LONG = "LONG";

    @StringDef({SHORT, LONG})
    public @interface Name {

    }

    @Name
    public String getName() {
        return name;
    }

    public void setName(@Name String name) {
        this.name = name;
    }
}
