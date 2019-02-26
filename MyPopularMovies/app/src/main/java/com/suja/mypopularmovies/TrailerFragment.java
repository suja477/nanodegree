package com.suja.mypopularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suja.popularmovies.R;

/**
 * Created by Suja Manu on 3/7/2018.
 */

public class TrailerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.trailer_recycler_view_fragment,
                container, false);
        return view;
    }

}
