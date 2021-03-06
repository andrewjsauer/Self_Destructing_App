package sauerapps.self_destructingapp.View;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sauerapps.self_destructingapp.Parse.selfDestructingApplication;
import sauerapps.self_destructingapp.R;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.sign_up_button_login_screen) protected Button mSignUpButton;
    @InjectView(R.id.user_name_input_login_screen) protected EditText mUserName;
    @InjectView(R.id.password_input_login_screen) protected EditText mPassword;
    @InjectView(R.id.login_screen_button) protected Button mLoginButton;
    @InjectView(R.id.progressBarLogin) protected ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.INVISIBLE);

                String username = mUserName.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (e == null) {

                                selfDestructingApplication.updateParseInstallation(user);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertBuilder.setMessage(e.getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = alertBuilder.create();
                                dialog.show();
                            }

                        }
                    });

                }
            }
        });

    }
}