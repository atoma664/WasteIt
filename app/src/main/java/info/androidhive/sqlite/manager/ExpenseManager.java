package info.androidhive.sqlite.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.model.Currency;
import info.androidhive.sqlite.model.Expense;

/**
 * Created by LENOVO on 13/05/2016.
 */
public class ExpenseManager implements ManagerInterface {
    static ExpenseManager m_nInstance;

    public DatabaseHelper getDB() {
        return m_db;
    }

    public void setM_db(DatabaseHelper m_db) {
        this.m_db = m_db;
    }

    DatabaseHelper m_db;

    private ExpenseManager(Context context)
    {
        m_db = new DatabaseHelper(context);
    }

    public static ExpenseManager newInstance(Context context)
    {
        if (m_nInstance == null)
        {
            m_nInstance = new ExpenseManager(context);
        }

        return m_nInstance;
    }

    public ArrayList<Expense> getAllExpenseByEventAndDate(int nEventID, Date dDate)
    {
        return getDB().getAllExpensesByDateAndEvent(nEventID, dDate);
    }

    public int DeleteExpense(int nExpenseID)
    {
        return (int)getDB().deleteExpense(nExpenseID);
    }

    public int CreateNewExpense(Expense expense)
    {
        return (int)getDB().createExpense(expense);
    }

    public int UpdateExpense(Expense expense)
    {
        return (int)getDB().updateExpense(expense);
    }

    public static ExpenseManager gewInstance()
    {
        if (m_nInstance == null)
        {
            Log.println(Log.ERROR, "Expense Manager", "Must Create new instant");
        }

        return m_nInstance;
    }

    public double getTotalExpensesByEventAndDate(int EventID, Date dCurr)
    {
        return getDB().getTotalExpensesByEventAndDate(EventID, dCurr);
    }

    public ArrayList<Expense> getTotalExpensesForDate(Date dCurr)
    {
        return getDB().getTotalExpensesForDate(dCurr);
    }

    public HashMap<Date, Double> getExpensesSumByDate()
    {
        ArrayList<Expense> allexpenses = getDB().getGeneralExpensesByDay();
        HashMap<Date, Double> sumExpenses = new HashMap<>();

        for (Expense expense:allexpenses)
        {
            Double dCostConverted =
                    CurrencyManager.ConvertFromToCurrency(expense.getEvent().getCurrency(), CurrencyManager.getDefault(),expense.getCost());

            if (sumExpenses.get(expense.getDate())!= null)
            {
                dCostConverted += sumExpenses.get(expense.getDate());
            }

            sumExpenses.put(expense.getDate(), dCostConverted);
        }

        return sumExpenses;
    }

    @Override
    public void CloseManager() {

    }
}
