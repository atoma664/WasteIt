package wasteit.wasteit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.manager.CategoryManager;
import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;
import wasteit.wasteit.MinFragment.EventFragment;
import wasteit.wasteit.MinFragment.ExpenseFragment;

public class StartActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        StartFragment.OnFragmentInteractionListener,
        NewEventFragment.OnNewEventListener,
        EventFragment.OnEventFragmentListener,
        EventDetailsDailyFragment.OnEventDetailsListener,
        NewExpenseDialogFragment.OnCreateExpenseListener,
        ExpenseFragment.OnExpenseListener,
        EventDailyFragment.OnEventDailyListener
{
    //region DM
    // The date of the date picker
    static EditText DateEdit;

    // My DB
    DatabaseHelper dbHelper;
    Event m_event;
    String m_strLastFrag;


    // Navigation
    Toolbar toolbar;
    android.support.v4.app.FragmentTransaction ft;

    NavigationView navigationView;
    TextView m_eventName;
    TextView m_eventDate;
    //endregion

    // Tags
    final String TAG_EVENT_MANAGER = "EventMangerFrag";
    final String TAG_EVENT_DAILY = "EventDailtFrag";
    final String TAG_START = "Start";
    final String TAG_NEW_EDIT_EVENT = "NewEditEventFrag";
    final String LAST_FRAG = "LastFragment";

    //region Getter Setter
    public Event getEvent() {
        return m_event;
    }

    // Get DB
    public DatabaseHelper GetDB()
    {
        return dbHelper;
    }

    // Setting the current event
    public void setEvent(Event m_event)
    {
        this.m_event = m_event;

        int nEventToSave;

        // If the event is not null
        if (m_event != null)
        {
            nEventToSave = m_event.getID();

            // Set the Menu screen details
            m_eventName.setText(m_event.getName());
            m_eventDate.setText(Services.FromDateToString(m_event.getStartDate()) + " - " +
                                Services.FromDateToString(m_event.getEndDate()));
        }
        else
        {
            // Init the event to none
            nEventToSave = -1;

            // Set the Menu screen details
            m_eventName.setText("No Current Event");
            m_eventDate.setText("");
        }

        // Save it to be the last event to show
        SharedPreferences.Editor editor = this.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.last_event_id), nEventToSave);
        editor.commit();
    }
    //endregion

    //region Interface Methods
    // Entering the New Event fragment
    public void onCreateEvent(View view)
    {
        Navagate(TAG_NEW_EDIT_EVENT);
    }

    @Override
    public void EditEvent(Event Event)
    {
        Navagate(TAG_NEW_EDIT_EVENT ,Event);
    }

    // Navagate between the models
    private void Navagate(String strFragName, Object... objects)
    {
        m_strLastFrag = strFragName;

        // Creating transaction
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = null;
        String strTag = null;

        switch (strFragName)
        {
            // Start Fragment
            case (TAG_START):
            {
                f = new StartFragment();
                strTag = TAG_START;

                break;
            }
            // Event manager
            case (TAG_EVENT_MANAGER):
            {
                f = EventManagerFragment.newInstance();
                strTag = TAG_EVENT_MANAGER;

                break;
            }
            // Event Daily
            case (TAG_EVENT_DAILY):
            {
                f = EventDailyFragment.newInstance(getEvent());
                strTag = TAG_EVENT_DAILY;

                break;
            }
            // Create Edit Event
            case (TAG_NEW_EDIT_EVENT):
            {
                if (objects.length == 0)
                {
                    f = NewEventFragment.newInstance();
                }
                else
                {
                    f = NewEventFragment.newInstance((Event) objects[0]);
                }
                strTag = TAG_NEW_EDIT_EVENT;

                break;
            }
        }

        // Saving to back stack
        ft.addToBackStack(strTag);
        ft.replace(R.id.fragmentContainer, f, strTag).commit();
    }

    // Create an expense
    public void UpdateExpenses()
    {
        // Updating the fragment
        EventDailyFragment eventDetail =
                (EventDailyFragment)getSupportFragmentManager().findFragmentByTag(TAG_EVENT_DAILY);
        eventDetail.Update();

        UpdateWidget();
    }

    private void UpdateWidget()
    {
        Intent intent = new Intent(this,AddExpenseWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AddExpenseWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }

    DialogFragment m_dCurr;

    // Show the selected event details
    public void ShowEventDetails(Event currEvent)
    {
        // Set the event to be the last viewed
        setEvent(currEvent);

        Navagate(TAG_EVENT_DAILY, currEvent);
    }

    // Show the the expense dialog
    public void ShowDialogNewExpense(Event currEvent, Date dCurr, Expense expense)
    {
        // Create the fragment and init the main with it
        DialogFragment newExpenseFrag =
                NewExpenseDialogFragment.newInstance(currEvent, dCurr, expense);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        newExpenseFrag.show(ft, "Dialog");
    }

    @Override
    public void UpdateEvent()
    {
        Navagate(TAG_EVENT_MANAGER);

        UpdateWidget();
    }

    public void CancelNewEditEvent()
    {
        Fragment returnFrag;
        ft = getSupportFragmentManager().beginTransaction();

        if (EventsManager.getInstance().getAllEvents().isEmpty())
        {
            returnFrag = new StartFragment();
        }
        else
        {
            returnFrag = EventManagerFragment.newInstance();
        }

        ft.replace(R.id.fragmentContainer, returnFrag).commit();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager
                imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void DeleteEvent(int nEventID)
    {
        if (getEvent() != null && getEvent().getID() == nEventID)
        {
            setEvent(null);
        }

        UpdateWidget();

        Navagate(TAG_EVENT_MANAGER);
    }
    //endregion

    //region Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating Manager
        CurrencyManager.newInstance(this);
        CategoryManager.newInstance(this);
        EventsManager.newInstance(this);
        ExpenseManager.newInstance(this);

        setContentView(R.layout.activity_start);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the DB
        dbHelper = new DatabaseHelper(getApplicationContext());
                m_eventName = ((TextView)navigationView.getHeaderView(0).findViewById(R.id.menu_event_name));
        m_eventDate = ((TextView)navigationView.getHeaderView(0).findViewById(R.id.menu_date));

        // Restore state members from saved instance
        if (savedInstanceState != null)
        m_strLastFrag = savedInstanceState.getString(LAST_FRAG);

        SetStartUp();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(LAST_FRAG, m_strLastFrag);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClose() {

    }

    public void SetStartUp()
    {
        // Get the last event id
        int nLastEvent = getPreferences(Context.MODE_PRIVATE)
                .getInt(getString(R.string.last_event_id), Consts.EVENT_NOT_EXIST_ERROR);

        Event last = null;

        // If start from widget
        if (getIntent().getExtras() != null)
        {
            nLastEvent = getIntent().getExtras().getInt("StartEvent");
        }

        // Get the last event
        if (nLastEvent != Consts.EVENT_NOT_EXIST_ERROR)
        {
            last = EventsManager.getInstance().getEvent(nLastEvent);
        }
        setEvent(last);

        if (m_strLastFrag == null)
        {
            // Check if the app has data
            // If not start the START fragment
            if (EventsManager.getInstance().getAllEvents().isEmpty())
            {
                Navagate(TAG_START);
            }
            else
            {


                // If none event has saves open event manager
                if (nLastEvent == Consts.EVENT_NOT_EXIST_ERROR)
                {
                    Navagate(TAG_EVENT_MANAGER);
                }
                else
                {
                    // If there is an event open is at startup
                    if (last != null)
                    {
                        Navagate(TAG_EVENT_DAILY);
                    }
                    else
                    {
                        Navagate(TAG_EVENT_MANAGER);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_my_event)
        {
            // Check if event has bees selected
            if (getEvent() != null)
            {
                Navagate(TAG_EVENT_DAILY);
            }
            else
            {
                Toast toast = Toast.makeText(this,  "No event has selected", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (id == R.id.menu_all_event)
        {
            Navagate(TAG_EVENT_MANAGER);
        }
        else if (id == R.id.menu_calander)
        {
        }
        else if (id == R.id.menu_tools)
        {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //region Date picker
    // Date Picker
    // Show the picker
    public void showTruitonDatePickerDialog(View v, Date dStart, Date dEnd)
    {
        showDatePickerDialog(v, dStart, dEnd, getSupportFragmentManager());
    }

    //region Date picker
    // Date Picker
    // Show the picker
    public static void showDatePickerDialog(View v, Date dStart, Date dEnd, FragmentManager fragmentManager)
    {
        // The edittext view
        DateEdit = (EditText) v;

        DialogFragment newFragment = new DatePickerFragment();

        // Check if there is a selected valid date already
        if (!DateEdit.getText().toString().isEmpty())
        {
            try
            {
                Date d = Services.FromStringToDate(DateEdit.getText().toString());
                ((DatePickerFragment)newFragment).SetDate(d);
            }
            catch (Exception e)
            {
                // TODO: Write to log
            }
        }

        ((DatePickerFragment)newFragment).SetValidDates(dStart, dEnd);
        newFragment.show(fragmentManager, "datePicker");
    }



    // Date Picker class
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        // Selected Date
        private Date m_CurrdDate = new Date();
        private Date m_startDate = null;
        private Date m_endDate = null;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            DatePickerDialog dPicker;

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Check if there is a selected date already
            if (m_CurrdDate != null)
            {
                year = m_CurrdDate.getYear() + 1900;
                month = m_CurrdDate.getMonth();
                day = m_CurrdDate.getDate();
            }
            else
            {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            dPicker = new DatePickerDialog(getActivity(), this, year, month, day);

            if (m_startDate != null) {

                Calendar cStart = Calendar.getInstance();
                cStart.setTime(m_startDate);
                dPicker.getDatePicker().setMinDate(cStart.getTimeInMillis());
            }

            if (m_endDate != null)
            {
                Calendar cEnd = Calendar.getInstance();
                cEnd.setTime(m_endDate);
                dPicker.getDatePicker().setMaxDate(cEnd.getTimeInMillis());
            }

            return (dPicker);
        }

        public void SetValidDates(Date dStart, Date dEnd)
        {
            m_startDate = dStart;
            m_endDate = dEnd;
        }

        // Init the current date
        public void SetDate(Date d)
        {
            m_CurrdDate = d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String str = "";

            // Building the string
            if (day < 10)
            {
                str += "0";
            }

            str += day + "/";

            if (month + 1 < 10)
            {
                str += "0";
            }

            str += month + 1 + "/" + year;

            DateEdit.setText(str);
        }
    }
    //endregion
}
