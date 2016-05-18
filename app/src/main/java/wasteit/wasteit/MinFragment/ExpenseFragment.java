package wasteit.wasteit.MinFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;
import wasteit.wasteit.R;


/**
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXPENSE_ARG = "CurrentExpense";

    private Expense m_Curr;

    public Expense getExpense() {
        return m_Curr;
    }

    private OnExpenseListener mListener;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param expense CurrentExpense.
     * @return A new instance of fragment ExpenseFragment.
     */
    public static ExpenseFragment newInstance(Expense expense) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXPENSE_ARG, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_Curr = (Expense) getArguments().getSerializable(EXPENSE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        ((TextView)view.findViewById(R.id.expense_frag_name)).setText(m_Curr.getName());
        ((TextView)view.findViewById(R.id.expense_frag_currency)).setText(m_Curr.getEvent().getCurrency().toString());
        ((TextView)view.findViewById(R.id.expense_frag_money)).setText(String.valueOf(Services.ReturnRound(m_Curr.getCost())));
        ((TextView)view.findViewById(R.id.expense_frag_category)).setText(m_Curr.getCategory().getCategoryName());
        ((ImageView)view.findViewById(R.id.expense_frag_delete)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.expense_frag_edit)).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case (R.id.expense_frag_delete):
            {
                ExpenseManager.gewInstance().DeleteExpense(getExpense().getID());

                mListener.UpdateExpenses();
                break;
            }
            case (R.id.expense_frag_edit):
            {
                mListener.ShowDialogNewExpense(m_Curr.getEvent(),m_Curr.getDate() , m_Curr);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseListener) {
            mListener = (OnExpenseListener) context;
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
     */
    public interface OnExpenseListener {
        void UpdateExpenses();
        void ShowDialogNewExpense(Event currEvent, Date dCurr, Expense expense);
    }
}
