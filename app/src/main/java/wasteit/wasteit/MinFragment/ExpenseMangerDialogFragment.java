package wasteit.wasteit.MinFragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;
import wasteit.wasteit.Dialog.NewExpenseDialogFragment;
import wasteit.wasteit.R;


/**
 * create an instance of this fragment.
 */
public class ExpenseMangerDialogFragment extends DialogFragment implements View.OnClickListener{

    // Const
    private static final String ALL_EXPENSES = "AllExpenses";
    private static final String CURRENT_EVENT = "Event";
    private static final String CURRENT_DATE = "Date";

    // DM
    private Event m_event;
    private Date m_date;

    // Listener
    private OnFragmentInteractionListener mListener;

    private ArrayList<ExpenseManagerListener> m_listener;

    public void setDismisseListener(ExpenseManagerListener mListener)
    {
        if (m_listener == null)
        {
            m_listener = new ArrayList<ExpenseManagerListener>();
        }
        m_listener.add(mListener);
    }

    public ExpenseMangerDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currExpense Parameter 2.
     * @return A new instance of fragment ExpenseMangerDialogFragment.
     */
    public static ExpenseMangerDialogFragment newInstance(Event event, Date date) {
        ExpenseMangerDialogFragment fragment = new ExpenseMangerDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, event);
        args.putSerializable(CURRENT_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_date = (Date) getArguments().getSerializable(CURRENT_DATE);
            m_event = (Event)getArguments().getSerializable(CURRENT_EVENT);
        }

        if (savedInstanceState != null)
        {
            m_listener = (ArrayList<ExpenseManagerListener>) savedInstanceState.getSerializable("Listeners");
        }
    }

    public void UpdateExpenses(ArrayList<Expense> m_allExpenses)
    {
        BuildUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle(getString(R.string.today_expenses_title));


        View view = inflater.inflate(R.layout.fragment_expense_manger, container, false);
        view.findViewById(R.id.expense_dialog_add).setOnClickListener(this);

        if (Services.GetToDayAtStart().before(m_date))
        {
            view.findViewById(R.id.expense_dialog_add).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        BuildUI();
    }

    private void BuildUI()
    {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        ArrayList<Expense> all = ExpenseManager.gewInstance().getAllExpenseByEventAndDate(m_event.getID(), m_date);

        // Clear the container and set no expenses
        if (all.size() == 0)
        {
            transaction.replace(R.id.expense_manager_container, new NoExpenseFragment());
        }
        else
        {
            // Clear the container
            transaction.replace(R.id.expense_manager_container, ExpenseFragment.newInstance(all.get(0)));

            // Adding the rest of the expenses
            for (int n = 1 ; n < all.size(); n++)
            {
                Expense e = all.get(n);
                Fragment fEvent = ExpenseFragment.newInstance(e);

                transaction.add(R.id.expense_manager_container, fEvent);
            }
        }

        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        savedInstanceState.putSerializable("Listeners", m_listener);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.expense_dialog_add):
            {
                NewExpenseDialogFragment fnew = NewExpenseDialogFragment.newInstance(m_event, m_date, null);

                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                fnew.show(ft, "Dialog");

                break;
            }
        }
    }

    public interface ExpenseManagerListener
    {
        public void handleDialogClose();
    }

    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);

        for (ExpenseManagerListener mCurr: m_listener) {
            if (mCurr != null)
                mCurr.handleDialogClose();
        }
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
