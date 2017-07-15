package com.jason.workdemo.demo.json.bean;

import java.io.Serializable;

/**
 * Created by liuzhenhui on 2017/7/15.
 */

public class EffectJsonModel implements Serializable{
    String effectName;
    String jsonName;

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }
}