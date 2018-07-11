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
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class LoopJ {
    private static final String TAG = "RESPONSE";
    private static final String BASE_URL = "http://103.27.207.134/umon/api/user/submitLogin/";

    private String url;
    private Context context;
    private LoopJListener loopjListener;
    private AsyncHttpClient client;
    private String authStat, token;
    private HashMap<String, String> params;
    private RequestParams requestParams;

    public LoopJ(Context context, LoopJListener loopjListener) {
        this.context = context;
        this.loopjListener = loopjListener;
        client = new AsyncHttpClient();
    }

    public void sendLoginRequest(String username, String password) {
        String md5Password = convertPassMd5(password);
        requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("hashpassword", md5Password); // password dlm bentuk hash: md5

        client.post(BASE_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                loopjListener.authenticate("Error Bro:(");
                Log.d(TAG, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                loopjListener.authenticate(responseString);
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
                Log.d(TAG, "onSuccess: " + authStat);

                loopjListener.authenticate(authStat);
                loopjListener.getToken(token);
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
}