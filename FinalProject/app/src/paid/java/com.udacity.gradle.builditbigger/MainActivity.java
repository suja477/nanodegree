package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;

import com.suja.displaylibrary.MainDisplayActivity;




public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner=findViewById(R.id.progressBar1);

          }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void tellJoke(View view) {
        spinner.setVisibility(View.VISIBLE);
                new MyEndpointsAsyncTask(getApplicationContext(),this).
                        execute(new Pair<Context, String>(getApplicationContext(), "Suja"));

            }


    @Override
    public void onTaskComplete(String result) {

        // decrement the counter when callback is invoked.
       // idlingResource.decrement();
        if (spinner != null)
            spinner.setVisibility(View.GONE);
        // dialog.dismiss();
        Intent intent = new Intent(this, MainDisplayActivity.class);
        intent.putExtra(String.valueOf(R.string.JOKE_INTENT_KEY), result);
        this.startActivity(intent);

        //   Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}

