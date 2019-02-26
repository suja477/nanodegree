package com.suja.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suja.bakingapp.R;

/**
 * Created by Suja Manu on 4/6/2018.
 */

public class StepDescFragment extends Fragment {
    public StepDescFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.step_desc_fragment, container, false);




        // Return the root view
        return rootView;
    }

}
