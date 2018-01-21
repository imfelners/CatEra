package catera.itp.sti.com.catera;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by FELIX NERJA on 07/01/2018.
 */

public class Announcement {
//    public String text, senderName, senderDateAndTime;
    public int ID;
    public String announcement;
    public String descrip;
    public String organizer;
    public String departy;
    public String date;
    public String time;
    public String status;

    public boolean isApproved()
    {
        return status.toLowerCase().equals("approved");
    }

    public String GetDateFormatted()
    {
        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");

        String string = date;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date d = null;
        try {
            d = format.parse(string);
            return df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
