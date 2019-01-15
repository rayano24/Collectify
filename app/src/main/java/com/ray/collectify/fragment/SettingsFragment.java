package com.ray.collectify.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.collectify.R;

import androidx.fragment.app.Fragment;

/**
 * This page allows the user to sign out, change their location, or view the help menu
 */
public class SettingsFragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // main view elements



        return rootView;
    }




}
