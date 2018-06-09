package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MenuItem menuSetting;  //icon chọn từ điển E-V, V-E
    Toolbar toolbar;

    DBHelper dbHelper;

    DictionaryFragment dictionaryFragment;
    BookmarkFragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Apply SQLite
        dbHelper = new DBHelper(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        dictionaryFragment = new DictionaryFragment();
        bookmarkFragment = BookmarkFragment.getNewIntance(dbHelper);
        goToFragment(dictionaryFragment, true);


        //khi nghe sự kiện click button thì tới detailFragment
        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this, "dic_type");
                int dicType = id == null ? R.id.action_ev : Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value, dbHelper, dicType), false);
            }
        });

        //khi nghe sự kiện click button thì tới detailFragment
        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this, "dic_type");
                int dicType = id == null ? R.id.action_ev : Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value, dbHelper, dicType), false);
            }
        });


        // Filter
        EditText edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dictionaryFragment.filterValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //
        menuSetting = menu.findItem(R.id.action_settings);

        // Lưu lại trạng thái từ điển đã chọn
        String id = Global.getState(this, "dic_type");
        if (id != null)
            onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
        else {

            // Apply SQLite
            ArrayList<String> source = dbHelper.getWord(R.id.action_ev);
            dictionaryFragment.resetDataSource(source);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            int id = item.getItemId();
            if (R.id.action_settings == id) return true;
            Global.saveState(this, "dic_type", String.valueOf(id));
            ArrayList<String> source = dbHelper.getWord(id);
            // Đổi icon và lấy DB tương ứng khi chọn từ điển E-V, V-E
            if (id == R.id.action_ev) {
                dictionaryFragment.resetDataSource(source);
                menuSetting.setIcon(getDrawable(R.drawable.ev_white));
            } else if (id == R.id.action_ve) {
                dictionaryFragment.resetDataSource(source);
                menuSetting.setIcon(getDrawable(R.drawable.ve_white));
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //
        if (id == R.id.nav_bookmark) {
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
                goToFragment(bookmarkFragment, false);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //
    void goToFragment(android.support.v4.app.Fragment fragment, boolean isTop) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    // Thay đổi menu của activity Bookmark
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        if (activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
            menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle("Bookmark");
        } else {
            menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
            toolbar.setTitle("");
        }
        return true;
    }
}
