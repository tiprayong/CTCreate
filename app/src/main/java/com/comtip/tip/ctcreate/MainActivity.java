package com.comtip.tip.ctcreate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {
    //Class
    public CritiqueClass mCritique;
    public CritiquePresenter critiquePresenter;
    public ArchivePresenter archivePresenter;

    //Database
    public SQLiteDatabase mSqLiteDatabase;
    public CritiqueDatabaseHelper critiqueDatabaseHelper;


    // Widgets
    public Button saveBT;
    public RecyclerView critiqueRV;
    private EditText keywordET;
    private Button archiveBT;
    private Button createBT;

    //InstanceState
    private Parcelable wrapped;
    public boolean isArchive = false;

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSQLite();
        setupWidgets();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void setupSQLite() {
        critiqueDatabaseHelper = new CritiqueDatabaseHelper(this);
        mSqLiteDatabase = critiqueDatabaseHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        critiqueDatabaseHelper.close();
        mSqLiteDatabase.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isArchive", isArchive);
        if (isArchive) {
            if (mCritique != null) {
                wrapped = Parcels.wrap(mCritique);
                outState.putParcelable("wrapped", wrapped);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isArchive = savedInstanceState.getBoolean("isArchive", false);
        if (isArchive) {
            wrapped = savedInstanceState.getParcelable("wrapped");
            mCritique = new CritiqueClass();
            mCritique = Parcels.unwrap(wrapped);
            archivePresenter.archiveCriticalThinkingCreate(mCritique.get_keyword());
        }
    }

    private void setupWidgets() {
        critiquePresenter = new CritiquePresenter(this);
        critiquePresenter.setupChrome();
        archivePresenter = new ArchivePresenter(this);

        keywordET = (EditText) findViewById(R.id.keywordET);

        critiqueRV = (RecyclerView) findViewById(R.id.critiqueRV);

        createBT = (Button) findViewById(R.id.createBT);
        createBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keywordET.getText().toString().isEmpty()) {
                     //Empty
                } else {

                    if (critiqueDatabaseHelper.inArchive(mSqLiteDatabase, keywordET.getText().toString())) {
                        customSnackBar("Topic Archive !!!");
                    } else {
                        //Hide Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                        critiquePresenter.criticalThinkingCreate(keywordET.getText().toString());

                        keywordET.setText("");
                    }
                }
            }
        });

        saveBT = (Button) findViewById(R.id.saveBT);
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Check  insert or update Database.
                if (isArchive) {

                    AlertDialog.Builder alertUpdate = new AlertDialog.Builder(MainActivity.this);
                    alertUpdate.setTitle("Update " + mCritique.get_keyword() + " ?");

                    alertUpdate.setPositiveButton("✔ YES ✔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            critiqueDatabaseHelper.updateCritique(mSqLiteDatabase, mCritique, archivePresenter.tempKeyword);
                            customSnackBar("Update "+mCritique.get_keyword());
                        }
                    });

                    alertUpdate.setNegativeButton("✘ NO ✘", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel
                        }
                    });
                    AlertDialog alertU = alertUpdate.create();
                    alertU.show();

                } else {

                    AlertDialog.Builder alertSave = new AlertDialog.Builder(MainActivity.this);
                    alertSave.setTitle("Save " + mCritique.get_keyword() + " ?");
                    alertSave.setPositiveButton("✔ YES ✔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            critiqueDatabaseHelper.addCritique(mSqLiteDatabase, mCritique);
                            isArchive = true;
                            saveBT.setText("update");
                            archivePresenter.tempKeyword = mCritique.get_keyword();
                            customSnackBar("Add "+mCritique.get_keyword());
                        }
                    });

                    alertSave.setNegativeButton("✘ NO ✘", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel
                        }
                    });
                    AlertDialog alertS = alertSave.create();
                    alertS.show();

                }

            }
        });
        saveBT.setEnabled(false);

        archiveBT = (Button) findViewById(R.id.archiveBT);
        archiveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archivePresenter.setupArchiveRV();
                keywordET.setText("");
            }
        });
    }

    /*--------------------------------------------------------------------------------------
      Method: customSnackBar (String message)
      Description:   Pop up  Activity Message.
    -------------------------------------------------------------------------------------*/
    public void customSnackBar(String message) {
        snackbar = Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).setAction("✘", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.WHITE);
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.BLUE);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show();
    }

}
