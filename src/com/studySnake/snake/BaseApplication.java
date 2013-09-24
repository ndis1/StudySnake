package com.studySnake.snake;

import javax.inject.Singleton;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.studySnake.snake.model.BaseManager;
import com.studySnake.snake.model.ForgotPasswordDialogFragment;
import com.studySnake.snake.model.UserManager;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class BaseApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MyModule(this));
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    @Module(injects = { BaseActivity.class,
            BaseFragment.class,LoginFragment.class,Login.class,BaseManager.class,UserManager.class,CreateAccountFragment.class,ForgotPasswordDialogFragment.class,
    })
    static class MyModule {
        private final Context appContext;
        private final Bus bus;
        MyModule(Context appContext) {
            this.appContext = appContext;
            bus = new Bus();
        }

        @Provides
        @Singleton
        Bus provideBus() {
            return bus;
        }
        @Provides
        @Singleton
        UserManager providesUserManager() {
            return new UserManager(bus);
        }
        
    }
}