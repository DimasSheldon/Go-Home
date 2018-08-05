package sheldon.com.android.gohome.asynctask;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Synchronizer {
    private static final String TAG = "SYNC";
    private static final String BASE_URL = "http://103.27.207.134/umon/api/device/getListSensor/";

    private SynchronizerListener syncListener;
    private AsyncHttpClient client;
    private ArrayList<String> labels, icons, colors, values;

    public Synchronizer(SynchronizerListener synchronizerListener) {
        this.syncListener = synchronizerListener;
        client = new AsyncHttpClient();
    }

    public void synchronize(String token, String username) {
        Log.d("USERNAME", "synchronize: " + username);
        Log.d("TOKEN", "synchronize: " + token);

        client.addHeader("tokenid", token);
        client.addHeader("username", username);

        labels = new ArrayList<>();
        values = new ArrayList<>();
        icons = new ArrayList<>();
        colors = new ArrayList<>();

        client.post(BASE_URL, null, new JsonHttpResponseHandler() {
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
                try {
                    Log.d("SYNC_RESPONSE", "onSuccess: " + response);
                    Log.d("SYNC_RESPONSE_LENGTH", "onSuccess: " + response.length());

                    for (int i = 1; i < response.length(); i++) {
                        labels.add(response.getJSONObject(String.valueOf(i)).getString("label"));
                        values.add(response.getJSONObject(String.valueOf(i)).getString("value"));
                        icons.add(response.getJSONObject(String.valueOf(i)).getString("icon"));
                        colors.add(response.getJSONObject(String.valueOf(i)).getString("color"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                syncListener.getAttributes(labels, values, icons, colors);
            }
        });
    }
}