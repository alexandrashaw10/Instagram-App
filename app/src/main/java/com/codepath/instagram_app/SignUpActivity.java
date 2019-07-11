package com.codepath.instagram_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signUpBtn)
    Button signUpBtn;
    @BindView(R.id.emailInput)
    EditText emailInput;
    @BindView(R.id.passwordInput)
    EditText passwordInput;
    @BindView(R.id.usernameInput)
    EditText usernameInput;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        context = this;
    }

    @OnClick(R.id.signUpBtn)
    public void createNewUser() {
        final String username = usernameInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // user is now signed in
                    Log.i("SignUpActivity", "user is signed up");
                    View rootView = getWindow().getDecorView().getRootView();
                    Snackbar.make(rootView, "Sign Up Failure", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    View rootView = getWindow().getDecorView().getRootView();
                    Snackbar.make(rootView, "Sign Up Failure", Snackbar.LENGTH_LONG).show();
                    Log.e("SignUpActivity", "signup user failure");
                    e.printStackTrace();
                }
            }
        });
    }

}
