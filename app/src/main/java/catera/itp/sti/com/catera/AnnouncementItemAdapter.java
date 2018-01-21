package catera.itp.sti.com.catera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FELIX NERJA on 07/01/2018.
 */

public class AnnouncementItemAdapter extends ArrayAdapter<Announcement> {

    ArrayList<Announcement> list = new ArrayList<>();

    public AnnouncementItemAdapter(Context context, int textViewResourceId, ArrayList<Announcement> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.announcement_item_layout, null);

        ((TextView) v.findViewById(R.id.ann_item_announcement)).setText(list.get(position).announcement);
        ((TextView) v.findViewById(R.id.ann_item_organizer)).setText(list.get(position).organizer);
        ((TextView) v.findViewById(R.id.ann_item_date)).setText(list.get(position).GetDateFormatted() + " " + list.get(position).time);
        ((TextView) v.findViewById(R.id.ann_item_description)).setText(list.get(position).descrip);

        return v;

    }

}
