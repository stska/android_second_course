package com.example.weather_app_drawer_second_java;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.example.weather_app_drawer_second_java.weatherApp.CityWeatherDescription;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example2;
import com.example.weather_app_drawer_second_java.weatherApp.JsonForecastClasses.Sys;
import com.example.weather_app_drawer_second_java.weatherApp.SingltoneListOfCities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SingltoneListOfCities singltoneListOfCities;
    private FragmentActivity myContext;


    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            singltoneListOfCities = SingltoneListOfCities.getInstance(getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            assert query != null;
            Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();
        }

        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (compareQueryToList(s, singltoneListOfCities.example2)) {
                    goTo(s);
                    clearTab(searchView);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }

        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                Cursor cursor = (Cursor) mAdapter.getItem(i);
                String temp = cursor.getString(i);
                System.out.println(temp);
                if(cursor != null && cursor.moveToFirst()){
                    goTo(cursor.getString(i));
                }
                clearTab(searchView);
                return true;
            }
        });
        return true;
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});

        for (int i = 0; i < singltoneListOfCities.getListOfCities().size(); i++) {
            if (singltoneListOfCities.getListOfCities().get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, singltoneListOfCities.getListOfCities().get(i)});
        }
        mAdapter.changeCursor(c);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean compareQueryToList(String query, Example2[] example2) {

        for (int i = 0; i < example2.length; i++) {
            if (example2[i].getName().equals(query)) {
                return true;
            }
        }
        return false;
    }

    private void goTo(String city) {
        CityWeatherDescription cwd = CityWeatherDescription.newInstance(city);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.initialFramLayoutId, cwd)
                .addToBackStack(cwd.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            recreate();     //если вы это видите, то подскажите насколько опасно так делать? незнал, как еще можно перерисовать активити не делая лисенеров и.т.д, так как уже время поздно.
        } else {
            getFragmentManager().popBackStack();
        }
    }
    public void clearTab(SearchView searchView){
        searchView.clearFocus();
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }
}