package com.github.aakumykov.full_screen_helper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Задача класса - сохранять состояние вписанности Activity (точнее, Window) в системные панели
 * и восстанавливать его после пересоздания. Для этой цели используется Bundle (из savedInstanceState).
 */
public class FullScreenHelper {

    private final String mKeyIsFitsSystemBars;
    private final String mKeyIsFullScreen;
    private final FullScreenController mFullScreenController;

    //
    // Для того, чтобы ключи для Bundle были явными, а не скрывались в этом классе,
    // передаю их через конструктор.
    //
    public FullScreenHelper(String keyIsFitsSystemBars, String keyIsFullScreen,
                            FullScreenController fullScreenController) {
        mKeyIsFitsSystemBars = keyIsFitsSystemBars;
        mKeyIsFullScreen = keyIsFullScreen;
        mFullScreenController = fullScreenController;
    }

    //
    // Метод должен быть вызван в одноимённом методе Activity.
    //
    public void onCreate(@Nullable Bundle savedState) {
//        mFullScreenController.setFitToSystemBars((null == savedState) || savedState.getBoolean(mKeyIsFitsSystemBars, true));

        final boolean wasFullScreen = (null != savedState && savedState.getBoolean(mKeyIsFullScreen, false));
        mFullScreenController.setFullScreen(wasFullScreen);
    }

    //
    // Метод должен быть вызван в одноимённом методе Activity.
    //
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(mKeyIsFitsSystemBars, mFullScreenController.isFitsSystemBars());
        outState.putBoolean(mKeyIsFullScreen, mFullScreenController.isFullScreen());
    }

}
