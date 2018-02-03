package catera.itp.sti.com.catera;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MessageListActivity extends AppCompatActivity {

    public static List<User> admins;

    String oldString, newString;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.lstMessageList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageActivity.recipient = admins.get(i);
                Intent intent = new Intent(MessageListActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });

        oldString = "";
        newString = "";

        admins = new ArrayList<>();

        GetEvents();
    }

    public void GetEvents()
    {
        new MessageListActivity.HttpAsyncTask().execute(MainActivity.domain + "android_getadmin.php");
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

            admins = new ArrayList<>();
            List<String> list = new ArrayList<>();
            String[] str = result.split("#");
            for (String i : str) {
                if (i.length() <= 0)
                    continue;

                String[] str2 = i.split("/");

                User u = new User();
                u.ID = Integer.parseInt(str2[1]);
                u.username = str2[2];
                u.password = str2[3];
                u.firstName = str2[4];
                u.middleName = str2[5];
                u.lastName = str2[6];
                u.userType = str2[7];

                String x = u.userType;
                boolean isAdmin = u.userType.equals("admin");
                boolean isSec = u.userType.equals("secretary");

                admins.add(u);
                list.add(u.firstName+" "+u.lastName+" ("+(isAdmin? "Principal" : "Secretary") + ")");
            }

            listView.setAdapter(new ArrayAdapter<>(MessageListActivity.this, android.R.layout.simple_list_item_1, list));

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
