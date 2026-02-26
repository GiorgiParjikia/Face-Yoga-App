package ru.netology.faceyoga.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsLogger @Inject constructor(
    private val fa: FirebaseAnalytics
) {
    fun log(name: String, params: Bundle? = null) {
        fa.logEvent(name, params)
    }
}