package com.example.mg.todo.DI;

import com.bumptech.glide.RequestManager;
import com.example.mg.todo.App;
import com.example.mg.todo.data.PrefrenceHelper;

import dagger.Component;

@IAppScope
@Component(modules = {appModule.class})
public interface IAppComponent {

    PrefrenceHelper prefrenceHelper();

    RequestManager glide();

    void inject(App app);
}
