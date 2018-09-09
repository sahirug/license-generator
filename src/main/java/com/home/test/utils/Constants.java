package com.home.test.utils;

import java.net.URL;

public class Constants {
    public static final URL PRIVATE_KEY_URL = Thread.currentThread().getContextClassLoader().getResource("private-key.cen");
    public static final URL PUBLIC_KEY_URL = Thread.currentThread().getContextClassLoader().getResource("public-key.cen");
}
