package com.suja.displaylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainDisplayActivity extends AppCompatActivity {
    public final String JOKE_INTENT_KEY = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity_main);

        TextView tvJoke=(TextView)findViewById(R.id.jokeDisplayView);
        Intent intent=getIntent();
        String joke=intent.getStringExtra(String.valueOf(R.string.JOKE_INTENT_KEY));
        if(joke!=null)
        {Log.i("joke",joke);
        tvJoke.setText(joke);}
        else
            tvJoke.setText(R.string.NoJoke);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
