package com.jodi.companioncompatibility.data.pref

import com.example.flavum.data.pref.Preferences


class JodiPreferences : Preferences() {

    var isLoggedIn by booleanPref(JodiPreferencesKeyEnum.IS_LOGGED_IN.value, false)
    var isGuestLoggedIn by booleanPref(JodiPreferencesKeyEnum.IS_GUEST.value, false)
    var userRole by stringPref(JodiPreferencesKeyEnum.USER.value, "")
    var userId by stringPref(JodiPreferencesKeyEnum.USER_ID.value, "")
    var userPassword by stringPref(JodiPreferencesKeyEnum.USER_PASSWORD.value, "")
    var userMobile by stringPref(JodiPreferencesKeyEnum.USER_MOBILE.value, "")
    var userFName by stringPref(JodiPreferencesKeyEnum.USER_F_NAME.value, "")
    var userLName by stringPref(JodiPreferencesKeyEnum.USER_L_NAME.value, "")
    var userEmail by stringPref(JodiPreferencesKeyEnum.USER_EMAIL.value, "")
    var userAvatar by stringPref(JodiPreferencesKeyEnum.USER_AVATAR.value, "")
    var userDataSaved by booleanPref(JodiPreferencesKeyEnum.USER_DATA_SAVED.value, false)


    var mode by booleanPref(JodiPreferencesKeyEnum.MODE.value, false)
    var accessToken by stringPref(JodiPreferencesKeyEnum.ACCESS_TOKEN.value, "")
    var refreshToken by stringPref(JodiPreferencesKeyEnum.ACCESS_TOKEN.value, "")
    var isAdmin by booleanPref(JodiPreferencesKeyEnum.USER_TYPE.value, false)

    var cameraPermissionDenied by booleanPref(
        JodiPreferencesKeyEnum.CAMERA_PERMISSION.value, false
    )
    var storagePermissionDenied by booleanPref(
        JodiPreferencesKeyEnum.STORAGE_PERMISSION.value, false
    )
    var notificationPermissionGranted by booleanPref(
        JodiPreferencesKeyEnum.NOTIFICATION_PERMISSION.value, false
    )

    var isNotificationEnabled by booleanPref(
        JodiPreferencesKeyEnum.NOTIFICATION_ENABLED.value,
        false
    )

//    fun saveUserData(user: User?) {
//        user?.fname?.takeIf { it.isNotEmpty() }?.let { userFName = it }
//        user?.lname?.takeIf { it.isNotEmpty() }?.let { userLName = it }
//        user?.photo?.takeIf { it.isNotEmpty() }?.let { userAvatar = it }
//        user?.phone?.takeIf { it.isNotEmpty() }?.let { userMobile = it }
//        user?.email?.takeIf { it.isNotEmpty() }?.let { userEmail = it }
//        user?._id?.takeIf { it.isNotEmpty() }?.let { userId = it }
//        userDataSaved = true
//    }

    private fun clearUserData() {
        userFName = ""
        userLName = ""
        userAvatar = ""
        userMobile = ""
        userEmail = ""
        userId = ""
        userPassword = ""
        userDataSaved = false
    }

    fun logout() {
        clearUserData()
        isLoggedIn = false
        userRole = ""
        userId = ""
    }
}