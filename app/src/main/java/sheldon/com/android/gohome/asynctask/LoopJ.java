package sheldon.com.android.gohome.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.*;

import com.loopj.android.http.*;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.activities.LoginActivity;
import sheldon.com.android.gohome.activities.MainActivity;

public class LoopJ {
    private static final String TAG = "LOOPJ";
    private static final String AUTH_URL = "user/submitLogin/";
    private static final String SYNC_URL = "device/getListSensor/";

    private Context context;
    private LoopJListener loopJListener;

    public static String uname, token, auth;
    public static JSONObject syncResponse;
    public static boolean isBusy;

    public LoopJ(LoopJListener loopJListener) {
        this.loopJListener = loopJListener;
    }

    public LoopJ(Context context, LoopJListener loopJListener) {
        this.context = context;
        this.loopJListener = loopJListener;
    }

    public void sendLoginRequest(String username, String hashPassword){
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("hashpassword", hashPassword);

        LoopJRestClient.post(AUTH_URL, requestParams, new JsonHttpResponseHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(context,
                        R.style.AppTheme_Dark_Dialog);

                progressDialog.setMessage("Authenticating...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "onFailure#2: " + responseString);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    auth = response.getString("logStat");
                    uname = response.getString("username");
                    token = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("AUTHENTICATOR_AUTHSTAT", "onSuccess: " + auth);
                Log.d("AUTHENTICATOR_USERNAME", "onSuccess: " + uname);
                Log.d("AUTHENTICATOR_TOKEN", "onSuccess: " + token);

                //Do something with the response
                loopJListener.authenticate(auth);
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    public void synchronize(String token, String username) {
        Log.d("USERNAME", "synchronize: " + username);
        Log.d("TOKEN", "synchronize: " + token);

//        LoopJRestClient.addHeader("username", username);
        LoopJRestClient.addHeader("username", "dimas");
        LoopJRestClient.addHeader("tokenid", token);

        LoopJRestClient.get(SYNC_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                isBusy = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.d(TAG, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.d(TAG, "onFailure#2: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                Log.d("SYNC_RESPONSE", "onSuccess: " + response);
                Log.d("SYNC_RESPONSE_LENGTH", "onSuccess: " + response.length());
                syncResponse = response;
            }

            @Override
            public void onFinish() {
                isBusy = false;
            }
        });
    }
}