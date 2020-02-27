package com.example.rotba.check;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CollapsingToolbarLayout layout = findViewById(R.id.collapsing_toolbar_layout);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);
        startInitialActivity();
    }

    private void startInitialActivity() {
        startActivity(State.getState().getInitialActivityIntent(this));
    }
}
