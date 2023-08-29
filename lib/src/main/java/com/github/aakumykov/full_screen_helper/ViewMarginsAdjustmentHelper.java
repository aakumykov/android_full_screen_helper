 package com.github.aakumykov.full_screen_helper;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;

public class ViewMarginsAdjustmentHelper {

    public static void adjustTopMarginToInsets(View view, Insets insets, int selfMargin) {
        adjustTopMarginTo(view, insets.top + selfMargin);
    }

    public static void adjustTopMarginTo(View view, int topMarginValue) {
        ViewGroup.MarginLayoutParams mlp = marginLayoutParams(view);
        mlp.topMargin = topMarginValue;
        view.setLayoutParams(mlp);
    }


    public static void adjustBottomMarginToInsets(View view, Insets insets, int selfMargin) {
        adjustBottomMarginTo(view, insets.bottom + selfMargin);
    }

    public static void adjustBottomMarginTo(View view, int bottomMarginValue) {
        ViewGroup.MarginLayoutParams mlp = marginLayoutParams(view);
        mlp.bottomMargin = bottomMarginValue;
        view.setLayoutParams(mlp);
    }


    public static void adjustRightMarginToInsets(View view, Insets insets, int selfMargin) {
        adjustRightMarginTo(view, insets.right + selfMargin);
    }

    public static void adjustRightMarginTo(View view, int rightMarginValue) {
        ViewGroup.MarginLayoutParams mlp = marginLayoutParams(view);
        mlp.rightMargin = rightMarginValue;
        view.setLayoutParams(mlp);
    }


    public static void adjustLeftMarginToInsets(View view, Insets insets, int selfMargin) {
        adjustLeftMarginTo(view, insets.left + selfMargin);
    }

    public static void adjustLeftMarginTo(View view, int leftMarginValue) {
        ViewGroup.MarginLayoutParams mlp = marginLayoutParams(view);
        mlp.leftMargin = leftMarginValue;
        view.setLayoutParams(mlp);
    }


    private static ViewGroup.MarginLayoutParams marginLayoutParams(View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        return  (ViewGroup.MarginLayoutParams) lp;
    }
}
