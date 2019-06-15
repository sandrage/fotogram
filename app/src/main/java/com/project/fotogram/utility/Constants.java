package com.project.fotogram.utility;

import java.util.HashMap;

public class Constants {
    public static final String BASEURL = "https://ewserver.di.unimi.it/mobicomp/fotogram/";
    public static final String APPLICATION_ID = "com.project.fotogram";
    public static final int MAX_POST_BYTES = 97280;
    public static final int MAX_PROFILE_BYTES = 10137;
    public static final String INTERNAL_FILE_NAME = "fotogramProfilePhotos";

    public static final HashMap<String, String> ERROR_CONVERSIONS = new HashMap<String, String>() {{
        put("CANNOT FOLLOW YOURSELF", "You can't follow yourself!");
        put("ALREADY FOLLOWING USER", "You are already following that user!");
        put("USERNAME NOT FOUND", "Username not found");
        put("YOU ARE NOT FOLLOWING THAT USER", "You are already not following that user!");
        put("INVALID CREDENTIALS", "Invalid username or password!");
    }};
}
