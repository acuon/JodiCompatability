package com.jodi.companioncompatibility.data.pref

enum class JodiPreferencesKeyEnum(val value: String) {

    IS_LOGGED_IN("is_logged_in"),
    IS_GUEST("is_guest"),
    USER("user"),
    USER_ID("user_id"),
    USER_PASSWORD("user_password"),
    USER_EMAIL("user_email"),
    USER_MOBILE("user_mobile"),
    USER_TYPE("user_type"),
    USER_AVATAR("user_avatar"),
    USER_F_NAME("user_f_name"),
    USER_L_NAME("user_l_name"),
    USER_DATA_SAVED("user_data_saved"),

    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token"),

    CAMERA_PERMISSION("camera_permission"),
    STORAGE_PERMISSION("storage_permission"),
    NOTIFICATION_PERMISSION("notification_permission"),
    NOTIFICATION_ENABLED("notification_enabled"),

    MODE("mode"),
    THEME_DARK("dark"),
    THEME_LIGHT("light"),
}