package info.androidhive.sqlite.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LENOVO on 05/05/2016.
 */
public class Services {

    static java.text.DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    static java.text.DateFormat shortformat = new SimpleDateFormat("dd/MM/yy");

    public static Date FromStringToDate(String strDate)
    {
        try
        {
            return format.parse(strDate);
        }
        catch (Exception e)
        {
            throw new RuntimeException("String " + strDate +  " not in the format to Convert to Date");
        }
    }

    public static String FromDateToShortYearString(Date date)
    {
        return shortformat.format(date);
    }

    public static String FromDateToString(Date date)
    {
        return format.format(date);
    }

    public static String GetTodayString()
    {
        return format.format(new Date());
    }

    public static boolean IsValidDate(String dateString)
    {
        boolean bIsDateValid = true;

        try
        {
            Date d = Services.FromStringToDate(dateString);
            bIsDateValid = true;
        }
        catch (Exception e)
        {
            bIsDateValid = false;
        }

        return bIsDateValid;
    }

    public static Date GetNextPrevDate(Date d, int nNumOfDays)
    {
        // Set the end value to the next day
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, nNumOfDays);

        return c.getTime();
    }

    public static int DaysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static double ReturnRound(double d)
    {
        return (new BigDecimal(d).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public static Date GetDayAtStart(Date d)
    {
        return Services.FromStringToDate(Services.FromDateToString(d));
    }

    public static Date GetToDayAtStart()
    {
        return GetDayAtStart(new Date());
    }

    public static Double findExchangeRateAndConvert(String from, String to, double amount)
    {
        double dReturn = -1;

        try
        {
            //Yahoo Finance API
            URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s="+ from + to + "=X");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            if (line.length() > 0) {
                dReturn = Double.parseDouble(line) * amount;
            }
            reader.close();
        }
        catch (Exception e)
        {
            int n = 0;
        }
        return dReturn;
    }
}
