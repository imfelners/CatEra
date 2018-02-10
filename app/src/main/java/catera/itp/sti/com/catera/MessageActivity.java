package catera.itp.sti.com.catera;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    public static User recipient;

    ListView listView;
    Button button;
    EditText editText;

    ArrayList<Message> messages;
    String oldString, newString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.lstMessageList);
        button = (Button)findViewById(R.id.btnSend);
        editText = (EditText)findViewById(R.id.txtMessage);



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
                m.ID  = Integer.parseInt(str2[1]);
                m.senderID = Integer.parseInt(str2[2]);
                m.receiverID = Integer.parseInt(str2[3]);
                m.message = str2[4];
                m.timestamp = str2[5];
                m.fromAdmin = Integer.parseInt(str2[6]);

                list.add(m.message);
                messages.add(m);
            }

            listView.setAdapter(new MessageListAdapter(MessageActivity.this, R.layout.layout_message, messages));

            GetEvents();
            oldString = newString;
        }
    }


    boolean CanUpdate(String str)
    {
        newString = str;

        boolean b = oldString.equals(newString);
        return !b;
    }
}
