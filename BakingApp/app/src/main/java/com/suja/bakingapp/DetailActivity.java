package com.suja.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
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
import com.suja.bakingapp.data.Recipe;

import com.suja.bakingapp.data.Steps;
import com.suja.bakingapp.presenter.IngredientRecyclerViewAdapter;
import com.suja.bakingapp.presenter.StepRecyclerViewAdapter;
import com.suja.bakingapp.ui.RecipeVideoFragment;
import com.suja.bakingapp.util.JSONUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements StepRecyclerViewAdapter.OnStepClickListener {
    private static String TAG = "com.suja.bakingapp.DEtailACtivity";
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepDesc;
    private SimpleExoPlayerView mPlayerView;
    private boolean mTwoPane=false;
    private static Recipe recipeSelected;
    private Steps savedStep;
    private Steps mStep;
    @BindView(R.id.error_video)
    TextView tvError;
    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;
    private PlaybackParams playbackparams;

    private Uri mExoPlayerURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        nestedScrollView=findViewById(R.id.nested_scroll_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Timber.plant(new Timber.DebugTree());
        String recipeName = intent.getStringExtra("recipe");
        recipeSelected = JSONUtil.getRecipeByName(recipeName, this);
        getSupportActionBar().setTitle(recipeSelected.getRecipeName());
        // Get a reference to the ingredient listview in the detail_fragment xml layout file
        RecyclerView ingredientView = findViewById(R.id.ingredient_recyclerview);
        IngredientRecyclerViewAdapter mAdapter = new IngredientRecyclerViewAdapter(this);
        mAdapter.setIngredients(recipeSelected.getIngredients());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientView.setLayoutManager(layoutManager);
        // Set the adapter on the GridView
        ingredientView.setAdapter(mAdapter);

        // Get a reference to the steps recyclerview in the detail_fragment xml layout file
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.step_detail_recyclerview);

        LinearLayoutManager listLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(listLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        StepRecyclerViewAdapter stepRecyclerViewAdapter = new StepRecyclerViewAdapter(this);
        stepRecyclerViewAdapter.setSteps(recipeSelected.getSteps());
        recyclerView.setAdapter(stepRecyclerViewAdapter);


        if (findViewById(R.id.detail_sw600) != null) {
//show the video selected on rotation only in oncreate
            mTwoPane = true;
            clearResumePosition();
            mPlayerView = findViewById(R.id.videoView);
            tvError=findViewById(R.id.error_video);
            tvError.setVisibility(View.INVISIBLE);
            if (savedInstanceState != null) {
                savedStep = (Steps) savedInstanceState.getSerializable("mStep");
                mStep = savedStep;//for continous rotation
                try {
                    mExoPlayerURL=Uri.parse(savedStep.getStepUrl());
                }
                catch (Exception e)
                {
                    mExoPlayerURL=null;
                }
                mStepDesc = findViewById(R.id.recipe_long_desc);
                mStepDesc.setText(savedStep.getStepDescription());
            }
        } else
            mTwoPane = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("mStep", mStep);

        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});

        if(mTwoPane)updateResumePosition();
        outState.putInt("currentWindow",currentWindow);
        outState.putLong("playbackPosition",playbackPosition);
        Log.i("media ----","storing playback"+playbackPosition);
        outState.putBoolean("playWhenReady",playWhenReady);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedStep = (Steps) savedInstanceState.getSerializable("mStep");
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            nestedScrollView.post(new Runnable() {
                public void run() {
                    nestedScrollView.scrollTo(position[0], position[1]);
                }
            });

        currentWindow=savedInstanceState.getInt("currentWindow");
        playbackPosition=savedInstanceState.getLong("playbackPosition",0);

        Log.i("media ----","getting playback"+playbackPosition);

        playWhenReady=savedInstanceState.getBoolean("playWhenReady");
    }

    @Override
    public void onStepSelected(int pos, Steps steps) {
        mStep = steps;
        Timber.i("onstep selected");
        if (mTwoPane) {
            mPlayerView =  findViewById(R.id.videoView);

            Timber.i("inside sw600");

            if (mExoPlayer != null) releasePlayer();

            playbackPosition= 0;
            initializePlayer(Uri.parse(mStep.getStepUrl()));
            mStepDesc = findViewById(R.id.recipe_long_desc);
            mStepDesc.setText(mStep.getStepDescription());

        } else {
            Timber.d("VideoActivity called");
            Intent intent = new Intent(this, RecipeVideoActivity.class);
            intent.putExtra("position", pos);
            Timber.d("position selected" + pos);
            intent.putExtra("fullSteps", recipeSelected.getSteps());
            this.startActivity(intent);
        }
    }

    /**
     * Initialize ExoPlayer.
     *
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
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
          // if(mTwoPane) initializePlayer(mExoPlayerURL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
           if(mTwoPane) initializePlayer(mExoPlayerURL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
           if(mTwoPane) releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if(mTwoPane)releasePlayer();
        }
    }


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            if(mTwoPane)releasePlayer();
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
