package wasteit.wasteit;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import info.androidhive.sqlite.manager.CategoryManager;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Category;
import info.androidhive.sqlite.model.Currency;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;


/**
 * create an instance of this fragment.
 */
public class NewExpenseDialogFragment extends DialogFragment implements View.OnClickListener
{
    // Arg Const
    private static final String CURRNET_EVENT = "CurrentEvent";
    private static final String CURRNET_DATE = "CurrentDate";
    private static final String CURRENT_EXPENSE = "CurrentExpense";

    // Views
    private EditText m_expenseName;
    private EditText m_cost;
    private Spinner m_Category;
    Spinner m_spinner;
    private Expense m_expense;

    // Data
    private Event m_CurrentEvent;
    private ArrayList<Category> m_allCat;
    private Date m_CurrentDate;

    private OnCreateExpenseListener mListener;

    public NewExpenseDialogFragment()
    {
    }

    // Click event
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.new_expense_cancel_btn):
            {
                // Closing the dialog
                this.getDialog().dismiss();
                break;
            }
            case (R.id.new_expense_create_btn):
            {
                // Check validation
                if (IsNewExpenseValid())
                {
                    // Convert the currency to the event currency
                    double dCost = Double.parseDouble(m_cost.getText().toString());
                    dCost = CurrencyManager.ConvertFromToCurrency((Currency) m_spinner.getSelectedItem(),
                                                                m_CurrentEvent.getCurrency(),
                                                                dCost);

                    // Create expense
                    Expense eNew = new Expense(m_expenseName.getText().toString(),
                            (Category)m_Category.getSelectedItem(),
                            dCost,
                            m_CurrentDate, m_CurrentEvent, true, null);

                    // If the expense is new, create
                    if (m_expense == null)
                    {
                        int nExpenseID = ExpenseManager.gewInstance().CreateNewExpense(eNew);
                        eNew.setID(nExpenseID);

                    }
                    // Update the current expense
                    else
                    {
                        eNew.setID(m_expense.getID());
                        ExpenseManager.gewInstance().UpdateExpense(eNew);
                    }

                    // Call to update the changes
                    mListener.UpdateExpenses();

                    Toast t = Toast.makeText(getContext(), "New Expense was created", Toast.LENGTH_SHORT);
                    t.show();
                    this.dismiss();
                }

                break;
            }
        }
    }

    // Check validation
    public boolean IsNewExpenseValid() {

        boolean bIsValid = true;

        // Check that the Name is not empty
        if (m_expenseName.getText().toString().isEmpty())
        {
            m_expenseName.setError("Must have an Expense name!");
            bIsValid = false;
        }

        // Check the cost is not empty
        if (m_cost.getText().toString().isEmpty())
        {
            m_cost.setError("An expense must have Cost");
        }

        return bIsValid;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currEvent CurrentEvent.
     *  @param dDate CurrentDate
     *  @param allCat All Categories
     * @return A new instance of fragment NewExpenseDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewExpenseDialogFragment newInstance(Event currEvent, Date dDate, Expense expense) {
        NewExpenseDialogFragment fragment = new NewExpenseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRNET_EVENT, currEvent);
        args.putSerializable(CURRNET_DATE, dDate);
        args.putSerializable(CURRENT_EXPENSE, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_CurrentEvent = (Event)getArguments().getSerializable(CURRNET_EVENT);
            m_CurrentDate = (Date)getArguments().getSerializable(CURRNET_DATE);
            m_expense = (Expense)getArguments().getSerializable(CURRENT_EXPENSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_expense_dialog, container, false);

        // Get all the categories
        m_allCat = CategoryManager.getInstance().getAllCategories();

        // Saveand init the views
        m_expenseName = (EditText)view.findViewById(R.id.new_expose_name);
        m_cost =  (EditText)view.findViewById(R.id.new_expose_cost);
        m_Category = (Spinner) view.findViewById(R.id.new_expose_category);
        ((Button)view.findViewById(R.id.new_expense_cancel_btn)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.new_expense_create_btn)).setOnClickListener(this);

        // Init the category spinner
        ArrayAdapter<Category> adptCat = new ArrayAdapter<Category>(this.getContext(), android.R.layout.simple_spinner_item, m_allCat);
        adptCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_Category.setAdapter(adptCat);

        // Init the currency spinner
        m_spinner = ((Spinner)view.findViewById(R.id.new_expense_currency));
        ArrayAdapter<info.androidhive.sqlite.model.Currency> adapter =
                new ArrayAdapter<info.androidhive.sqlite.model.Currency>(this.getContext(), android.R.layout.simple_spinner_item, CurrencyManager.getInstance().getAllCurrencies());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner.setAdapter(adapter);

        // Init the default currency by the current event
        int nIndex = 0;

        for (Currency cCurr: CurrencyManager.getInstance().getAllCurrencies())
        {
            // Check if the currency matches the event
            if (cCurr.getID() == m_CurrentEvent.getCurrency().getID())
            {
                break;
            }

            nIndex++;
        }

        // Set the selection
        m_spinner.setSelection(nIndex);

        this.getDialog().setTitle("New Expense");

        // If edit an existing expense
        if (m_expense != null)
        {
            // Changing the title and set the values of the currrent expense
            this.getDialog().setTitle("Update Expense");
            m_cost.setText(String.valueOf(m_expense.getCost()));
            m_expenseName.setText(m_expense.getName());

            // Init the expense category
            int nCatIndex = 0;

            for (Category category: CategoryManager.getInstance().getAllCategories())
            {
                // Check if the expense category matches
                if (category.getID() == m_expense.getCategory().getID())
                {
                    m_Category.setSelection(nCatIndex);
                    break;
                }

                nIndex++;
            }
        }

        return view;
    }

     @Override
    public void onAttach(Context context) {
       super.onAttach(context);
        if (context instanceof OnCreateExpenseListener) {
            mListener = (OnCreateExpenseListener) context;
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

    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);

        if (mListener != null)
        mListener.onClose();
    }

    /**
     */
    public interface OnCreateExpenseListener {
        void UpdateExpenses();
        void onClose();
    }
}
