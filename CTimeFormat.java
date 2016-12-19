package ru.fizteh.fivt.students.maked0n.moduletests.library;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class CTimeFormat {
    static final int GET_LAST_NUM = 10;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final int NINE = 9;
    static final int ELEVEN = 11;
    static final int NINETEEN = 19;

    enum ETime {
        MINUTE,
        HOUR,
        DAY
    }

    public static String getTime(Status tweet) throws Exception {
        LocalDateTime tweetDate = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curDate = LocalDateTime.now();

        if (ChronoUnit.MINUTES.between(tweetDate, curDate) < 2) {
            return "только что";
        } else if (ChronoUnit.HOURS.between(tweetDate, curDate) == 0) {
            return Long.toString(ChronoUnit.MINUTES.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.MINUTES.between(tweetDate, curDate), ETime.MINUTE);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == 0) {
            return Long.toString(ChronoUnit.HOURS.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.HOURS.between(tweetDate, curDate), ETime.HOUR);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == ONE) {
            return "вчера";
        } else {
            return Long.toString(ChronoUnit.DAYS.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.DAYS.between(tweetDate, curDate), ETime.DAY);
        }
    }

    private static String getTimeString(long number, ETime type) {
        if (type == ETime.MINUTE) {
            if (number % GET_LAST_NUM == 0 || number % GET_LAST_NUM == ONE
                    || (number >= ELEVEN && number <= NINETEEN)) {
                return " минут назад";
            } else if (number % GET_LAST_NUM > ONE && number % GET_LAST_NUM < FIVE) {
                return " минуты назад";
            } else {
                return " минут назад";
            }
        } else if (type == ETime.HOUR) {
            if (number % GET_LAST_NUM == 0 || (number % GET_LAST_NUM > FOUR && number % GET_LAST_NUM <= NINE)
                    || (number >= ELEVEN && number <= NINETEEN)) {
                return " часов назад";
            } else if (number % GET_LAST_NUM == ONE) {
                return " час назад";
            } else {
                return " часа назад";
            }
        } else {
            if (number >= ELEVEN && number <= NINETEEN) {
                return " дней назад";
            } else if (number % GET_LAST_NUM >= 0 && number % GET_LAST_NUM < TWO) {
                return " день назад";
            } else if (number % GET_LAST_NUM >= TWO && number % GET_LAST_NUM < FIVE) {
                return " дня назад";
            } else {
                return " дней назад";
            }
        }
    }
}