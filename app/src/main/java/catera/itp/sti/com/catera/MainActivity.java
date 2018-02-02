package catera.itp.sti.com.catera;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {

    public static final String domain = "http://192.168.56.1/newback/backend/";

    public static boolean reminderMode;

    ListView listView;

    String oldString, newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to the views
        // check if you are connected or not

        listView = (ListView)findViewById(R.id.listView);

        oldString = "";
        newString = "";

        GetAnnouncements();
    }

    public void GetAnnouncements()
    {
        new HttpAsyncTask().execute(domain + (reminderMode? "android_getreminders.php" : "android_getannouncement.php"));
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
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
                GetAnnouncements();
                return;
            }



            ArrayList<Announcement> announcementList = new ArrayList<>();


            boolean newAnnouncement = false;

            String[] str = result.split("#");
            for (String i : str) {
                if (i.length() <= 0)
                    continue;

                String[] str2 = i.split("/");
                Log.d("asd", i);
                Announcement n = new Announcement();
//                n.text = str2[0];
//                n.senderDateAndTime = str2[1] + " " + str2[2];
//                n.senderName = str2[3];
                n.ID = Integer.valueOf(str2[1]);
                n.announcement = str2[2];
                n.descrip = str2[3];
                n.organizer = str2[4];
                n.departy = str2[5];
                n.date = str2[6];
                n.time = str2[7];
                n.status = str2[8];

                if (n.isApproved()) {
                    announcementList.add(n);
                    newAnnouncement = true;
                }
            }



            if (oldString.length() > 0 && newAnnouncement) {
                SplashScreen.notificationID += 1;
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("CATERA")
                        .setContentText("There are new " + (reminderMode? "reminders" : "announcements"))
                        .setSmallIcon(R.drawable.nolabel)
                        //.setContentIntent(pIntent)
                        .setAutoCancel(true).build();
                notificationManager.notify(SplashScreen.notificationID, notification);
            }

            listView.setAdapter(new AnnouncementItemAdapter(MainActivity.this, R.layout.announcement_item_layout, announcementList));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Announcement a = (Announcement) (listView.getAdapter().getItem(position));

                    AnnouncementDetailsActivity.currentAnnouncement = a;
                    Intent i = new Intent(MainActivity.this, AnnouncementDetailsActivity.class);
                    startActivity(i);
                }
            });

            GetAnnouncements();
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
