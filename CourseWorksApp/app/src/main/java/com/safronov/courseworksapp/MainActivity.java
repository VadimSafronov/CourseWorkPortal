package com.safronov.courseworksapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.safronov.courseworksapp.Fragments.CourseWorksFragment;
import com.safronov.courseworksapp.Fragments.WorksDetailFragment;
import com.safronov.courseworksapp.Helpers.DatabaseContext;
import com.safronov.courseworksapp.Helpers.DatabaseOperations;
import com.safronov.courseworksapp.Helpers.NetworkHelper;
import com.safronov.courseworksapp.Helpers.SnackbarHelper;
import com.safronov.courseworksapp.Models.CourseWork;
import com.safronov.courseworksapp.Models.FavoriteWork;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_PERMISSIONS = 200;
    public static Context app_context;
    private String portal_url;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CourseWorksFragment works_fragment;
    private WorksDetailFragment detail_fragment;
    private FragmentManager fragmentManager;
    private DatabaseOperations dbOp;
    private String currentWF = "allCourseWorks";
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
           //Screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        app_context = getApplicationContext();
        portal_url = getResources().getString(R.string.portal_url);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();

        works_fragment = new CourseWorksFragment();
        detail_fragment = new WorksDetailFragment();
        fragmentManager = getSupportFragmentManager();
        dbOp = new DatabaseOperations(this);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorLightRed);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView);
        navigationView.setNavigationItemSelectedListener(this);
        navUsername.setText("@" + dbOp.getUsername());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        updateCourseWorks(true);
        updateFavWorks(false);
    }

    @Override
    public void onRefresh() {
        if (NetworkHelper.isNetConn(this)) {
            if (!detail_fragment.isAdded()) {
                if (currentWF.equals("allCourseWorks")) updateCourseWorks(true);
                else if (currentWF.equals("allFavWorks")) updateFavWorks(true);
            } else mSwipeRefreshLayout.setRefreshing(false);
        } else {
            SnackbarHelper.showStaticSnackbar(findViewById(R.id.container), this);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_allWorks) {
            currentWF = "allCourseWorks";
//            if (!works_fragment.isAdded()) openFragment(works_fragment);
//            works_fragment.initializeData(dbOp.getCourseWorks());
//            works_fragment.notifyChanges();
            updateCourseWorks(true);
        } else if (id == R.id.nav_favorite) {
            currentWF = "allFavWorks";
//            if (!works_fragment.isAdded()) openFragment(works_fragment);
//            works_fragment.initializeData(dbOp.getFavWorks());
//            works_fragment.notifyChanges();
            updateFavWorks(true);
        } else if (id == R.id.nav_exit) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transact = fragmentManager.beginTransaction();
        transact.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transact.replace(R.id.container, fragment);
        transact.addToBackStack(null);
        transact.commit();
    }

    public void updateCourseWorks(final boolean withAddingFragment) {
        String url = portal_url + "/api/CourseWorks/Get";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<List<CourseWork>>() {
                        }.getType();
                        List<CourseWork> courseWorks = new Gson().fromJson(response, listType);

                        // обновляем данные в базе
                        dbOp.dropTable(DatabaseContext.T_COURSEWORKS);
                        dbOp.insertCourseWorks(courseWorks);

                        // оповещаем об изменениях и, если не добавлен фрагмент, - добавляем
                        if (withAddingFragment) {
                            works_fragment.initializeData(dbOp.getCourseWorks());
                            if (!works_fragment.isAdded()) openFragment(works_fragment);
                            else works_fragment.notifyChanges();
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + dbOp.getToken());
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void updateFavWorks(final boolean withAddingFragment) {
        String url = portal_url + "/api/FavoriteWorks/Get";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<List<FavoriteWork>>() {
                        }.getType();
                        List<FavoriteWork> favoriteWorks = new Gson().fromJson(response, listType);

                        // обновляем данные в базе
                        dbOp.dropTable(DatabaseContext.T_FAVWORKS);
                        dbOp.insertFavWorks(favoriteWorks);

                        // оповещаем об изменениях и, если не добавлен фрагмент, - добавляем
                        if (withAddingFragment) {
                            works_fragment.initializeData(dbOp.getFavWorks());
                            if (!works_fragment.isAdded()) openFragment(works_fragment);
                            else works_fragment.notifyChanges();
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + dbOp.getToken());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }
}
