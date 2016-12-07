package com.jason.workdemo.annotation;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by liuzhenhui on 2016/12/7.
 */
public class AnnotationActivity extends Activity {
    public static final String TAG = AnnotationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IceCreamFlavourManager iceCreamFlavourManager = new IceCreamFlavourManager();
        iceCreamFlavourManager.setFlavour(IceCreamFlavourManager.CHOCOLATE);
        iceCreamFlavourManager.setName(IceCreamFlavourManager.LONG);
    }
}
