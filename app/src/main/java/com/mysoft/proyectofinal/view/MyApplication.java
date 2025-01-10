package com.mysoft.proyectofinal.view;

import android.app.Application;

import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.model.Post;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        // Registrar la clase Post antes de inicializar Parse
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(false);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
