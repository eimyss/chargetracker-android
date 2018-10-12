package de.server.eimantas.expensesapp.helpers.serializers;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

   private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(formatter));
    }
}