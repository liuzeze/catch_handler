package com.lz.httplib.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lz.httplib.gson.IntegerDefault0Adapter;
import com.lz.httplib.gson.LongDefault0Adapter;

/**
 * Created by Allen on 2017/11/20.
 */

public class GsonAdapter {

    public static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(String.class, new StringDefaultAdapter())
                .create();

        return gson;
    }
}
