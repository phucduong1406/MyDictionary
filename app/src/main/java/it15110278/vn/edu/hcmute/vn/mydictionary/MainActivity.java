package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MenuItem menuSetting;  //icon chọn từ điển E-V, V-E
    Toolbar toolbar;

    DBHelper dbHelper;

    DictionaryFragment dictionaryFragment;
    BookmarkFragment bookmarkFragment;
    DetailFragment detailFragment;

    ImageButton btnHear;

    TextView textWord;
    TextToSpeech toSpeech;

    Dialog dialog;

    int flagLang;  // cờ chọn từ điển E-V, V-E

    RadioButton radioEng, radioVie;
    LanguageDialog languageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Apply SQLite
        dbHelper = new DBHelper(this);


        // Add the button that opens the navigation drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        dictionaryFragment = new DictionaryFragment();
        bookmarkFragment = BookmarkFragment.getNewIntance(dbHelper);
        goToFragment(dictionaryFragment, true);


        // Khi nghe sự kiện click button thì tới detailFragment
        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this, "dic_type");
                int dicType = id == null ? R.id.action_ev : Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value, dbHelper, dicType), false);
            }
        });
        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this, "dic_type");
                int dicType = id == null ? R.id.action_ev : Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value, dbHelper, dicType), false);
            }
        });


        // Filter: Tìm kiếm từ
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

        // Change languege
//        radioEng = findViewById(R.id.radioEng);
//        radioVie = findViewById(R.id.radioVie);
//        if (radioEng.isChecked()) {
//            changeLang("en");
//        }
//        if (radioVie.isChecked()) {
//            changeLang("vi");
//        }
    }

    // Khi nhấn vào btnHear
    public void hearPronunciation(View view) {
        textWord = findViewById(R.id.textWord);
        btnHear = findViewById(R.id.btnVolume);
        btnHear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String s = textWord.getText().toString();
                toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i != TextToSpeech.ERROR) {
                            if (flagLang == 0) {
                                toSpeech.setLanguage(Locale.ENGLISH);
                            } else if (flagLang == 1) {
                                toSpeech.setLanguage(Locale.forLanguageTag("vi-VN"));
                            }
                            toSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }
        });
    }


    // Thay đổi ngôn ngữ
    public void changeLang(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();
        config.locale = locale;

        // Cập nhật lạo ngôn ngữ
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent();
        startActivity(intent);
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

                flagLang = 0;
            } else if (id == R.id.action_ve) {
                dictionaryFragment.resetDataSource(source);
                menuSetting.setIcon(getDrawable(R.drawable.ve_white));

                flagLang = 1;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_bookmark) {
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
                goToFragment(bookmarkFragment, false);
            }
        } else if (id == R.id.nav_lang) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_language);
            dialog.show();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Thêm fragment trong layout
    void goToFragment(android.support.v4.app.Fragment fragment, boolean isTop) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();  // lấy ra một đối tượng FragmentManager
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  // Chuyển giữa các fragment đẹp hơn

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
