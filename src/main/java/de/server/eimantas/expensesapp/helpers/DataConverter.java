package de.server.eimantas.expensesapp.helpers;

import android.arch.persistence.room.TypeConverter;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;

public class DataConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String fromDate(LocalDateTime date) {
        if (date == null) {
            return (null);
        }

        return (date.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        if (dateString == null) {
            return (null);
        }

        return (LocalDateTime.parse(dateString));
    }

}
