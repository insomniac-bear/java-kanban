package api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(final JsonWriter out, final LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(formatter.format(value));
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        String dateString = in.nextString();

        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга даты: " + dateString, e);
        }
    }
}
