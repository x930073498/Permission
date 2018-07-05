package com.x930073498.permission;

import android.util.SparseArray;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class RequestManager {
    private SparseArray<Retrofit> map = new SparseArray<>();

    public static RequestManager getInstance() {
        return Holder.INSTANCE;
    }

    private RequestManager() {

    }

    private static class Holder {
        static RequestManager INSTANCE = new RequestManager();
    }

    public Retrofit get(@HostType int type) {
        Retrofit retrofit = map.get(type);
        if (retrofit == null) {
            retrofit = create(type);
            map.put(type, retrofit);
        }
        return retrofit;
    }

    private String getUrl(@HostType int type) {
        String url = null;
        switch (type) {
            case HostType.TYPE_GANK:
                url = "http://gank.io/";
                break;
        }
        return url;
    }

    private Retrofit create(@HostType int type) {
        Retrofit retrofit = null;
        switch (type) {
            case HostType.TYPE_GANK:
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                        .build();
                retrofit = new Retrofit.Builder()
                        .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .baseUrl(getUrl(type))
                        .build();
                break;
        }
        return retrofit;
    }

    public Retrofit get() {
        return get(HostType.TYPE_GANK);
    }

}
