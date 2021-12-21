package smartphone.csandaz.evanjelickyspevnik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemPickerFragment.NoticeDialogListener {

    private TextView songDisplay;
    private EditText songNumber;
    private TextView label;
    private RelativeLayout alternativeSongPicker;
    public static SongBook SB;
    private LinearLayout mainLayout;
    private Boolean nightMode = false;
    private TextWatcher myTextWatcher;

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ItemPickerFragment();
        dialog.show(getSupportFragmentManager(), "ItemPickerFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onClick(int i) {
        SB.selectedBook.selectedSong = i+1;
        setSongNumber();
        showSong();
    }

    public void setSongNumber(){
    songNumber.removeTextChangedListener(myTextWatcher);
    songNumber.setText(Integer.toString(SB.selectedBook.selectedSong));
    songNumber.addTextChangedListener(myTextWatcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SB = new SongBook();

        songDisplay = (TextView) findViewById(R.id.textView);

        alternativeSongPicker = (RelativeLayout) findViewById(R.id.alternative_songpicker);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        label = (TextView) findViewById(R.id.textView2);

        songNumber=(EditText)findViewById(R.id.song_number);

        songNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                songNumber.setText("");
                return false;
            }
        });

        songDisplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });

        myTextWatcher = new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if(songNumber.getText().length()>0) {
                    int i = Integer.parseInt(songNumber.getText().toString());
                    SB.selectedBook.selectedSong = i;
                    showSong();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        };

        final EditText songNumber = (EditText)findViewById(R.id.song_number);
        songNumber.addTextChangedListener(myTextWatcher);

        showSong();
        setTitle(SB.selectedBook.title);
        alternativeSongPicker.setVisibility(SB.selectedBook.alternateSongPicker_Visibility);
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
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setColor();
            return true;
        }

        if (id == R.id.action_mute) {
            muteSounds();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setColor(){
        nightMode = !nightMode;
        if (nightMode) {
            mainLayout.setBackgroundColor(Color.BLACK);
            songDisplay.setTextColor(Color.WHITE);
            label.setTextColor(Color.WHITE);
            songNumber.setTextColor(Color.WHITE);
        }
        else{
            mainLayout.setBackgroundColor(Color.WHITE);
            songDisplay.setTextColor(Color.BLACK);
            label.setTextColor(Color.BLACK);
            songNumber.setTextColor(Color.BLACK);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_antifonyy)       changeBook(SB.Antifony);
        else if (id == R.id.nav_piesne)     changeBook(SB.Spevnik);
        else if (id == R.id.nav_pasie)      changeBook(SB.Pasie);
        else if (id == R.id.nav_funebral)   changeBook(SB.Funebral);
        else if (id == R.id.nav_funebral_antifony) changeBook(SB.Funebral_Antifony);
        else if (id == R.id.nav_predspevy)  changeBook(SB.Predspevy);
        else if (id == R.id.nav_liturgia)  changeBook(SB.Liturgia);
        else if (id == R.id.nav_vecera_panova)  changeBook(SB.Vecera_Panova);
        else if (id == R.id.nav_info)  changeBook(SB.Info);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void muteSounds(){
        AudioManager am;
        am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    public void showSong() {
        AssetManager am = this.getAssets();
        InputStream inputStream = null;
        String songFile = null;

        songFile = SB.selectedBook.getSongPath(SB.selectedBook.selectedSong);

        try { inputStream = am.open(songFile);
        }
        catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Chyba")
                    .setMessage("Zvolená pieseň sa nenašla.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        InputStreamReader inputreader = null;
        try {
            inputreader = new InputStreamReader(inputStream, "UTF_16");
        } catch (UnsupportedEncodingException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Chyba")
                        .setMessage("Nesprávne kódovanie textu")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            return;
        }
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
        }
        songDisplay.setText(text);

    }

    public void changeBook(BookType book)
    {
        SB.selectedBook = book;
        setTitle(SB.selectedBook.title);
        alternativeSongPicker.setVisibility(SB.selectedBook.alternateSongPicker_Visibility);
        setSongNumber();

        showSong();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
