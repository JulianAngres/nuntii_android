package com.nuntiias.nuntiitheone

data class PushNotification(
    val notification: NotificationNotification,
    val data: NotificationData, // Achtung: Am 26.01.23 wurde notification statt data verwendet, Test morgen, laut Stackoverflow https://stackoverflow.com/questions/45286202/how-does-one-distinguish-between-android-and-ios-firebase-ids-for-push-notificat
    val to: String
)