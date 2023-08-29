package com.github.aakumykov.full_screen_helper;

import androidx.core.graphics.Insets;

public class FullScreenState {

    public final boolean isFitsSystemWindows;
    public final boolean isFullScreen;
    public final boolean statusBarVisible;
    public final boolean navigationBarVisible;
    public final Insets insets;

    public FullScreenState(boolean isFitsSystemWindows, boolean isFullScreen,
                           boolean statusBarVisible, boolean navigationBarVisible,
                           Insets insets
    ) {
        this.isFitsSystemWindows = isFitsSystemWindows;
        this.insets = insets;
        this.isFullScreen = isFullScreen;
        this.statusBarVisible = statusBarVisible;
        this.navigationBarVisible = navigationBarVisible;
    }
}
