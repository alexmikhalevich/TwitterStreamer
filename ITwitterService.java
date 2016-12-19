package ru.fizteh.fivt.students.maked0n.moduletests.library;

import twitter4j.TwitterException;

import java.util.List;
import java.util.function.Consumer;

public interface ITwitterService {
    List<String> getFormattedTweets(String query) throws Exception;
    void listenForTweets(String query, Consumer<String> listener) throws TwitterException;
}
