package catera.itp.sti.com.catera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SchoolEventsDetailsActivity extends AppCompatActivity {

    public static List<SchoolEvent> currentEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_events_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> str = new ArrayList<>();
        for (SchoolEvent i : currentEvents) {
            str.add(i.event);
        }

        ListView l = (ListView)findViewById(R.id.lstSchoolEvents);
        l.setAdapter(new ArrayAdapter<>(SchoolEventsDetailsActivity.this, android.R.layout.simple_list_item_1, str));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Load(currentEvents.get(i));
            }
        });

        Load(currentEvents.get(0));
    }

    public void Load(SchoolEvent s)
    {
        ((TextView)findViewById(R.id.ann_details_date)).setText(s.announce_date + " " + s.announce_time);
        ((TextView)findViewById(R.id.ann_details_department)).setText(s.department);
        ((TextView)findViewById(R.id.ann_details_description)).setText(s.description);
        ((TextView)findViewById(R.id.ann_details_organizer)).setText(s.organizer_name);
        ((TextView)findViewById(R.id.ann_details_type)).setText(s.GetType());
    }
}
