package com.example.mg.todo;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mg.todo.data.IPrefHelper;
import com.example.mg.todo.data.PrefrenceHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class appModule {

    private App appContext;

    public appModule(App appContext) {
        this.appContext = appContext;
    }

    @Provides
    @IAppContext
    @IAppScope
    public Context provideAppContxt() {
        return appContext;
    }

    @Provides
    @IAppScope
    RequestManager glide() {
        return Glide.with(appContext);
    }

    @Provides
    @IAppContext
    IPrefHelper prefHelper(PrefrenceHelper prefrenceHelper) {
        return prefrenceHelper;
    }
}
