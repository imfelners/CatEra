package catera.itp.sti.com.catera;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by FELIX NERJA on 09/02/2018.
 */

public class ScheduledService extends Service
{

    private Timer timer = new Timer();

    public static String stringAnnouncement = "", stringEvents = "", stringReminders = "";


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url =MainActivity.domain + "get_data.php";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Parse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }, 0, 15*1000);//5 Minutes
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void PushNotification(Context context, String type)
    {
        NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        //set
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.nolabel);
        builder.setContentText("There are new posted " + type.toLowerCase() + "(s)");
        builder.setContentTitle("CAT-ERA");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        nm.notify((int)System.currentTimeMillis(),notification);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void Parse(String str)
    {
        Log.d("servicetest",str);
        String[] data = str.split("/");

        for (String i : data)
        {
            Log.d("servicetest", "parse:   " + i);
        }

        String a, e, r;
        a = data[0];
        e = data[1];
        r = data[2];

        if (stringAnnouncement.length() <= 0) {
            stringAnnouncement = a;
        }else{
            if (!stringAnnouncement.equals(a))
            {
                PushNotification(getApplicationContext(), "Announcement");
                stringAnnouncement = a;
            }
        }

        if (stringEvents.length() <= 0) {
            stringEvents = e;
        }else{
            if (!stringEvents.equals(e))
            {
                PushNotification(getApplicationContext(), "Event");
                stringEvents = e;
            }
        }

        if (stringReminders.length() <= 0) {
            stringReminders = r;
        }else{
            if (!stringReminders.equals(r))
            {
                PushNotification(getApplicationContext(), "Reminder");
                stringReminders = r;
            }
        }


    }
}

