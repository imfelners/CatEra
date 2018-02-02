package catera.itp.sti.com.catera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AnnouncementDetailsActivity extends AppCompatActivity {

    public static Announcement currentAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(currentAnnouncement.announcement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.ann_details_dept_title)).setText(MainActivity.reminderMode? "Inclusion" : "Department");

        ((TextView)findViewById(R.id.ann_details_organizer)).setText(currentAnnouncement.organizer);
        ((TextView)findViewById(R.id.ann_details_date)).setText(currentAnnouncement.GetDateFormatted() + " " + currentAnnouncement.time);
        ((TextView)findViewById(R.id.ann_details_department)).setText(currentAnnouncement.departy);
        ((TextView)findViewById(R.id.ann_details_description)).setText(currentAnnouncement.descrip);
    }

}
