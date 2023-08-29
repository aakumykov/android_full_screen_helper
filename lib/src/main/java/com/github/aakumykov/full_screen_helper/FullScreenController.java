package com.github.aakumykov.full_screen_helper;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FullScreenController implements View.OnApplyWindowInsetsListener {

    //
    // Log tag.
    //
    private static final String TAG = FullScreenController.class.getSimpleName();

    //
    // Controller to change Window insets and hide toolbars.
    //
    @Nullable private final WindowInsetsControllerCompat mWindowInsetsControllerCompat;

    //
    // Флаг для отслеживания состояния списанности окна в системные панели.
    // Необходим потому, что метод onApplyWindowInsets() вызывается только один раз, что
    // не позволяет динамически определять состояние.
    // По умолчанию окно приложения вписано в системные панели.
    //
    private boolean mIsFittedSystemBars = true;
    private boolean mIsFullScreen = false;


    //
    // Окно Activity-пользователя этого класса.
    //
    private final Window mWindow;


    private final MutableLiveData<FullScreenState> mScreenParamsMutableLiveData =
            new MutableLiveData<>();


    /**
     * Конструктор.
     * @param fragmentActivity Пусть вас не смущает FragmentActivity: это предок
     *                         AppCompatActivity. Нужен именно этот класс, потому
     *                         что его возвращает метод {@link androidx.fragment.app.Fragment#requireActivity()} во фрагменте.
     */
    public FullScreenController(@NonNull FragmentActivity fragmentActivity) {
        mWindow = fragmentActivity.getWindow();
        final View decorView = mWindow.getDecorView();
        decorView.setOnApplyWindowInsetsListener(this);
        mWindowInsetsControllerCompat = WindowCompat.getInsetsController(mWindow, decorView);
        mWindowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }


    @NonNull @Override
    public WindowInsets onApplyWindowInsets(@NonNull View v, @NonNull WindowInsets windowInsets) {

        final WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets);
        final Insets insets = windowInsetsCompat.getInsets(systemBarsMask());
        final boolean statusBarVisible = windowInsetsCompat.isVisible(statusBarMask());
        final boolean navBarVisible = windowInsetsCompat.isVisible(navigationBarMask());

        /* Здесь нужно использовать setValue(), при использовании postValue()
           панель инструментов дёргается при смене фрагмента. Вероятно, это происходит
           из-за задержки, так как postValue() выполняется асинхронно, с задержкой.*/
        mScreenParamsMutableLiveData.setValue(new FullScreenState(
                mIsFittedSystemBars,
                mIsFullScreen,
                statusBarVisible,
                navBarVisible,
                insets
        ));

        return v.onApplyWindowInsets(windowInsets);
    }


    public LiveData<FullScreenState> getFullScreenState() {
        return mScreenParamsMutableLiveData;
    }


    public void toggleFitSystemBars() {
        setFitToSystemBars(!mIsFittedSystemBars);
    }

    public void setFitToSystemBars(boolean isFitting) {
        if (insetsControllerNotReady()) return;
        WindowCompat.setDecorFitsSystemWindows(mWindow, isFitting);
        mIsFittedSystemBars = isFitting;
    }


    public void toggleFullScreen() {
        setFullScreen(!mIsFullScreen);
    }

    public void setFullScreen(boolean shouldBeFullScreen) {
        if (null == mWindowInsetsControllerCompat)
            return;

        if (shouldBeFullScreen)
            mWindowInsetsControllerCompat.hide(systemBarsMask());
        else
            mWindowInsetsControllerCompat.show(systemBarsMask());

        // По идее, этот флаг должен устанавливаться в методе onApplyWindowInsets(), но тот
        // вызывается лишь единожды.
        mIsFullScreen = shouldBeFullScreen;
    }


    public boolean isFullScreen() {
        return mIsFullScreen;
    }

    public boolean isFitsSystemBars() {
        return mIsFittedSystemBars;
    }



    public void setLightSystemBars() {
        if (null != mWindowInsetsControllerCompat)
            mWindowInsetsControllerCompat.setAppearanceLightStatusBars(true);
    }

    public void setDarkSystemBars() {
        if (null != mWindowInsetsControllerCompat)
            mWindowInsetsControllerCompat.setAppearanceLightStatusBars(false);
    }


    private int systemBarsMask() {
        return WindowInsetsCompat.Type.systemBars();
    }

    private int statusBarMask() {
        return WindowInsetsCompat.Type.statusBars();
    }

    private int navigationBarMask() {
        return WindowInsetsCompat.Type.navigationBars();
    }


    private boolean insetsControllerNotReady() {
        if (null == mWindowInsetsControllerCompat) {
            Log.e(TAG, "WindowInsetsCompat is null");
            return true; }
        return false;
    }
}
