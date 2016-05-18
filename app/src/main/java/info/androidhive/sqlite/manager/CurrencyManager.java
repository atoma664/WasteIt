package info.androidhive.sqlite.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.model.Currency;
import info.androidhive.sqlite.model.Expense;

/**
 * Created by LENOVO on 13/05/2016.
 */
public class CurrencyManager implements ManagerInterface {
    DatabaseHelper m_db;
    static CurrencyManager m_instance;

    public DatabaseHelper getDB() {
        return m_db;
    }

    ArrayList<Currency> allCurrencies;

    public ArrayList<Currency> getAllCurrencies() {
        return this.allCurrencies;
    }

    public void setAllCurrencies(ArrayList<Currency> allCurrencies) {
        this.allCurrencies = allCurrencies;
    }

    public int CreateCurrency(Currency currency)
    {
        int nCurrencyID = (int)getDB().createCurrency(currency);
        currency.setID(nCurrencyID);

       // getAllCurrencies().add(currency);

        return nCurrencyID;
    }

    public void UpdateAllCurrencies()
    {
        allCurrencies = getDB().getAllCurrencies();
    }

    private CurrencyManager(Context context)
    {
        m_db = new DatabaseHelper(context);

        allCurrencies = m_db.getAllCurrencies();
    }

    public static CurrencyManager newInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance = new CurrencyManager(context);
        }

        return m_instance;
    }

    @Override
    public void CloseManager() {

    }

    public int UpdateCurrency(String strCurrCode, double dNewValue)
    {
        try
        {
            return getDB().UpdateCurrencyValueByCode(strCurrCode, dNewValue);
        }
        catch (Exception e)
        {
            // TODO: write to log

            return Consts.CURRENCY_UPDATE_ERROR;
        }
    }

    public Currency getCurrencyByCode(String Code)
    {
        for (Currency curr: getAllCurrencies())
        {
            if (curr.getCurrencyCode().equals(Code))
            {
                return curr;
            }
        }

        return null;
    }

    public static CurrencyManager getInstance()
    {
        if (m_instance == null)
        {
            Log.println(Log.ERROR, "Currency Manager", "Must Create Currency Manager before using");
        }

        return m_instance;
    }

    public static Currency getDefault()
    {
        return (CurrencyManager.getInstance().getCurrencyByCode("USD"));
    }

    public static double ConvertFromToCurrency(Currency from, Currency to, double dValue)
    {
        return (dValue / from.getValue() * to.getValue());
    }
}
