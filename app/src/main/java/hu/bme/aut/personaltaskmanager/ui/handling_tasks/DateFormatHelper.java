package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatHelper {

    public static String getFormattedDate(long date, String dateFormat)
    {
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatter.format(c.getTime());
    }

}
