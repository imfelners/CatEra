package catera.itp.sti.com.catera;

import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FELIX NERJA on 22/01/2018.
 */

public class SchoolEvent {
    public int ID;
    public String event;
    public String description;
    public String organizer_name;
    public String department;
    public String announce_date;
    public String announce_time;
    public String status_event;
    public String event_color;

    public long GetDate()
    {
        String sDate1=announce_date;
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean isApproved()
    {
        return status_event.toLowerCase().equals("approved");
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String GetType() {
        return event_color.substring(event_color.indexOf('(')+1, event_color.length()-1);
    }

    public int GetColor()
    {
        String str = event_color.substring(0, event_color.indexOf('(')).toLowerCase().trim();

        switch (str)
        {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "violet":
                return Color.rgb(130, 10, 134);
        }

        return Color.RED;
    }


////        holidays.add("07/03/2018"); holidaysText.add(" Girl Child Week (PP 759n, s, 1996)");
//        holidays.add("03/09/2018"); holidaysText.add("Commeration of the Birth Anniversary of Paciano Rizal (PP 1737, s. 2009)");
//        holidays.add("03/17/2018"); holidaysText.add("Commeration of the Death Anniversary of President Ramon Magsaysay (AO 23, s. 2011)");
//        holidays.add("04/02/2018"); holidaysText.add("Commeration of the Birth Anniversary of Francisco 'Balagtas' Baltazar (PP 964, s. 1997)");
//        holidays.add("04/10/2018"); holidaysText.add("Commeration of the Birth Anniversary of General Isidoro Torres (AO 23, s. 2011)");
//        holidays.add("04/16/2018"); holidaysText.add("Commemoration of the Birth Anniversary of President Elpidio Quirino (PP 967, s. 2015)");
//        holidays.add("04/25/2018"); holidaysText.add("National Consciousness Day for the Elimination of Violence Againts Women And Children (RA 10398)");
//        holidays.add("04/27/2018"); holidaysText.add("Araw ng Pagbasa (RA 10556)");
//        holidays.add("12/01/2018"); holidaysText.add("World AIDS day (DM 270, s. 2011)");
//        holidays.add("12/03/2018"); holidaysText.add("International Day of Persons with Disabilities (PD 1157, s. 2006)");
//        holidays.add("01/23/2018"); holidaysText.add("Commemoration of the Anniversary of the Inauguration of the First Philipines Republic (PP 533, s. 2013) ");
//        holidays.add("02/04/2018"); holidaysText.add("Commeration of the Death Anniversary of Baldomero Aguilnadldo (AO 23) ");
//        holidays.add("02/06/2018"); holidaysText.add("Commeration of the Death Anniversary of General Emillio Aguinaldo (AO 23)");
//        holidays.add("02/07/2018"); holidaysText.add("Adoption Consciousness Celebration (PP 72, s. 1999) ");
//        holidays.add("11/01/2017"); holidaysText.add("All Saints Day");
//        holidays.add("11/02/2017"); holidaysText.add("Resumption of Classes");
//        holidays.add("11/30/2017"); holidaysText.add("Bonifacio Day Regular Holiday");
//        holidays.add("12/22/2017"); holidaysText.add("Start of Christmas Break");
//        holidays.add("03/01/2017"); holidaysText.add("Resumption of Classes");
//        holidays.add("01/20/2017"); holidaysText.add("Distribution of Report Cards Parent-Teacher Conference");
//        holidays.add("02/26/2017"); holidaysText.add("Chinese Lunar New Year's Day");
//        holidays.add("03/29/2017"); holidaysText.add("Maundy Thursday (Regular Holiday)");
//        holidays.add("03/30/2017"); holidaysText.add("Good Friday (Regular Holiday)");
//        holidays.add("04/06/2017"); holidaysText.add("Last Day of Classes (Kinder and Grade 10)");
//        holidays.add("04/09/2017"); holidaysText.add("Araw ng Kagitingan (regular holiday)");
}
