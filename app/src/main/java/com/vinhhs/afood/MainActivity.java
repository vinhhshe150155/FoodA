package com.vinhhs.afood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.vinhhs.afood.fragment.HomeFragment;
import com.vinhhs.afood.fragment.LanguageFragment;
import com.vinhhs.afood.fragment.ListRecipeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_SETTINGS = 3;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_LIST_RECIPE = 1;
    private static final int FRAGMENT_SIGNOUT = 2;
    private int currentFragment = FRAGMENT_HOME;
    private Toolbar toolbar;
    private TextView tvUsername;
    private TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        showUser();
        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.home).setChecked(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        return true;
    }

    private void initUI() {
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_main);
        navigationView.setNavigationItemSelectedListener(this);
//        toolbar = findViewById(R.id.toolbar);
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_header_nav, null, false);
        View headerView = navigationView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.drawerlayout_tv_username);
        tvEmail = headerView.findViewById(R.id.drawerlayout_tv_email);
//        toolbar.setTitle("");
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            if (currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.list_saved) {
            if (currentFragment != FRAGMENT_LIST_RECIPE) {
                replaceFragment(new ListRecipeFragment());
                currentFragment = FRAGMENT_LIST_RECIPE;
            }
        } else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.settíngs) {
            if (currentFragment != FRAGMENT_SETTINGS) {
                replaceFragment(new LanguageFragment());
                currentFragment = FRAGMENT_SETTINGS;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_replace, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void showUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        tvUsername.setText(name);
        tvEmail.setText(email);
    }
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
