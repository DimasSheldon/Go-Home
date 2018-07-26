package sheldon.com.android.gohome.asynctask;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

public class Authenticator {
    private static final String TAG = "RESPONSE";
    private static final String BASE_URL = "http://103.27.207.134/umon/api/user/submitLogin/";

    private Context context;
    private AuthenticatorListener authenticatorListener;
    private AsyncHttpClient client;
    private String authStat;
    public String token;

    public Authenticator() {
        //empty constructor
    }

    public Authenticator(Context context, AuthenticatorListener loopjListener) {
        this.context = context;
        this.authenticatorListener = loopjListener;
        client = new AsyncHttpClient();
    }

    public void sendLoginRequest(String username, String password) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("hashpassword", convertPassMd5(password)); // password dlm bentuk hash: md5

        client.post(BASE_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                authenticatorListener.authenticate("Error Bro:(");
                Log.d(TAG, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                authenticatorListener.authenticate(responseString);
                Log.d(TAG, "onFailure#2: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    authStat = response.getString("logStat");
                    token = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("AUTHSTAT_RESPONSE", "onSuccess: " + authStat);
                Log.d("TOKEN_RESPONSE", "onSuccess: " + token);

                authenticatorListener.passToken(token);
                authenticatorListener.authenticate(authStat);
            }
        });
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

    public String getToken() {
        return token;
    }
}