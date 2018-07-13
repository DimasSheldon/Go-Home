package sheldon.com.android.gohome.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sheldon.com.android.gohome.asynctask.LoopJ;
import sheldon.com.android.gohome.asynctask.LoopJListener;
import sheldon.com.android.gohome.R;

public class LoginActivity extends AppCompatActivity implements LoopJListener {

    private EditText mUsername, mPassword;
    private Button mButtonLogin;
    private LoopJ client;
    private ProgressDialog progressDialog;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mUsername = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        mButtonLogin = (Button) findViewById(R.id.btn_login);

        client = new LoopJ(this, this);

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

        client.sendLoginRequest(username, password);

        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @Override
    public void authenticate(String serverResponse) {
        if (serverResponse.equals("SUCCESS")) {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            onLoginSuccess();

            Toast.makeText(this, token, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, serverResponse, Toast.LENGTH_SHORT).show();
            onLoginFailed();
        }
    }


    @Override
    public void getToken(String serverToken) {
        token = serverToken;
    }

    public boolean validate(String username, String password) {
        boolean valid = true;

        if (username.isEmpty() || username.length() < 3) {
            mUsername.setError("Enter a valid username");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        progressDialog.dismiss();
        mButtonLogin.setEnabled(true);
    }

    public void onLoginFailed() {
        progressDialog.dismiss();
        mButtonLogin.setEnabled(true);
    }
}