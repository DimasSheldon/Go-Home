package sheldon.com.android.gohome.asynctask;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class Authenticator {
    private static final String TAG = "RESPONSE";
    private static final String BASE_URL = "http://103.27.207.134/umon/api/user/submitLogin/";

    private AuthenticatorListener authenticatorListener;
    private AsyncHttpClient client;
    private String authStat;
    public static String uname, token;

    public Authenticator(AuthenticatorListener authenticatorListener) {
        this.authenticatorListener = authenticatorListener;
        client = new AsyncHttpClient();
    }

    public void sendLoginRequest(String username, String hashPassword) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("hashpassword", hashPassword);

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
                    uname = response.getString("username");
                    token = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("AUTHENTICATOR_AUTHSTAT", "onSuccess: " + authStat);
                Log.d("AUTHENTICATOR_USERNAME", "onSuccess: " + uname);
                Log.d("AUTHENTICATOR_TOKEN", "onSuccess: " + token);

                authenticatorListener.authenticate(authStat);
            }
        });
    }
}