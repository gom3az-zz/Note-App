package com.example.mg.todo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {
    private RefWatcher mRefWatcher;
    private IAppComponent mComponent;

    public IAppComponent geAppComponent() {
        return mComponent;
    }

    public static App get(Activity activity) {
        return (App) activity.getApplication();
    }


    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initDagger();
    }

    private void initDagger() {
        appModule module= new appModule(this);
        mComponent = DaggerIAppComponent.builder()
                .appModule(module)
                .build();
        mComponent.inject(this);

    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {

            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }
}
