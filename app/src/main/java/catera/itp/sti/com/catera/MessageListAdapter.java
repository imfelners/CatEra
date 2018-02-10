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

public class MessageListAdapter extends ArrayAdapter<Message> {

    ArrayList<Message> list = new ArrayList<>();

    public MessageListAdapter(Context context, int textViewResourceId, ArrayList<Message> objects) {
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
        v = inflater.inflate(R.layout.layout_message, null);

        Message m = list.get(position);

        ((TextView) v.findViewById(R.id.txtMessage)).setText(m.message);
        (v.findViewById(R.id.spaceReceiver)).setVisibility(m.fromAdmin == 0? View.GONE : View.VISIBLE);
        (v.findViewById(R.id.spaceReceiver)).setVisibility(m.fromAdmin == 1? View.GONE : View.VISIBLE);

        return v;

    }

}
