package de.scriptsdk.core.model.mapper;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class DelphiDateTimeMapper {

    private final LocalDateTime dateTime;
    private final Double value;

    public DelphiDateTimeMapper(Double value) {
        this.value = value;

        Calendar calendar = Calendar.getInstance();
        calendar.set(1899, Calendar.DECEMBER, 30, 0, 0, 0);
        calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

        int days = value.intValue();
        calendar.add(Calendar.DATE, days);

        double remaining = value - days;

        int hours = (int) (remaining * 24);
        calendar.add(Calendar.HOUR, hours);
        remaining -= (hours / 24.0);

        int minutes = (int) (remaining * 24 * 60);
        calendar.add(Calendar.MINUTE, minutes);
        remaining -= (minutes / 24.0 / 60.0);

        int seconds = (int) (remaining * 24 * 60 * 60);
        calendar.add(Calendar.SECOND, seconds);
        remaining -= (seconds / 24.0 / 60.0 / 60);

        int nanos = (int) Math.round(remaining * 24 * 60 * 60 * 1000000000);
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        int val = Double.valueOf(decimalFormat.format(nanos / 1000000.00).replace(",", ".")).intValue();


        LocalDateTime time = LocalDateTime.ofInstant(calendar.toInstant(),
                calendar.getTimeZone().toZoneId());

        this.dateTime = time.plusNanos(val * 1000000L);
    }

    public DelphiDateTimeMapper(LocalDateTime value) {

        int hour = value.getHour();
        int minute = value.getMinute();
        int seconds = value.getSecond();
        int milli = value.getNano() / 1000000;

        LocalDateTime tmp = value.minusNanos(value.getNano());
        this.dateTime = tmp.plusNanos(milli * 1000000L);


        double nextValue = (hour) / 24.0;
        nextValue += (minute) / (24.0 * 60.0);
        nextValue += (seconds) / (24.0 * 60.0 * 60);
        nextValue += (milli) / (24.0 * 60.0 * 60 * 1000);

        LocalDate dateBefore = LocalDate.of(1899, 12, 30);
        LocalDate dateAfter = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
        long daysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);

        this.value = nextValue + daysBetween;
    }

    public Double getValue() {
        return value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
