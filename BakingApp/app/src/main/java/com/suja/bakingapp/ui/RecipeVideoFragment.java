package com.suja.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suja.bakingapp.R;

import timber.log.Timber;

/**
 * Created by Suja Manu on 4/6/2018.
 */

public class RecipeVideoFragment extends android.app.Fragment {


    public RecipeVideoFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.video_layout_fragment, container, false);
        Timber.d("done ");



        // Return the root view
        return rootView;
    }

}
