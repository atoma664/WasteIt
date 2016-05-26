package wasteit.wasteit.MinFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.model.Event;
import wasteit.wasteit.EventDailyFragment;
import wasteit.wasteit.NewEventFragment;
import wasteit.wasteit.R;
import wasteit.wasteit.StartFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements View.OnClickListener {
    private static final String CURRENT_EVENT = "Current_Event";
    private OnEventFragmentListener mListener;

    private EventDailyFragment m_fDaily;
    private StartFragment m_fragStart;
    private NewEventFragment m_fragNew;

    public Event getEvent() {
        return m_CurrEvent;
    }

    private Event m_CurrEvent;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currEvent The current event
     * @return A new instance of fragment EventFragment.
     */
    public static EventFragment newInstance(Event currEvent) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, currEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            m_CurrEvent = (Event)getArguments().getSerializable(CURRENT_EVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        ((TextView)view.findViewById(R.id.event_frag_name)).setText(getEvent().getName());
        ((TextView)view.findViewById(R.id.event_frag_money)).setText(String.valueOf(getEvent().getMoneyAmount()));
        ((TextView)view.findViewById(R.id.event_frag_start_date)).setText(Services.FromDateToShortYearString(getEvent().getStartDate()));
        ((TextView)view.findViewById(R.id.event_frag_end_date)).setText(Services.FromDateToShortYearString(getEvent().getEndDate()));
        ((TextView)view.findViewById(R.id.event_frag_currency)).setText(getEvent().getCurrency().toString());

        ((LinearLayout)view.findViewById(R.id.event_frag_container)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.event_frag_delete)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.event_frag_edit)).setOnClickListener(this);

        ProgressBar p = (ProgressBar)view.findViewById(R.id.event_frag_progress);

        Date dStart = Services.GetDayAtStart(new Date());

        if (getEvent().getEndDate().before(dStart))
        {
            ((LinearLayout)view.findViewById(R.id.event_frag_container)).setBackground(getResources().getDrawable(R.drawable.backgroud_behave_past));
            p.setProgress(100);
            view.setAlpha((float) 0.5);
        }
        else if (getEvent().getStartDate().after(dStart))
        {
            ((LinearLayout)view.findViewById(R.id.event_frag_container)).setBackground(getResources().getDrawable(R.drawable.backround_behave_future));
            p.setProgress(0);
        }
        else
        {
            int nDaysPast = Services.DaysBetween(getEvent().getStartDate(), dStart);
            int nPercent = nDaysPast * 100 / getEvent().getDaysNum();

            p.setProgress(nPercent);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case (R.id.event_frag_container):
                mListener.ShowEventDetails(getEvent());
                break;
            case (R.id.event_frag_delete):
            {
                ShowAlertDialog();

                break;
            }
            case (R.id.event_frag_edit):
            {
                mListener.EditEvent(getEvent());
            }
        }
    }

    private void ShowAlertDialog()
    {
        // Creating the dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete '" + getEvent().getName() + "' Event");

        alertDialogBuilder
                .setMessage("Are You Sure")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        EventsManager.getInstance().DeleteEvent(getEvent().getID());

                        mListener.DeleteEvent(getEvent().getID());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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

    public interface OnEventFragmentListener {
        void ShowEventDetails(Event currEvent);
        void DeleteEvent(int nEventID);
        void EditEvent(Event Event);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof OnEventFragmentListener) {
            mListener = (OnEventFragmentListener) context;
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
}
