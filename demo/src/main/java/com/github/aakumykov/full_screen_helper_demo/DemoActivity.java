package com.github.aakumykov.full_screen_helper_demo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.aakumykov.full_screen_helper.FullScreenController;
import com.github.aakumykov.full_screen_helper.FullScreenHelper;
import com.github.aakumykov.full_screen_helper.FullScreenState;
import com.github.aakumykov.full_screen_helper.ViewMarginsAdjustmentHelper;
import com.github.aakumykov.full_screen_helper_demo.databinding.ActivityDemoBinding;
import com.github.aakumykov.full_screen_helper_demo.fragments.FirstFragment;
import com.github.aakumykov.full_screen_helper_demo.fragments.SecondFragment;
import com.github.aakumykov.full_screen_helper_demo.interfaces.FullScreenableFragment;
import com.github.aakumykov.full_screen_helper_demo.interfaces.HasCustomTitle;

public class DemoActivity extends AppCompatActivity {

    public static final String KEY_IS_FITS_SYSTEM_BARS = "IS_FITS_SCREEN";
    public static final String KEY_IS_FULL_SCREEN = "IS_FULL_SCREEN";
    private static final String KEY_FRAGMENT_TAG = "FRAGMENT_NUM";
    private ActivityDemoBinding mBinding;
    private FullScreenController mFullScreenController;
    private FullScreenHelper mFullScreenHelper;
    private FragmentManager mFragmentManager;
    @Nullable private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        mBinding.backButton.setOnClickListener(v -> returnPreviousFragment());
        mBinding.forwardButton.setOnClickListener(v -> loadNextFragment());

        mFullScreenController = new FullScreenController(this);
        mFullScreenController.getFullScreenState().observe(this, this::onFullScreenStateChanged);

        mFullScreenHelper = new FullScreenHelper(KEY_IS_FITS_SYSTEM_BARS, KEY_IS_FULL_SCREEN, mFullScreenController);
        mFullScreenHelper.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
                mCurrentFragment = f;
                processFragmentTitle(f);
                processFragmentFullScreenFlag(f, savedInstanceState);
            }
        }, false);


        if (null == savedInstanceState)
            loadFirstFragment();
        else {
            if (FirstFragment.class.getSimpleName().equals(savedInstanceState.getString(KEY_FRAGMENT_TAG)))
                loadFirstFragment();
            else
                loadSecondFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mFullScreenHelper.onSaveInstanceState(outState);

        if (null != mCurrentFragment)
            outState.putString(KEY_FRAGMENT_TAG, mCurrentFragment.getClass().getSimpleName());
    }

    @Override
    public boolean onSupportNavigateUp() {
        loadFirstFragment();
        return true;
    }

    private void processFragmentTitle(Fragment fragment) {
        if (fragment instanceof HasCustomTitle)
            setTitle(((HasCustomTitle) fragment).getTitle());
        else
            setTitle(R.string.app_name);
    }


    private void processFragmentFullScreenFlag(Fragment fragment, @Nullable Bundle savedInstanceState) {
        final boolean fitSystemBars = !(fragment instanceof FullScreenableFragment);
        mFullScreenController.setFitToSystemBars(fitSystemBars);
    }


    private void onFullScreenStateChanged(FullScreenState fullScreenState) {

        if (fullScreenState.isFitsSystemWindows || fullScreenState.isFullScreen) {
            ViewMarginsAdjustmentHelper.adjustTopMarginTo(mBinding.toolbar, getDimen(R.dimen.fab_margin));

            ViewMarginsAdjustmentHelper.adjustBottomMarginTo(mBinding.forwardButton, getDimen(R.dimen.fab_margin));
            ViewMarginsAdjustmentHelper.adjustRightMarginTo(mBinding.forwardButton, getDimen(R.dimen.fab_margin));

            ViewMarginsAdjustmentHelper.adjustBottomMarginTo(mBinding.backButton, getDimen(R.dimen.fab_margin));
            ViewMarginsAdjustmentHelper.adjustLeftMarginTo(mBinding.backButton, getDimen(R.dimen.fab_margin));
        }
        else {
            ViewMarginsAdjustmentHelper.adjustTopMarginToInsets(mBinding.toolbar, fullScreenState.insets, 0);

            ViewMarginsAdjustmentHelper.adjustBottomMarginToInsets(mBinding.forwardButton, fullScreenState.insets, getDimen(R.dimen.fab_margin));
            ViewMarginsAdjustmentHelper.adjustRightMarginToInsets(mBinding.forwardButton, fullScreenState.insets, getDimen(R.dimen.fab_margin));

            ViewMarginsAdjustmentHelper.adjustBottomMarginToInsets(mBinding.backButton, fullScreenState.insets, getDimen(R.dimen.fab_margin));
            ViewMarginsAdjustmentHelper.adjustLeftMarginToInsets(mBinding.backButton, fullScreenState.insets, getDimen(R.dimen.fab_margin));
        }

        if (fullScreenState.isFullScreen) {
            hideToolbar();
        }
        else {
            showToolbar();
        }
    }

    private void showToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.show();
    }

    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.hide();
    }


    private void loadNextFragment() {
        if (mCurrentFragment instanceof FirstFragment)
            loadSecondFragment();
    }

    private void returnPreviousFragment() {
        mFragmentManager.popBackStack();
    }

    private void loadFirstFragment() {
        setFragment(FirstFragment.create());
    }

    private void loadSecondFragment() {
        loadFragment(SecondFragment.create());
    }


    private void loadFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
    }

    private void setFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void toggleFullScreen() {
        mFullScreenController.toggleFullScreen();
    }

    public void toggleFitsSystemBars() {
        mFullScreenController.toggleFitSystemBars();
    }

    public void toggleScreen() {
//        toggleFitsSystemBars();
        toggleFullScreen();
    }

    private int getDimen(@DimenRes int dimenRes) {
        return (int) getResources().getDimension(dimenRes);
    }

}