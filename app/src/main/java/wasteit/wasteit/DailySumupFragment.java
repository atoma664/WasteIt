package wasteit.wasteit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;

import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;
import wasteit.wasteit.MinFragment.NoExpenseFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailySumupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailySumupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailySumupFragment extends DialogFragment {
    private static final String CURRENT_DATE = "CurrentDate";

    private Date m_date;

    private OnFragmentInteractionListener mListener;

    public DailySumupFragment() {
        // Required empty public constructor
    }

    /**
     * @param date CurrentDate.
     * @return A new instance of fragment DailySumupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailySumupFragment newInstance(Date date) {
        DailySumupFragment fragment = new DailySumupFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_date = (Date)getArguments().getSerializable(CURRENT_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_sumup, container, false);

        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        ArrayList<Expense> expenses = ExpenseManager.gewInstance().getTotalExpensesForDate(m_date);

        if (expenses.isEmpty())
        {
            Fragment f = new NoExpenseFragment();

            ft.add(R.id.sum_up_container, f);
        }

        for (Expense expense: expenses) {

            Fragment s =
                    ShortDailyEventFragment.newInstance(expense.getEvent(), expense.getDate(), expense.getCost());
            ft.add(R.id.sum_up_container, s, expense.getEvent().getName());
        }

        ft.addToBackStack(null);
        ft.commit();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        ///    throw new RuntimeException(context.toString()
            ///        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
