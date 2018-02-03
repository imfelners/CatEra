package catera.itp.sti.com.catera;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchoolEventsActivity extends AppCompatActivity {

    final String colorHoliday = "#FF08FF00";

    CompactCalendarView compactCalendar;
    String oldString, newString;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        holidays = new ArrayList<>();
//        holidaysText = new ArrayList<>();
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setEventIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                  List<Event> e = compactCalendar.getEvents(dateClicked);

                  if (e == null)
                      return;
                  if (e.size() <= 0)
                      return;

                List<SchoolEvent> s = new ArrayList<SchoolEvent>();
                for (Event i : e)
                {
                    s.add((SchoolEvent) (i.getData()));
                }

                SchoolEventsDetailsActivity.currentEvents = s;

                Intent i = new Intent(SchoolEventsActivity.this, SchoolEventsDetailsActivity.class);
                startActivity(i);

//                Context context = getApplicationContext();
//
//                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
//                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
//                }


            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                getSupportActionBar().setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


        oldString = "";
        newString = "";

        GetEvents();

//        int j = 0;
//        for (String i : holidays)
//        {
//            long x = StoD(i).getTime();
//
//            Event ev = new Event(Color.RED, x, holidaysText.get(j));
//            compactCalendar.addEvent(ev);
//
//            j++;
//        }

    }

    public void GetEvents()
    {
        new HttpAsyncTask().execute(MainActivity.domain + "android_getevents.php");
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

            boolean newAnnouncement = false;


            compactCalendar.removeAllEvents();
            String[] str = result.split("#");
            for (String i : str) {
                if (i.length() <= 0)
                    continue;

                String[] str2 = i.split("/");
                SchoolEvent n = new SchoolEvent();
//                n.text = str2[0];
//                n.senderDateAndTime = str2[1] + " " + str2[2];
//                n.senderName = str2[3];
                n.ID = Integer.valueOf(str2[1]);
                n.event = str2[2];
                n.description = str2[3];
                n.organizer_name = str2[4];
                n.department = str2[5];
                n.announce_date = str2[6];
                n.announce_time = str2[7];
                n.status_event = str2[8];
                n.event_color = str2[9];


                if (n.isApproved()) {
                    compactCalendar.addEvent(new Event(n.GetColor(), n.GetDate(), n));
                    newAnnouncement = true;
                }
            }



            if (oldString.length() > 0 && newAnnouncement) {
                SplashScreen.notificationID += 1;
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("CATERA")
                        .setContentText("There are new school events")
                        //.setSmallIcon(R.drawable.nolabel)
                        //.setContentIntent(pIntent)
                        .setAutoCancel(true).build();
                notificationManager.notify(SplashScreen.notificationID, notification);
            }

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
