package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.suja.joke.MyJoke;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi there, " + name);
       MyJoke myJoke=new MyJoke();

       response.setJoke(myJoke.getJoke());

        return response;
    }
    @ApiMethod(name = "myJoke")
    public MyBean myJoke(){
        MyBean response = new MyBean();
        MyJoke myJoke=new MyJoke();
        response.setJoke(myJoke.getJoke());
        return response;
    }

}
