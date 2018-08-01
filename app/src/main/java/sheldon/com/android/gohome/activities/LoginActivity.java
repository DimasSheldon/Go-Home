package sheldon.com.android.gohome.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sheldon.com.android.gohome.asynctask.Authenticator;
import sheldon.com.android.gohome.asynctask.AuthenticatorListener;
import sheldon.com.android.gohome.R;

public class LoginActivity extends AppCompatActivity implements AuthenticatorListener {

    private EditText mUsername, mPassword;
    private Button mButtonLogin;
    private Authenticator client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mUsername = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        mButtonLogin = (Button) findViewById(R.id.btn_login);

        client = new Authenticator(this);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void login(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if (!validate(username, password)) {
            return;
        }

        mButtonLogin.setEnabled(false);

        client.sendLoginRequest(username, convertPassMd5(password));

        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @Override
    public void authenticate(String serverResponse) {
        if (serverResponse.equals("SUCCESS")) {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            onLoginSuccess();

        } else {
            Toast.makeText(this, serverResponse, Toast.LENGTH_SHORT).show();
            onLoginFailed();
        }
    }

    public boolean validate(String username, String password) {
        boolean valid = true;

        if (username.isEmpty() || username.length() < 3) {
            mUsername.setError("Enter a valid username");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            mPassword.setError("Must be filled");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        progressDialog.dismiss();
        mButtonLogin.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        progressDialog.dismiss();
        mButtonLogin.setEnabled(true);
    }
    private static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}