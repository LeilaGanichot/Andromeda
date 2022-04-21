package edu.fsu.cs.andromeda.util;

import android.annotation.SuppressLint;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AndromedaDate {

    public static String getTodaysDate() {
        LocalDateTime date = new LocalDateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.print(date);
    }

    public static String monthFromDate(LocalDate selectedDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM");
        return dtf.print(selectedDate); // minus because Joda time is not zero indexed
    }

    public static String formatDateForDB(org.joda.time.LocalDate date, String dayNum){
        if(dayNum.equals(" ")) return " ";
        LocalDate moddedDate = null;
        /*
            If the user taps on calendar cell 31, on a month that has 31 days, but then switches
            to another month that only has 30 days, this try/catch prevents Joda time from
            attempting to convert this into a valid LocalDate, since a date such as 04-31-2022
            does not exist.
         */
        try {
            moddedDate = date.withDayOfMonth(Integer.parseInt(dayNum));
        } catch (IllegalFieldValueException e) {
            e.printStackTrace();
        }
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        return (moddedDate != null)? dtf.print(moddedDate) : " ";
    }

    public static String monthYearFromDate(org.joda.time.LocalDate selectedDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM yyyy");
        return dtf.print(selectedDate);
    }

    public static ArrayList<String> daysInMonthArray(org.joda.time.LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        org.joda.time.YearMonth yearMonth = new YearMonth(
                selectedDate.getYear(),
                selectedDate.getMonthOfYear()
        );

        int daysInMonth = yearMonth
                .toDateTime(null)
                .dayOfMonth()
                .getMaximumValue();

        org.joda.time.LocalDate firstOfMonth = selectedDate.withDayOfMonth(1); // get first day of the month
        int dayOfWeek = firstOfMonth.getDayOfWeek();

        for (int i = 1; i < 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add(" "); // we add a blank
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    public static String formatDateTimeFromMDPToDBFormat(String dateTimeString){
        @SuppressLint("SimpleDateFormat") DateFormat parser = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    public static String formatDateTimeFromDBFormatToPretty(String dateTimeString){
        @SuppressLint("SimpleDateFormat") DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    public static Calendar convertToDateTime(String dateTime) {
        Calendar convertedDateTime = Calendar.getInstance();

        String[] dateTimeSplit = dateTime.split(" ");

        String[] dateSplit = dateTimeSplit[0].split("-");
        String[] timeSplit = dateTimeSplit[1].split(":");

        convertedDateTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]) - 1); // -1 because months are 0th indexed
        convertedDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[2]));
        convertedDateTime.set(Calendar.YEAR, Integer.parseInt(dateSplit[0]));

        convertedDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
        convertedDateTime.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
        convertedDateTime.set(Calendar.SECOND, 0);

        return convertedDateTime;
    }

    public static String extractDayFromDate(String dateTimeString, String month){
        if (!dateTimeString.isEmpty()) {
            String dateString = dateTimeString.split(" ")[0];
            String monthFromSplit = dateString.split("-")[1]; // string array after splits looks like: [year, month, day]
            if(monthFromSplit.equals(month)) {
                return String.valueOf(Integer.parseInt(dateString.split("-")[2]));
            } else {
                return "";
            }
        }else{
            return "";
        }
    }
}
