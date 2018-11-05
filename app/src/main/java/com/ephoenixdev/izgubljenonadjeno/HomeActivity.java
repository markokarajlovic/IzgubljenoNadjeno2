package com.ephoenixdev.izgubljenonadjeno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                case R.id.navigation_lost:
                    fragment = new LostFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                case R.id.navigation_found:
                    fragment = new FoundFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

}
