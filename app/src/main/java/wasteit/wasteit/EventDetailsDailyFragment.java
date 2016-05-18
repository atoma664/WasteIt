package wasteit.wasteit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;
import wasteit.wasteit.MinFragment.ExpenseMangerDialogFragment;


/**
 * create an instance of this fragment.
 */
public class EventDetailsDailyFragment extends Fragment implements View.OnClickListener, ExpenseMangerDialogFragment.ExpenseManagerListener {

    //region Views
    private TextView m_totalbalance;
    private TextView m_dailybalance;
    private TextView m_dailyAvgExpenses;
    private TextView m_dailyAllowance;
    private TextView m_totalbalancePercent;
    private ProgressBar m_dbProgress;
    private Fragment fExpense;
    private FloatingActionButton m_btnAdd;
    private TextView m_total;
    private TextView m_totalCurrency;
    private TextView m_DailyCurrency;
    private View m_container;
    private DialogFragment m_currDialog;

    //endregion

    //region Frag event
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_daily_details, container, false);

        InitInfo(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventDetailsListener) {
            mListener = (OnEventDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void handleDialogClose()
    {
        m_currDialog = null;
    }

    /**
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEventDetailsListener {
        void ShowDialogNewExpense(Event currEvent, Date dCurr, Expense expense);
        void showTruitonDatePickerDialog(View view, Date dStart, Date dEnd);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void ShowExpensesDialog(ArrayList<Expense> allExpenses)
    {
        // Create the fragment and init the main with it
        ExpenseMangerDialogFragment newExpenseFrag = ExpenseMangerDialogFragment.newInstance(getCurrnetEvent(), getCurrentDate());
        m_currDialog = newExpenseFrag;
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        newExpenseFrag.show(ft, "Dialog");

        newExpenseFrag.setDismisseListener(this);
    }

    /**
     * @param CurrEvent The event to show.
     * @return A new instance of fragment EventDetailsDailyFragment.
     */
    public static EventDetailsDailyFragment newInstance(Event CurrEvent) {
        EventDetailsDailyFragment fragment = new EventDetailsDailyFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EVENT, CurrEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_currnet_event = (Event)getArguments().getSerializable(CURRENT_EVENT);
        }

        if (savedInstanceState != null)
        {
            setCurrentDate((Date)savedInstanceState.getSerializable("CurrentDate"));
        }
    }
    //endregion

    //region Getter Setter
    public void setBalancePercent(int nbalancePercent) {
        this.m_totalbalancePercent.setText("%" + String.valueOf(nbalancePercent));
    }

    public void setTotal(double d_total) {
        this.m_total.setText(String.valueOf(Services.ReturnRound(d_total)));
    }

    public Date getCurrentDate() {
        return m_CurrentDate;
    }

    public void setCurrentDate(Date m_CurrentDate)
    {
        this.m_CurrentDate = m_CurrentDate;

        // Update the info according to the date
   //     UpdateInfo();
    }

    // Set the percent value
    public void setProgress(int nProgress)
    {
        this.m_dbProgress.setProgress(nProgress);

        // Change the progress bar color according to the value
        if (nProgress > 50)
        {
            m_dbProgress.getProgressDrawable().setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else if (nProgress > 10)
        {
            m_dbProgress.getProgressDrawable().setColorFilter(
                    Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else
        {
            m_dbProgress.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public double getSpentTillToday() {
        return m_dSpentTillToday;
    }

    public double getSpentToday()
    {
        return (getSpentTillNow() - getSpentTillToday());
    }

    public void setSpentTillToday(double m_dSpentTillToday) {
        this.m_dSpentTillToday = m_dSpentTillToday;
    }

    public double getSpentTillNow() {
        return m_dSpentTillNow;
    }

    public void setSpentTillNow(double m_dSpentTillNew) {
        this.m_dSpentTillNow = m_dSpentTillNew;
    }

    public void setTotalbalance(double m_totalbalance)
    {
     /*   this.m_totalbalance.setTextColor(getResources().getColor(R.color.lightBlue));
        this.m_totalCurrency.setTextColor(getResources().getColor(R.color.lightBlue));

        // If the balance is negative change the color to red
        if (m_totalbalance < 0)
        {
            this.m_totalbalance.setTextColor(getResources().getColor(R.color.alert));
            this.m_totalCurrency.setTextColor(getResources().getColor(R.color.alert));
        }*/

        this.m_totalbalance.setText(String.valueOf(m_totalbalance));
    }

    public void setDailybalance(double m_dailybalance)
    {
        m_container.setBackgroundColor(getResources().getColor(R.color.lightBlue));

        // If the balance is negative change the color to red
        if (m_dailybalance < 0)
        {
            m_container.setBackgroundColor(getResources().getColor(R.color.alert));
        }

        this.m_dailybalance.setText(String.valueOf(m_dailybalance));
    }

    public void setDailyAvgExpenses(double dailyAvgExpenses) {
        this.m_dailyAvgExpenses.setText(String.valueOf(dailyAvgExpenses));
    }

    public void setDailyAllowance(double dailyAllowance) {
        this.m_dailyAllowance.setText(String.valueOf(dailyAllowance));
    }

    public void setCurrnet_event(Event m_currnet_event) {
        this.m_currnet_event = m_currnet_event;
    }

    public Event getCurrnetEvent() {
        return m_currnet_event;
    }

    //endregion

    //region DM

    // Const
    private static final String CURRENT_EVENT = "Current_event";

    private Event m_currnet_event;
    private Date m_CurrentDate;
    private OnEventDetailsListener mListener;
    private double m_dSpentTillToday = 0;
    private double m_dSpentTillNow = 0;
    //endregion

    //region Ctor
    public EventDetailsDailyFragment() {
    }
    //endregion

    //region Events
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            // Update to next day
            case (R.id.event_daily_next_day):
                    {
                            setCurrentDate(Services.GetNextPrevDate(getCurrentDate(), 1));
                            break;
                    }
            // Update to prev day
            case(R.id.event_daily_prev_day):
                    {
                            setCurrentDate(Services.GetNextPrevDate(getCurrentDate(), -1));
                            break;
                    }
            // Show the dialog of add expense
            case (R.id.addExpense):
            {
                mListener.ShowDialogNewExpense(getCurrnetEvent(), getCurrentDate(), null);

                break;
            }
            case (R.id.event_daily_show_expenses_btn):
            {
                ShowExpensesDialog(ExpenseManager.gewInstance().getAllExpenseByEventAndDate(getCurrnetEvent().getID(), getCurrentDate()));

                break;
            }
            // Open date time picker
            case (R.id.event_daily_current_date):
            {
                mListener.showTruitonDatePickerDialog(view, getCurrnetEvent().getStartDate(), getCurrnetEvent().getEndDate());
            }
        }
    }
    //endregion




    //region Methods
    // Update the information according to the new value
    public void UpdateInfo()
    {
        // Set the add, next\prev day button to be visible
        m_btnAdd.setVisibility(View.VISIBLE);

        // Calc the total expenses until now
        Date cYesterday = Services.GetNextPrevDate(getCurrentDate(), -1);
        setSpentTillNow(ExpenseManager.gewInstance().getTotalExpensesByEventAndDate(getCurrnetEvent().getID(), getCurrentDate()));

        // Calc the total expenses until today (not include the current day)
        setSpentTillToday(ExpenseManager.gewInstance().getTotalExpensesByEventAndDate(getCurrnetEvent().getID(), cYesterday));

        // Update the current day expenses
        if (m_currDialog != null)
        {
            ((ExpenseMangerDialogFragment) m_currDialog)
                    .UpdateExpenses(ExpenseManager.gewInstance()
                            .getAllExpenseByEventAndDate(getCurrnetEvent().getID(), getCurrentDate()));
        }

        // Calc the values
        // Balances
        double dBalance = getCurrnetEvent().getMoneyAmount() - getSpentTillNow();
        double dBalanceTillToday = getCurrnetEvent().getMoneyAmount() - getSpentTillToday();
        int nBalancePercent = (int)Math.floor(dBalance * 100 / getCurrnetEvent().getMoneyAmount());

        // Days
        int nDaysPast = Services.DaysBetween(getCurrnetEvent().getStartDate(),
               getCurrentDate());
        int nDaysLeft = getCurrnetEvent().getDaysNum() - nDaysPast;

        // Daily var
        double dExpensesAllowense;
        double dAverage = 0;
        double dBalancePerDay;

        boolean bIsLastDayToday = !getCurrentDate().after(Services.GetDayAtStart(new Date()));

        // If this is the last day the allowance equals the balance
        if (nDaysLeft <= 1 && bIsLastDayToday)
        {
            dExpensesAllowense = dBalance;
        }
        else
        {
            // If the dae is in the future
            if (!bIsLastDayToday)
            {
                // Hide add expense button
                m_btnAdd.setVisibility(View.INVISIBLE);

                // Set the days lest and the day past to according for tosay for the allowance and avarage calcutarion
                nDaysLeft = Services.DaysBetween(Services.GetNextPrevDate(Services.GetDayAtStart(new Date()), -1), getCurrnetEvent().getEndDate());
                nDaysPast = getCurrnetEvent().getDaysNum() - nDaysLeft;

                dBalanceTillToday = getCurrnetEvent().getMoneyAmount() - ExpenseManager.gewInstance().getTotalExpensesByEventAndDate(getCurrnetEvent().getID(), Services.GetNextPrevDate(Services.GetDayAtStart(new Date()),-1));
            }

            // Calc the allowance acording to the remaining days and money (of the day before
            dExpensesAllowense = dBalanceTillToday /  nDaysLeft;
        }

        // Calc the balance according to the allowance and the money already spent
        dBalancePerDay = dExpensesAllowense - getSpentToday();

        // Calc the avarage according to the spent money and the days that have past
        if (nDaysPast >= 0)
        {
            dAverage = getSpentTillNow() / (nDaysPast + 1);
        }

        // Set the values of the screen
        setTotalbalance(Services.ReturnRound(dBalance));
        setDailybalance(Services.ReturnRound(dBalancePerDay));
        setDailyAllowance(Services.ReturnRound(dExpensesAllowense));
    }

    // Init the first value when the frag creates
    public void InitInfo(View v)
    {
        // Date the views to the DM
        m_totalbalance = (TextView)v.findViewById(R.id.event_daily_balance_total);
        m_dailybalance = (TextView)v.findViewById(R.id.event_daily_balance_daily);
        m_dailyAllowance =  (TextView)v.findViewById(R.id.event_daily_daily_allowance);
        m_btnAdd = (FloatingActionButton) v.findViewById(R.id.addExpense);
        m_container = v.findViewById(R.id.event_daily_daily_container);

        ((TextView)v.findViewById(R.id.event_daily_money)).setText(String.valueOf(m_currnet_event.getMoneyAmount()));

        m_totalCurrency = ((TextView)v.findViewById(R.id.event_daily_balance_total_currency));
        m_DailyCurrency =  ((TextView)v.findViewById(R.id.event_daily_balance_daily_currency));

        String strCurrency = getCurrnetEvent().getCurrency().toString();
        m_totalCurrency.setText(strCurrency);
        m_DailyCurrency.setText(strCurrency);
        ((TextView)v.findViewById(R.id.event_daily_daily_allowance_currency)).setText(strCurrency);
        ((TextView)v.findViewById(R.id.event_daily_badget_total_currency)).setText(strCurrency);

        v.findViewById(R.id.event_daily_show_expenses_btn).setOnClickListener(this);

        // Listen to the buttons
        m_btnAdd.setOnClickListener(this);

        if (m_CurrentDate == null)
        {
            m_CurrentDate = Services.GetToDayAtStart();
        }

        UpdateInfo();
    }
    //endregion

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        savedInstanceState.putSerializable("CurrentDate", getCurrentDate());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
