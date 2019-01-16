package com.ray.collectify.activity;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ray.collectify.R;
import com.ray.collectify.fragment.AboutFragment;
import com.ray.collectify.fragment.CollectionFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * This class is responsible for handling the bottom navigation view and switching between fragments based on nav selections.
 */
public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private List<Fragment> fragments = new ArrayList<>(2);

    private static final String TAG_FRAGMENT_COLLECTION = "tag_frag_collection";
    private static final String TAG_FRAGMENT_ABOUT = "tag_frag_about";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_collection:
                    switchFragment(0, TAG_FRAGMENT_COLLECTION);
                    return true;
                case R.id.navigation_about:
                    switchFragment(1, TAG_FRAGMENT_ABOUT);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // app theme must be set as the theme is set to the splash screen initially
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buildFragmentsList();

        switchFragment(0, TAG_FRAGMENT_COLLECTION);

    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        CollectionFragment collectionFragment = buildCollectionFragment();
        AboutFragment aboutFragment = buildAboutFragment();

        fragments.add(collectionFragment);
        fragments.add(aboutFragment);
    }

    private CollectionFragment buildCollectionFragment() {
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private AboutFragment buildAboutFragment() {
        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


}
