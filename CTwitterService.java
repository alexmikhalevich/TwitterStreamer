package ru.fizteh.fivt.students.maked0n.moduletests.library;

import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static ru.fizteh.fivt.students.maked0n.moduletests.library.CGeoCoding.getCurrentLocation;
import static ru.fizteh.fivt.students.maked0n.moduletests.library.CGeoCoding.getPlace;
import static ru.fizteh.fivt.students.maked0n.moduletests.library.CTimeFormat.getTime;

public class CTwitterService implements ITwitterService {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final int RT_PREFIX_LENGTH = 3;
    static final int GET_LAST_NUM = 10;
    static final int ONE = 1;
    static final int FIVE = 5;
    static final double NEARBY_RADIUS = 5.0d;

    private final Twitter twitter;
    private final TwitterStream twitterStream;
    private CJCommanderParameters commander;

    CTwitterService(Twitter twitter, TwitterStream twitterStream, CJCommanderParameters commander) {
        this.twitterStream = twitterStream;
        this.twitter = twitter;
        this.commander = commander;
    }

    @Override
    public void listenForTweets(String query, Consumer<String> listener) throws TwitterException {
        twitterStream.addListener(new CStatusAdapter() {
            @Override
            public void onStatus(Status status) {
                try {
                    listener.accept(formatTweet(status));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                System.err.println(e.getMessage());
            }
        });
        FilterQuery tweetFilterQuery = new FilterQuery();
        if (query.contains(",")) {
            String[] tags = query.split(",");
            tweetFilterQuery.track(tags);
        } else {
            tweetFilterQuery.track(query);
        }
        twitterStream.filter(tweetFilterQuery);
    }

    @Override
    public List<String> getFormattedTweets(String query) throws Exception {
        if (query == null) {
            throw new IllegalArgumentException("Запрос является обязательным параметром");
        }

        Query twtQuery = new Query(query);

        if (commander.isHideRetweets()) {
            twtQuery.setQuery(twtQuery.getQuery() + " +exclude:retweets");
        }
        if (commander.getLimit() > 0) {
            twtQuery.setCount(commander.getLimit());
        }
        if (!commander.getPlace().isEmpty()) {
            if (commander.getPlace().matches("nearby")) {
                double[] result = getCurrentLocation();
                if (result != null) {
                    GeoLocation location = new GeoLocation(result[0], result[1]);
                    twtQuery.setGeoCode(location, NEARBY_RADIUS, Query.Unit.km);
                }
            } else {
                double[] placeCoord = getPlace(commander);
                GeoLocation location = new GeoLocation(placeCoord[0], placeCoord[1]);
                twtQuery.setGeoCode(location, placeCoord[2], Query.Unit.km);
            }
        }
        QueryResult result = twitter.search(twtQuery);
        List<Status> status = result.getTweets();
        List<String> twtList = new ArrayList<>();

        if (status.isEmpty()) {
            twtList.add("Твиты по вашему запросу не найдены");
        } else {
            for (Status st : status) {
                twtList.add(formatTweet(st));
            }
        }

        return twtList;
    }

    private String formatTweet(Status status) throws Exception {
        StringBuilder tweetText = new StringBuilder("[").append(getTime(status))
                .append("] @").append(ANSI_BLUE).append(status.getUser()
                        .getScreenName()).append(ANSI_RESET).append(": ");
        if (status.isRetweet()) {
            tweetText.append("ретвитнул ").append(
                    status.getText().substring(RT_PREFIX_LENGTH));
        } else if (status.getRetweetCount() > 0) {
            if (status.getRetweetCount() % GET_LAST_NUM > ONE && status.getRetweetCount() % GET_LAST_NUM < FIVE) {
                tweetText.append(status.getText()).append(" (")
                        .append(status.getRetweetCount()).append(" ретвита)");
            } else if (status.getRetweetCount() % GET_LAST_NUM == ONE) {
                tweetText.append(status.getText()).append(" (")
                        .append(status.getRetweetCount()).append(" ретвит)");
            } else {
                tweetText.append(status.getText()).append(" (")
                        .append(status.getRetweetCount()).append(" ретвитов)");
            }
        } else {
            tweetText.append(status.getText()).append(" (нет ретвитов)");
        }

        return tweetText.toString();
    }
}
