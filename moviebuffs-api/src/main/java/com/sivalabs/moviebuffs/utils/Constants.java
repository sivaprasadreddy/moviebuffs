package com.sivalabs.moviebuffs.utils;

public final class Constants {

    public static final String PROFILE_PROD = "prod";
    public static final String PROFILE_NOT_PROD = "!"+PROFILE_PROD;
    public static final String PROFILE_HEROKU = "heroku";
    public static final String PROFILE_NOT_HEROKU = "!"+PROFILE_HEROKU;
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_IT = "integration-test";
    public static final String PROFILE_DOCKER = "docker";

    public static final String KAFKA_TOPIC_ORDERS = "orders";
    public static final String TMDB_IMAGE_PATH_PREFIX = "https://image.tmdb.org/t/p/w500";

}
