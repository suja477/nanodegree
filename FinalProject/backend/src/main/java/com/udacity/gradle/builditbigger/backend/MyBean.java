package com.udacity.gradle.builditbigger.backend;

import com.suja.joke.MyJoke;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;
    private String joke;

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public String getJoke()
    {

        return joke;
    }
}