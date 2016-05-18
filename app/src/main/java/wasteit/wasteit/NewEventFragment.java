package wasteit.wasteit;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment implements View.OnClickListener {

    final static String CURRENT_EVENT = "CurrentEvent";

    // Date Formater from string to Date
    private OnNewEventListener mListener;
    EditText etName;
    EditText etMoney;
    EditText etStartDate;
    EditText etEndDate;
    EditText etPeopleNum;
    TextView twNumOfDays;
    boolean bIsToSave;
    Spinner spnCurrency;

    Event m_event;

    public Event getEvent() {
        return m_event;
    }

    public void setEvent(Event m_event) {
        this.m_event = m_event;
    }

    public NewEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.new_event_end_date):
            {
            }
            case ( R.id.new_event_start_date):
            {
                mListener.showTruitonDatePickerDialog(v, null, null);

                break;
            }
            case (R.id.new_event_create_btn):
            {
                final Event eNew = CreateEvent();

                if (eNew != null)
                {
                    if (getEvent() == null)
                    {
                        int nID = EventsManager.getInstance().CreateNewEvent(eNew);

                        if (nID != Consts.SAVE_ERROR)
                        {
                            eNew.setId(nID);
                            Toast toast = Toast.makeText(getContext(), "'" + eNew.getName() + "' Event was created", Toast.LENGTH_SHORT);
                            toast.show();
                            mListener.ShowEventDetails(eNew);
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getContext(), "'" + eNew.getName() + "' Event Creation failed", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        StartActivity.hideKeyboardFrom(getContext(), getView());
                    }
                    else
                    {
                        Date dStart = Services.FromStringToDate(etStartDate.getText().toString());
                        Date dEnd = Services.FromStringToDate(etEndDate.getText().toString());
                        String strMessage = "";
                        eNew.setId(getEvent().getID());

                        bIsToSave = true;

                        if (dEnd.before(getEvent().getEndDate()) || dStart.after(getEvent().getStartDate())) {
                            bIsToSave = false;

                            strMessage = "The New Dates doesn't include some of the Original Dates. Data of these dates will be deleted. Are you sure";

                            // Creating the dialog
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            alertDialogBuilder.setTitle("Alert");

                            alertDialogBuilder
                                    .setMessage(strMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            EventsManager.getInstance().UpdateEvent(eNew);
                                            mListener.UpdateEvent();
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }

                        if (bIsToSave)
                        {
                            StartActivity.hideKeyboardFrom(getContext(), getView());
                            EventsManager.getInstance().UpdateEvent(eNew);
                            mListener.UpdateEvent();
                        }
                    }
                }

                break;
            }
            case (R.id.new_event_cancel_btn):
            {
                mListener.CancelNewEditEvent();

                break;
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewEventFragment.
     */
    public static NewEventFragment newInstance(Event e) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, e);
        fragment.setArguments(args);

        return fragment;
    }

    public static NewEventFragment newInstance() {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_event = (Event) getArguments().getSerializable(CURRENT_EVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_event, container, false);

        // Init the Views
        etName = ((EditText)view.findViewById(R.id.new_event_name));
        etMoney = ((EditText)view.findViewById(R.id.new_event_money));
        etStartDate = ((EditText)view.findViewById(R.id.new_event_start_date));
        etEndDate = ((EditText)view.findViewById(R.id.new_event_end_date));
        etPeopleNum = ((EditText)view.findViewById(R.id.new_event_people_num));
        twNumOfDays = ((TextView)view.findViewById(R.id.new_event_days_num));
        Button create_button = (Button) view.findViewById(R.id.new_event_create_btn);
        view.findViewById(R.id.new_event_cancel_btn).setOnClickListener(this);
        spnCurrency = ((Spinner) view.findViewById(R.id.new_event_currency));

        // Listen to the create button
        create_button.setOnClickListener(this);

        // Listen to the date click for opening date picker
        etEndDate.setOnClickListener(this);
        etStartDate.setOnClickListener(this);

        if (getEvent() == null) {

            spnCurrency.setEnabled(true);

            // Setting default dates
            // Start today
            Calendar c = Calendar.getInstance(Locale.getDefault());
            Date date = c.getTime();
            String strToday = Services.FromDateToString(date);

            // End Tomorrow
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            String strTomorrow = Services.FromDateToString(date);

            // Init the Edit text
            this.etStartDate.setText(strToday);
            this.etEndDate.setText(strTomorrow);
        }
        else
        {
            spnCurrency.setEnabled(false);

            this.etStartDate.setText(Services.FromDateToString(getEvent().getStartDate()));
            this.etEndDate.setText(Services.FromDateToString(getEvent().getEndDate()));
            this.etName.setText(getEvent().getName());
            this.etMoney.setText(String.valueOf(getEvent().getMoneyAmount()));
            this.etPeopleNum.setText(String.valueOf(getEvent().getNumOfPeople()));
            this.twNumOfDays.setText(String.valueOf(getEvent().getDaysNum()));
            ((TextView)view.findViewById(R.id.new_event_title)).setText(getString(R.string.update_event_title));
             create_button.setText("SAVE");
        }

        // Create the currency values
        spnCurrency = (Spinner)view.findViewById(R.id.new_event_currency);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<info.androidhive.sqlite.model.Currency> adapter =
                new ArrayAdapter<info.androidhive.sqlite.model.Currency>(this.getContext(), android.R.layout.simple_spinner_item, CurrencyManager.getInstance().getAllCurrencies());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurrency.setAdapter(adapter);

        // Listen to the dates changes
        etStartDate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try
                {
                    StartDateChanges();
                }
                catch (Exception e)
                {

                }
            }
        });

        etEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try
                {
                    EndDateChanges();
                }
                catch (Exception e)
                {
                }
            }
        });

        return view;
    }

    private void EndDateChanges()
    {
        Date dStart = Services.FromStringToDate(etStartDate.getText().toString());
        Date dEnd = Services.FromStringToDate(etEndDate.getText().toString());

        // Check that the end date is before the start date
        if (dEnd.before(dStart))
        {
            // Set the end value to the prev day
            Calendar c = Calendar.getInstance();
            c.setTime(dEnd);
            c.add(Calendar.DATE, -1);
            dStart = c.getTime();
            etStartDate.setText(Services.FromDateToString(c.getTime()));
        }

        int nDays = Services.DaysBetween(dStart, dEnd) + 1; // TODO: check days extra
        twNumOfDays.setText(String.valueOf(nDays));
    }

    private void StartDateChanges()
    {
        Date dStart = Services.FromStringToDate(etStartDate.getText().toString());
        Date dEnd = Services.FromStringToDate(etEndDate.getText().toString());

        // Check that the end date is before the start value
        if (dEnd.before(dStart))
        {
            // Set the end value to the next day
            Calendar c = Calendar.getInstance();
            c.setTime(dStart);
            c.add(Calendar.DATE, 1);
            dEnd = c.getTime();
            etEndDate.setText(Services.FromDateToString(c.getTime()));
        }

        // Calc the number of days
        int nDays =  Services.DaysBetween(dStart, dEnd) + 1; // TODO: check days extra
        twNumOfDays.setText(String.valueOf(nDays));
    }

    // Create an event
    public Event CreateEvent()
    {
        // Get the values from the page
        String strEventName = this.etName.getText().toString();
        String strNumOfPeople = this.etPeopleNum.getText().toString();
        String strStartDate = etStartDate.getText().toString();
        String strEndDate = etEndDate.getText().toString();
        String strMoney = etMoney.getText().toString();
        Date dStart = null;
        Date dEnd = null;
        int nNumOfDays =  Integer.parseInt(((TextView)getView().findViewById(R.id.new_event_days_num)).getText().toString());
        Object o = ((Spinner)getView().findViewById(R.id.new_event_currency)).getSelectedItem();
        String strCurreny = o.toString();
        boolean bIsValid = true;
        double nMoney;

        // Check validation
        if (strEventName.isEmpty())
        {
            etName.setError("Must have a trip name!");

            bIsValid = false;
        }

        if (strMoney.isEmpty())
        {
            etMoney.setError("Must have a positive amount of money");

             bIsValid = false;
        }

        if (strNumOfPeople.isEmpty())
        {
            etPeopleNum.setError("Must have a positive amount of people");

            bIsValid = false;
        }

        if (strStartDate.isEmpty() || !Services.IsValidDate(strStartDate) )
        {
            etStartDate.setError("Date in invalid. Date format must be " + getString(R.string.date_format));

            bIsValid = false;
        }

        if (strEndDate.isEmpty() || !Services.IsValidDate(strEndDate))
        {
            etEndDate.setError("Date in invalid. Date format must be " + getString(R.string.date_format));

            bIsValid = false;
        }

        try
        {
            dStart = Services.FromStringToDate(etStartDate.getText().toString());
            dEnd = Services.FromStringToDate(etEndDate.getText().toString());
        }
        catch (Exception e)
        {
            bIsValid = false;
        }

        // If all the validation is ok create an event
        if (bIsValid)
        {
            return new Event(-999, strEventName, Double.parseDouble(strMoney), dStart,
                    dEnd, nNumOfDays, Integer.parseInt(strNumOfPeople), (info.androidhive.sqlite.model.Currency) spnCurrency.getSelectedItem());
        }

        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewEventListener) {
            mListener = (OnNewEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewEventListener {

        // Show picker
        void showTruitonDatePickerDialog(View view, Date start, Date end);

        // Create the event
        void ShowEventDetails(Event newEvent);
        void CancelNewEditEvent();
        void UpdateEvent();
    }
}
