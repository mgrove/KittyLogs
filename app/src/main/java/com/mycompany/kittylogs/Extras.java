package com.mycompany.kittylogs;

import java.util.Calendar;

/**
 * Created by System User on 10/20/2016.
 */

public class Extras {
    public static String convertMillisecondsToDate(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return monthOfYear + "/" + dayOfMonth + "/" + year;
    }

    public static String convertBoolIntToString(int input){
        if (input==1){
            return "Yes";
        } else {
            return "No";
        }
    }

    public static int convertStringToBoolInt(String input){
        if (input.equals("Yes")){
            return 1;
        } else {
            return 0;
        }
    }

}
