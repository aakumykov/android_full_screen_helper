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
import com.github.aakumykov.full_screen_helper_demo.interfaces.FullScreenableFragment;
import com.github.aakumykov.full_screen_helper_demo.interfaces.HasCustomTitle;

public class SecondFragment extends Fragment implements
        HasCustomTitle
        , FullScreenableFragment
{
    public static SecondFragment create() {
        return new SecondFragment();
    }

    @Override
    public int getTitle() {
        return R.string.FRAGMENT_SECOND_title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        view.findViewById(R.id.imageView).setOnClickListener(v -> {
            ((DemoActivity) requireActivity()).toggleScreen();
        });
        return view;
    }
}
