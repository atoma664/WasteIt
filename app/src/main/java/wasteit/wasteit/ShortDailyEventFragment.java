package wasteit.wasteit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShortDailyEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShortDailyEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortDailyEventFragment extends Fragment {
    private static final String CURRENT_EVENT = "Event";
    private static final String CURRENT_DATE = "Date";
    private static final String CURRENT_AMOUNT = "Amount";

    private Event m_event;
    private Date m_date;
    private double m_amount;

    private OnFragmentInteractionListener mListener;

    public ShortDailyEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param event CurretnEvent.
     * @param date Selected Date.
     * @return A new instance of fragment ShortDailyEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShortDailyEventFragment newInstance(Event event, Date date, double dAmount) {
        ShortDailyEventFragment fragment = new ShortDailyEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, event);
        args.putSerializable(CURRENT_DATE, date);
        args.putDouble(CURRENT_AMOUNT, dAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_event =(Event) getArguments().getSerializable(CURRENT_EVENT);
            m_date = (Date) getArguments().getSerializable(CURRENT_DATE);
            m_amount = getArguments().getDouble(CURRENT_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_short_daily_event, container, false);

        ((TextView)view.findViewById(R.id.event_short_date_name)).setText(m_event.getName());
        ((TextView)view.findViewById(R.id.event_short_date_currency)).setText(m_event.getCurrency().toString());
        ((TextView)view.findViewById(R.id.event_short_date_amount)).setText(String.valueOf(Services.ReturnRound(m_amount)));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
           // throw new RuntimeException(context.toString()
          //          + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
