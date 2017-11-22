package com.example.wustls14.dy_beacon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wustls14.dy_beacon.util.DBHelper;
import com.example.wustls14.dy_beacon.util.U;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class Register_Beacon_Activity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    //Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;
    // A dummy authentication store containing known user names and passwords.
    // TODO: remove after connecting to a real authentication system.
    private static final String[] DUMMY_CREDENTIALS = new String[]{"foo@example.com:hello", "bar@example.com:world"};
    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    private static String DATABASE_NAME = "DY_Beacon_DB";
    private static String TABLE_NAME = "registered_Info_Table";
    private static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    DBHelper helper;
    private SQLiteDatabase db;

    // UI references.
    private AutoCompleteTextView beaconNameView;
    private EditText srlNoView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner s;

    // 필요변수 선언
    String beaconName;
    String temp_srlNo;
    int srlNo = 0;
    String distance;
    int distance_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_beacon);
        // Set up the login form.
        beaconNameView = (AutoCompleteTextView) findViewById(R.id.beaconName_txt);
        populateAutoComplete();

        srlNoView = (EditText) findViewById(R.id.srlNo_txt);
        srlNoView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.register_btn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                beforeLoginCheck();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // 알람 거리 설정하는 스피너 ===============================================================
        s = (Spinner)findViewById(R.id.distance_spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(s.getSelectedItemPosition()>=1){

                    distance = s.getSelectedItem().toString();
                    // 선택된 값의 순서를 저장
                    distance_number = s.getSelectedItemPosition();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // === 로그인 =================================================================================

    // 로그인 전 입력된 값이 적절한지 체크하는 메소드
    private void beforeLoginCheck() {

        boolean cancel = false;
        View focusView = null;

        // 창에 입력된 값 받아오기
        beaconName = beaconNameView.getText().toString();
        temp_srlNo = srlNoView.getText().toString();

        // beaconName과 X, srlNo X -> beaconName에 포커스
        if (TextUtils.isEmpty(beaconName) && TextUtils.isEmpty(temp_srlNo)) {
            beaconNameView.setError(getString(R.string.error_field_required));
            focusView = beaconNameView;
            beaconNameView.requestFocus();
            cancel = true;
        }
        // beaconName O srlNo X -> srlNO에 포커스
        else if (!(TextUtils.isEmpty(beaconName)) && TextUtils.isEmpty(temp_srlNo)) {
            srlNoView.setError(getString(R.string.error_field_required));
            focusView = srlNoView;
            srlNoView.requestFocus();
            cancel = true;
        }
        // 값이 모두 O -> 스피너 확인
        else if (!(TextUtils.isEmpty(beaconName)) && !(TextUtils.isEmpty(temp_srlNo))) {
            srlNo = Integer.parseInt(temp_srlNo);
            if (srlNo > 0 && distance != null) {
                // 로그인 실행 =========================================================================================
                login(beaconName,srlNo,distance);
            } else if (srlNo > 0 && distance == null) {
                Toast.makeText(this, "알람 설정 거리를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            } else if(srlNo < 0) {
                Toast.makeText(this, "비콘 시리얼 번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                // srlNoView.setError(getString(R.string.error_field_required));
                focusView = srlNoView;
                srlNoView.requestFocus();
                cancel = true;
            }
        }
    }

    // 로그인
    private void login(String beaconName, int srlNo, String distance){
        boolean isOpen = openDatabase();
        if (isOpen) {
            if(dbHelper.insertReord(beaconName,srlNo,distance)){
                Toast.makeText(this, "비콘이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {return;}
        // Reset errors.
        beaconNameView.setError(null);
        srlNoView.setError(null);
    }

    // 데이터 저장 =================================================================================

    private boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                String DROP_SQL = "drop table if exists " + TABLE_NAME;
                db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                U.getInstance().log("Exception in DROP_SQL");
            }

            String CREATE_SQL = "create table " + TABLE_NAME + "("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " name text, "
                    + " age integer, "
                    + " phone text)";

            try {
                db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                U.getInstance().log("Exception in CREATE_SQL");
            }

        }

        public boolean insertReord(String beaconName, int srlNo, String distance){
            try {
                db.execSQL( "insert into " + TABLE_NAME + "(name, age, phone) values ('" + beaconName + "', "+ srlNo +", '" + distance +"');" );
                return true;
            } catch(Exception ex) {
                U.getInstance().log("Exception in insert SQL");
                return false;
            }
        }

        public void onOpen(SQLiteDatabase db) {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            U.getInstance().log("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

    // =============================================================================================

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(beaconNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    // Callback received when a permissions request has been completed.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    // Shows the progress UI and hides the login form.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) { }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Register_Beacon_Activity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        beaconNameView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    // Represents an asynchronous login/registration task used to authenticate the user.
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                srlNoView.setError(getString(R.string.error_incorrect_password));
                srlNoView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

