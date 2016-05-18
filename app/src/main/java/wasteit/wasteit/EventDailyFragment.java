package wasteit.wasteit;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDailyFragment extends Fragment implements View.OnClickListener {

    // Args
    private static final String CURRENT_EVENT = "Event";
    private OnEventDailyListener mListener;

    // Views
    ImageView nNext;
    ImageView nPrev;
    EventCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    // Data
    private Event m_event;
    private Date m_dToShow;

    public Event getEvent()
    {
        return m_event;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Update();
    }

    @Override
    public void onClick(View v)
    {
        // Calc the present day and how many days to jump from the start
        Date dPresent =
                Services.FromStringToDate(((EditText)getView().findViewById(R.id.event_daily_current_date)).getText().toString());
        int nDaysNext = Services.DaysBetween(getEvent().getStartDate(), dPresent);

        switch (v.getId())
        {
            // Jump to next day
            case (R.id.event_daily_next_day):
            {
                mViewPager.setCurrentItem(nDaysNext + 1, true);

                break;
            }
            // Jump to prev day
            case (R.id.event_daily_prev_day):
            {
                mViewPager.setCurrentItem(nDaysNext - 1, true);

                break;
            }
            // Open date picker with range dates of the event
            case (R.id.event_daily_current_date):
            {
                mListener.showTruitonDatePickerDialog(v, getEvent().getStartDate(), getEvent().getEndDate());

                break;
            }

        }
    }

    public EventDailyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventDailyFragment.
     */
    public static EventDailyFragment newInstance(Event e) {
        EventDailyFragment fragment = new EventDailyFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, e);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_event = (Event)getArguments().getSerializable(CURRENT_EVENT);
        }
    }

    // Update the details of the current fragment
    public void Update()
    {
        for (int nIndex = 0; nIndex < mViewPager.getAdapter().getCount(); nIndex++)
        {
            EventDetailsDailyFragment f = ((EventCollectionPagerAdapter)mViewPager.getAdapter()).getFragment(nIndex);

            if (f != null)
            f.UpdateInfo();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_daily, container, false);


        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new EventCollectionPagerAdapter(
                        getChildFragmentManager());
        mDemoCollectionPagerAdapter.setEvent(m_event);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        ((TextView)view.findViewById(R.id.event_daily_name)).setText(getEvent().getName());

        // Init the views
        nNext = (ImageView) view.findViewById(R.id.event_daily_next_day);
        nPrev = (ImageView) view.findViewById(R.id.event_daily_prev_day);
        view.findViewById(R.id.event_daily_next_day).setOnClickListener(this);
        view.findViewById(R.id.event_daily_prev_day).setOnClickListener(this);
        view.findViewById(R.id.event_daily_current_date).setOnClickListener(this);

        // Listen to the text change in the date
        ((EditText)view.findViewById(R.id.event_daily_current_date)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Go to the selected page
                Date dSelected = Services.FromStringToDate(s.toString());
                int nIndex = Services.DaysBetween(getEvent().getStartDate(), dSelected);
                mViewPager.setCurrentItem(nIndex);
            }
        });

        // Init today days
        if (savedInstanceState == null) {
            m_dToShow = Services.GetToDayAtStart();
        }
        else
        {
            m_dToShow = (Date)savedInstanceState.getSerializable("CurrentDate");
        }

        int nDaysNext = Services.DaysBetween(getEvent().getStartDate(), m_dToShow);

        // If the event has already ended set the current date to be the end date
        if (savedInstanceState == null && m_dToShow.after(getEvent().getEndDate()))
        {
            ((EditText)view.findViewById(R.id.event_daily_current_date))
                    .setText(Services.FromDateToString(getEvent().getEndDate()));
            mViewPager.setCurrentItem(getEvent().getDaysNum());

            nNext.setVisibility(View.INVISIBLE);
        }
        // If the event havent started yes init to first page
        else if (savedInstanceState == null && m_dToShow.before(getEvent().getStartDate()))
        {
            ((EditText)view.findViewById(R.id.event_daily_current_date))
                    .setText(Services.FromDateToString(getEvent().getStartDate()));
            mViewPager.setCurrentItem(0);

            nPrev.setVisibility(View.INVISIBLE);
        }
        else
        {
            // Jump to today by calc the day num from the start till today
            m_dToShow = Services.GetNextPrevDate(getEvent().getStartDate(), nDaysNext);

            SetNextPrevVisible(m_dToShow);
            ((EditText)view.findViewById(R.id.event_daily_current_date))
                    .setText(Services.FromDateToString(m_dToShow));
            mViewPager.setCurrentItem(nDaysNext);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                // Change the date when the page is changed
                Date dToShow = Services.GetNextPrevDate(getEvent().getStartDate(), position);
                SetNextPrevVisible(dToShow);
                ((EditText)getView().findViewById(R.id.event_daily_current_date))
                        .setText(Services.FromDateToString(dToShow));

                // Update the presented date
                m_dToShow = dToShow;
            }
        });

        return view;
    }



    // Set the visibility of the next/prev by the start end date
    private void SetNextPrevVisible(Date dToShow)
    {
        nNext.setVisibility(View.VISIBLE);
        nPrev.setVisibility(View.VISIBLE);

        if (dToShow.equals(getEvent().getStartDate()))
        {
            nPrev.setVisibility(View.INVISIBLE);
        }

        if (dToShow.equals(getEvent().getEndDate()))
        {
            nNext.setVisibility(View.INVISIBLE);
        }

    }

    public interface OnEventDailyListener {
        void showTruitonDatePickerDialog(View v, Date dStart, Date dEnd);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventDailyListener) {
            mListener = (OnEventDailyListener) context;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        savedInstanceState.putSerializable("CurrentDate", m_dToShow);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class EventCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public EventCollectionPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    HashMap mPageReferenceMap = new HashMap();
    Event m_Curr;

    public void setEvent(Event m_Curr)
    {
        this.m_Curr = m_Curr;
    }

    @Override
    public Fragment getItem(int i)
    {
        // Creating the fragment to the current date
        Date dNext = Services.GetNextPrevDate(m_Curr.getStartDate(), i);
        EventDetailsDailyFragment fragment = EventDetailsDailyFragment.newInstance(m_Curr);
        fragment.setCurrentDate(dNext);
        mPageReferenceMap.put(i, fragment);

        return (Fragment) fragment;
    }

    // Remove fragment when distroyed
    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    // Add fragment when creates
    public EventDetailsDailyFragment getFragment(int key) {
        return (EventDetailsDailyFragment)mPageReferenceMap.get(key);
    }

    @Override
    public int getCount() {
        return m_Curr.getDaysNum();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

}
