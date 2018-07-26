package sheldon.com.android.gohome.asynctask;

import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import sheldon.com.android.gohome.fragments.MonitorFragment;

public class Synchronizer {
    private static final String TAG = "SYNC";
    private static final String BASE_URL = "http://103.27.207.134/umon/api/device/getListSensor/"; //192.168.88.15

    private SynchronizerListener syncListener;
    private AsyncHttpClient client;
    private ArrayList<String> labels, icons, colors;

    public Synchronizer(SynchronizerListener synchronizerListener) {
        this.syncListener = synchronizerListener;
        client = new AsyncHttpClient();
    }

    public void synchronize(String token) {
        client.addHeader("tokenid", token);
        client.addHeader("username", "ASDFasdf");
        Log.d("TOKEN", "synchronize: " + token);

        labels = new ArrayList<>();
        icons = new ArrayList<>();
        colors = new ArrayList<>();

        client.post(BASE_URL, null, new JsonHttpResponseHandler() {
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
                    Log.d("SYNC_RESPONSE", "onSuccess: " + response);
                    Log.d("SYNC_RESPONSE_LENGTH", "onSuccess: " + response.length());

                    for (int i = 1; i <= response.length(); i++) {
                        icons.add(response.getJSONObject(String.valueOf(i)).getString("icon"));
                        labels.add(response.getJSONObject(String.valueOf(i)).getString("label"));
                        colors.add(response.getJSONObject(String.valueOf(i)).getString("color"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.d(TAG, "onSuccess: " + labels + icons + colors);

                syncListener.getAttributes(labels, icons, colors);
            }
        });
    }
}