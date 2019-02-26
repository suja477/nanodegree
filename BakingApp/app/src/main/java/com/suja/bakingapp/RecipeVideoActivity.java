package com.suja.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.suja.bakingapp.data.Steps;

import timber.log.Timber;

public class RecipeVideoActivity extends AppCompatActivity {

    private static final String TAG = "RecipeVideoActivity";
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepDesc;
    private TextView tvError;
    private SimpleExoPlayerView mPlayerView;
    private static Steps[] steps;
    private static int position;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;
    private PlaybackParams playbackparams;

    private Uri mExoPlayerURL;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("storedPosition",position);
        updateResumePosition();
        outState.putInt("currentWindow",currentWindow);
        outState.putLong("playbackPosition",playbackPosition);
        Log.i("media ----","storing playback"+playbackPosition);
        outState.putBoolean("playWhenReady",playWhenReady);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        position = savedInstanceState.getInt("storedPosition", 0);
        currentWindow=savedInstanceState.getInt("currentWindow");
        playbackPosition=savedInstanceState.getLong("playbackPosition",0);

        Log.i("media ----","getting playback"+playbackPosition);

        playWhenReady=savedInstanceState.getBoolean("playWhenReady");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video);

        Timber.plant(new Timber.DebugTree());
        Intent intent=getIntent();

         steps= (Steps[]) intent.getSerializableExtra("fullSteps");

         //update all views
        mPlayerView =  findViewById(R.id.videoView);
        tvError=findViewById(R.id.error_video);
        tvError.setVisibility(View.INVISIBLE);

        mStepDesc =findViewById(R.id.recipe_long_desc);
         //if there is no savedinstance then get from intent
        if(savedInstanceState==null)
        {position=intent.getIntExtra("position",0);
        clearResumePosition();
           }

        Steps stepItem=steps[position];//else get position from savedstate,
        //exoplayer get released in ondestroy during rotation ,so initialize it and setposition
       // initializePlayer(Uri.parse(stepItem.getStepUrl()));
        Log.i(TAG,steps[position].getStepDescription());

        try {
            mExoPlayerURL=Uri.parse(stepItem.getStepUrl());
        }
       catch (Exception e)
       {
         mExoPlayerURL=null;
       }
        mStepDesc.setText(steps[position].getStepDescription());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int count =  steps.length;
                int pos =position;
                int nextPos = 0;
                int prevPos = 0;
                if (pos < count) {
                    nextPos = pos + 1;
                    prevPos = pos - 1;

                } if (pos == count - 1) {

                    nextPos = 0;
                    prevPos = pos - 1;
                } if (pos == 0) {
                    nextPos = pos + 1;
                    prevPos = 0;

                }
                switch (item.getItemId()) {
                    case R.id.menu_next:
                        if(mExoPlayer!=null)
                        releasePlayer();
                        playbackPosition=0;
                        initializePlayer(Uri.parse(steps[nextPos].getStepUrl()));
                        position=nextPos;
                        mStepDesc.setText(steps[position].getStepDescription());
                        break;
                    case R.id.menu_prev:
                        if(mExoPlayer!=null)
                        releasePlayer();
                        playbackPosition=0;
                        initializePlayer(Uri.parse(steps[prevPos].getStepUrl()));
                        position=prevPos;
                        mStepDesc.setText(steps[position].getStepDescription());
                        break;
                }
                return true;
            }
        });
        //to change video to fullscreen in landscape
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE))
        {
            mStepDesc.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            try {
               if(mediaUri.toString().equalsIgnoreCase(""))throw new Exception();
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mPlayerView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
                //mExoPlayer.seekTo(playbackPosition);
                Log.i("exoplayer-----","placbackpos----"+playbackPosition);
          //  mExoPlayer.prepare(mediaSource,false,true);



                boolean haveResumePosition = currentWindow != C.INDEX_UNSET;
                if (haveResumePosition) {
                    mExoPlayer.seekTo(currentWindow, playbackPosition);
                }
                mExoPlayer.setPlayWhenReady(playWhenReady);
                //mExoPlayer.seekTo(playbackPosition);
                mExoPlayer.prepare(mediaSource, !haveResumePosition, false);

            }catch (Exception exception){
              // mPlayerView.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
               tvError.setText(R.string.error_no_video);

               mPlayerView.setVisibility(View.INVISIBLE);
                Timber.d("error in video play");

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
          //  initializePlayer(mExoPlayerURL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
           initializePlayer(mExoPlayerURL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }


    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
           updateResumePosition();

            //   resumePosition = Math.max(0, mExoPlayer.getCurrentPosition());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateResumePosition() {
        currentWindow = mExoPlayer.getCurrentWindowIndex();
        playbackPosition = Math.max(0, mExoPlayer.getCurrentPosition());
        playWhenReady = mExoPlayer.getPlayWhenReady();
    }
    private void clearResumePosition() {
        currentWindow = C.INDEX_UNSET;
        playbackPosition = C.TIME_UNSET;
    }

}
