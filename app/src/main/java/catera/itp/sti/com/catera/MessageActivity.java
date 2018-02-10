package catera.itp.sti.com.catera;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    public static User recipient;

    ListView listView;
    Button button;
    EditText editText;
    RequestQueue queue;
    ArrayList<Message> messages;
    String oldString, newString;

    String sentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.lstMessageList);
        button = (Button)findViewById(R.id.btnSend);
        editText = (EditText)findViewById(R.id.txtMessage);

        sentMessage = "";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() <= 0)
                {
                    Toast.makeText(MessageActivity.this, "Please input your message first", Toast.LENGTH_SHORT).show();
                    return;
                }

                sentMessage = editText.getText().toString();
                editText.setText("");
                Send();
            }
        });

        oldString = "";
        newString = "";
        GetEvents();

        Log.d("querymessage", MainActivity.domain + "android_getmessages.php?senderID="+LoginActivity.currentUser.ID+"&receiverID="+recipient.ID);
    }

    public void GetEvents()
    {
        new MessageActivity.HttpAsyncTask().execute(MainActivity.domain + "android_getmessages.php?senderID="+LoginActivity.currentUser.ID+"&receiverID="+recipient.ID);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (!CanUpdate(result)) {
                GetEvents();
                return;
            }

            Log.d("---result", result);

            messages = new ArrayList<>();
            List<String> list = new ArrayList<>();
            String[] str = result.split("#");
            for (String i : str) {
                if (i.length() <= 0)
                    continue;

                String[] str2 = i.split("/");

                Log.d("---result", i);

                Message m = new Message();
                m.senderID = LoginActivity.currentUser.ID;
                m.message = str2[3];
                m.created_at = str2[4];
                m.senderType = Integer.parseInt(str2[2]);

                list.add(m.message);
                messages.add(m);
            }

            listView.setAdapter(new MessageListAdapter(MessageActivity.this, R.layout.layout_message, messages));
            listView.setSelection(listView.getAdapter().getCount() - 1);
            GetEvents();
            oldString = newString;
        }
    }

    public void Send()
    {
        String u = "";

        try {
            u = URLEncoder.encode(sentMessage,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://www.catera2018.com/android_sendmessage.php?receiverID="
        +recipient.username+"&senderID="+LoginActivity.currentUser.username+"&message="+ u;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        sentMessage = "";
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "error");
                        editText.setText(sentMessage);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

//                params.put("receiverID", String.valueOf(recipient.username));
//                params.put("senderID", String.valueOf(LoginActivity.currentUser.username));
//                params.put("message", sentMessage);

                return params;
            }
        };
        Log.d("Response", postRequest.getUrl());
        queue.add(postRequest);
    }


    boolean CanUpdate(String str)
    {
        newString = str;

        boolean b = oldString.equals(newString);
        return !b;
    }
}
