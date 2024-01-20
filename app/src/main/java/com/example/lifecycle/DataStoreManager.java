package com.example.lifecycle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DataStoreManager {
    public static final DataStoreManager instance = new DataStoreManager();

    private static final Handler dataStoreManagerHandler = new Handler(Looper.getMainLooper());

    private DataStoreManager() {}

    private RxDataStore<Preferences> dataStore;

    /**
     * should call first
     */
    public void init(Context context) {
        dataStore = new RxPreferenceDataStoreBuilder(context, "group6").build();
    }

    public void saveValue(String keyName, String value) {
        Preferences.Key<String> key = new Preferences.Key<>(keyName);

        dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            String currentKey = preferences.get(key);
            if (currentKey == null) {
                // Preferences.Key always return null when running first time
                saveValue(keyName, value);
            }

            mutablePreferences.set(key, value != null ? value : "");

            return Single.just(mutablePreferences);
        });
    }

    public void getStringValue(String keyName, StringValueDelegate stringValueDelegate) {
        Preferences.Key<String> key = new Preferences.Key<>(keyName);
        Flowable<String> flowable = dataStore.data().map(preferences -> preferences.get(key));
        flowable.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(String value) {
                stringValueDelegate.onGetValue(value);
            }

            @Override
            public void onError(Throwable t) {
                Log.d("-->", "getStringValue err: " + t);
            }

            @Override
            public void onComplete() {}
        });
    }

    interface StringValueDelegate {
        void onGetValue(String s);
    }
}
