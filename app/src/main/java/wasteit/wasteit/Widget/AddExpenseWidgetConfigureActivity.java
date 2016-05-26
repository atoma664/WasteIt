package wasteit.wasteit.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.manager.CategoryManager;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import wasteit.wasteit.R;

/**
 * The configuration screen for the {@link AddExpenseWidget AddExpenseWidget} AppWidget.
 */
public class AddExpenseWidgetConfigureActivity extends Activity {

    DatabaseHelper m_db;
    private static final String PREFS_NAME = "wasteit.wasteit.Widget.AddExpenseWidget";
    private static final String PREF_PREFIX_KEY = "EventID";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Spinner mEventsCurrent;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AddExpenseWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            Event eSelected = (Event)mEventsCurrent.getSelectedItem();

            int nExentID = Consts.EVENT_NOT_EXIST_ERROR;

                nExentID = eSelected.getID();

                saveEventID(context, mAppWidgetId, nExentID);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                AddExpenseWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();

        }
    };

    public AddExpenseWidgetConfigureActivity() {

        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveEventID(Context context, int appWidgetId, int nEventID) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, nEventID);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadEventID(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int eventid = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);

        return eventid;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public void CreateEvent(View view)
    {
        Intent intent = new Intent(this, CreateEventWidgetActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        InitScreen();
    }


    // Write the prefix to the SharedPreferences object for this widget
    static void InitOpeningEvent(Context context, int appWidgetId, int nEventID) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, nEventID);
        prefs.apply();
    }

    private void InitScreen()
    {
        // Select all the current events
        ArrayList<Event> mEvent = m_db.getCurrentEvents();

        // Init spinner and button
        mEventsCurrent = (Spinner) findViewById(R.id.widget_current_event_spinner);
        Button btn = (Button) findViewById(R.id.add_widget_button);
        btn.setOnClickListener(mOnClickListener);

        // Setting spinner values
        ArrayAdapter<Event> adptCat = new ArrayAdapter<Event>(this.getApplicationContext(), android.R.layout.simple_spinner_item, mEvent);

        if (adptCat.isEmpty())
        {
            btn.setVisibility(View.GONE);
            mEventsCurrent.setVisibility(View.GONE);
            findViewById(R.id.ErrorMessage).setVisibility(View.VISIBLE);

        }
        else
        {
            btn.setVisibility(View.VISIBLE);
            mEventsCurrent.setVisibility(View.VISIBLE);
            findViewById(R.id.ErrorMessage).setVisibility(View.GONE);

            adptCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventsCurrent.setAdapter(adptCat);

            // Find the widget id from the intent.
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }


            // If this activity was started with an intent without an app widget ID, finish with an error.
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();

                return;
            }
        }
    }

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        // Connect the DB
        m_db = new DatabaseHelper(this.getApplicationContext());

        // Initialing managers
        CurrencyManager.newInstance(this);
        CategoryManager.newInstance(this);
        EventsManager.newInstance(this);
        ExpenseManager.newInstance(this);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Set the view
        setContentView(R.layout.add_expense_widget_configure);

        InitScreen();
    }
}

