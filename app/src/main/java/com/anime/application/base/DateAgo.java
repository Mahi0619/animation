package com.anime.application.base;

import android.annotation.SuppressLint;

import java.util.Date;

public class DateAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;

    public static String getFormattedDate(String inputDate) {
        try {
            // Parse input date in "yyyy-MM-dd" format
            @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(inputDate);
            long inputMillis = parsedDate.getTime();

            // Get current time and calculate differences
            long now = System.currentTimeMillis();
            long diff = now - inputMillis;

            if (diff < 0) {
                return "In the future";
            }

            if (diff < DAY_MILLIS) {
                return "Today";
            } else if (diff < 2 * DAY_MILLIS) {
                return "Yesterday";
            } else if (diff < 7 * DAY_MILLIS) {
                return (diff / DAY_MILLIS) + " days ago";
            } else {
                // Return the date after 7 days
                @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
                return outputFormat.format(parsedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
}
