package com.ray.collectify.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.collectify.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * This fragment displays the about page
 */
public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);


    }

}
