package com.example.diana.booksrecommender;

import android.content.Intent;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private LinearLayout Prof_Section;
    private Button SignOut;
    private Button Books;
    private SignInButton SignIn;
    private TextView Name,Email;
    private ImageView Prof_Pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    public static String email;
    private Button show;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        Prof_Section = (LinearLayout)findViewById(R.id.prof_section);
        SignOut = (Button)findViewById(R.id.bn_logout);
        SignIn = (SignInButton) findViewById(R.id.bn_login);
        Books = (Button) findViewById(R.id.bn_books);
        Books.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BooksActivity.class));
            }
        });
        Name = (TextView) findViewById(R.id.name);
        Email = (TextView) findViewById(R.id.email);
        Prof_Pic = (ImageView) findViewById(R.id.prof_pic);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        Prof_Section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDb.getAllData(email);
                if (cursor.getCount() == 0)
                {
                    ShowMessage("Error","No data found 😞");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(cursor.moveToNext())
                {
                    buffer.append("Id: "+ cursor.getString(0)+"\n");
                    buffer.append("Email: "+ cursor.getString(1)+"\n");
                    buffer.append("Tile: "+ cursor.getString(3)+"\n");
                }

                ShowMessage("Data: ",buffer.toString());
            }
        });
    }

    public void ShowMessage (String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bn_login:
                signIn();
                break;
            case R.id.bn_logout:
                signOut();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn()
    {
        Intent intent =Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }
    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            this.email = email;
            String img_url = account.getPhotoUrl().toString();
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(img_url).into(Prof_Pic);
            updateUI(true);
        }
        else
        {
            updateUI(false);
        }

    }
    private void updateUI(boolean isLogin)
    {
        if(isLogin)
        {
            Prof_Section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }
        else
        {
            Prof_Section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        }

    }
}
