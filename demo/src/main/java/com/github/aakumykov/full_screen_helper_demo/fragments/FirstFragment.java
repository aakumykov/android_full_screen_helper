package com.github.aakumykov.full_screen_helper_demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.aakumykov.full_screen_helper_demo.DemoActivity;
import com.github.aakumykov.full_screen_helper_demo.R;
import com.github.aakumykov.full_screen_helper_demo.interfaces.HasCustomTitle;


public class FirstFragment extends Fragment implements HasCustomTitle {

    public static FirstFragment create() {
        return new FirstFragment();
    }

    @Override
    public int getTitle() {
        return R.string.FRAGMENT_FIRST_title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        view.findViewById(R.id.imageView).setOnClickListener(v -> {
            ((DemoActivity) requireActivity()).toggleScreen();
        });

        return view;
    }
}
