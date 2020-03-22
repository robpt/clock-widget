package com.cogiris.justawidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.view.View
import android.widget.EditText
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.clock_widget.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * The configuration screen for the [clock_widget] AppWidget.
 */
class ClockWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private var mHandler: Handler = Handler()

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.clock_widget_configure)
        appWidgetText = findViewById<View>(R.id.appwidget_text) as EditText

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

//        appWidgetText.setText(loadTitlePref(this@ClockWidgetConfigureActivity, appWidgetId))

        mHandler.postDelayed(mRunnable, 1000)
    }

    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            println("run()")
            var context = this@ClockWidgetConfigureActivity
            var appWidgetInstance = AppWidgetManager.getInstance(context)
            updateAppWidget(context, appWidgetInstance, appWidgetId)
            mHandler.postDelayed(this, 1000)
        }
    } //runnable


    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
        finish()
    }

    fun onClickListener(view: View) {
        System.out.println("onClick()")
        val context = this@ClockWidgetConfigureActivity

        // When the button is clicked, store the string locally
//        val widgetText = appWidgetText.text.toString()
//        saveTitlePref(context, appWidgetId, widgetText)

        // It is the responsibility of the configuration activity to update the app widget
//        val appWidgetManager = AppWidgetManager.getInstance(context)
//        updateAppWidget(context, appWidgetManager, appWidgetId)

//        // Make sure we pass back the original appWidgetId
        val resultValue = intent;
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//        resultValue.action = AlarmClock.ACTION_SHOW_ALARMS;
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

private const val PREFS_NAME = "com.cogiris.justawidget.clock_widget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}

internal fun loadCalendarTime(context: Context, appWidgetId: Int): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    return current.format(formatter);
}