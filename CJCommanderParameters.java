package ru.fizteh.fivt.students.maked0n.moduletests.library;

import com.beust.jcommander.Parameter;

public class CJCommanderParameters {
    @Parameter(names = "--hideRetweets",
            description = "Фильтровать ретвиты")
    private boolean hideRetweets = false;

    @Parameter(names = { "--stream", "-s" },
            description = "Равномерно печатать твиты на экран по заданноу запросу")
    private boolean stream = false;

    @Parameter(names = { "--query", "-q" },
            description = "Запрос или ключевые слова для поиска",
            required = true)
    private String query = "";

    @Parameter(names = { "--place", "-p" },
            description = "Искать твиты в вашем регионе")
    private String place = "";

    @Parameter(names = { "--limit", "-l" },
            description = "Выводить лишь несколько твитов (не работает для --stream)",
            validateWith = CLimitValidator.class)
    private int limit = 0;

    @Parameter(names = { "--help", "-h" },
            description = "Показать эту справку", help = true)
    private boolean help = false;

    public final boolean isHideRetweets() {
        return hideRetweets;
    }
    public final boolean isStream() {
        return stream;
    }
    public final String getQuery() {
        return query;
    }
    public final String getPlace() {
        return place;
    }
    public final int getLimit() {
        return limit;
    }
    public final boolean isHelp() {
        return help;
    }
}
