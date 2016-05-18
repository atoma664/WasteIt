package info.androidhive.sqlite.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.model.Currency;

/**
 * Created by LENOVO on 18/05/2016.
 */
public class SettingsManager{

    private Context myContext;
    private static SettingsManager m_settingManager;
    private Currency m_currencyDefault;
    SharedPreferences.Editor m_editor;

    public Currency getDefaultCurrency()
    {
        return m_currencyDefault;
    }

    public void setDefaultCurrency(Currency m_currencyDefault) {
        m_currencyDefault = m_currencyDefault;

        m_editor.putInt(Consts.DEFAULT_CURRENCY, m_currencyDefault.getID());
    }

    private SettingsManager(Context context)
    {
        myContext = context;
        m_editor = myContext.getSharedPreferences(Consts.SETTING_PREFERANCES, Context.MODE_PRIVATE).edit();

        InitValues();
    }

    private void InitValues()
    {
        if (myContext.getSharedPreferences(Consts.SETTING_PREFERANCES, Context.MODE_PRIVATE).getBoolean(Consts.IS_SETTING_INIT, false))
        {
            InitDefault();
        }
        else
        {
            int nCurrencyID = myContext.getSharedPreferences(Consts.SETTING_PREFERANCES, Context.MODE_PRIVATE).getInt(Consts.DEFAULT_CURRENCY,
                    CurrencyManager.getInstance().getCurrencyByCode(Consts.DEFAULT_CURRENCY_CODE).getID());

            m_currencyDefault = CurrencyManager.getInstance().getCurrencyByCode(Consts.DEFAULT_CURRENCY_CODE);
        }
    }

    private void InitDefault()
    {
        m_editor.putBoolean(Consts.IS_SETTING_INIT, true);

        // Init default values
        m_editor.putString(Consts.DEFAULT_LANGUAGE, Consts.DEFAULT_STRING);

        // Setting the default Currency
        setDefaultCurrency(CurrencyManager.getInstance().getCurrencyByCode(Consts.DEFAULT_CURRENCY_CODE));
    }

    public static SettingsManager newInstance(Context context)
    {
        if (m_settingManager == null)
        {
            m_settingManager = new SettingsManager(context);
        }

        return (m_settingManager);
    }

    public static SettingsManager getInstance()
    {
        if (m_settingManager == null)
        {
            // TODO: Throw exeption
        }

        return (m_settingManager);
    }

}
