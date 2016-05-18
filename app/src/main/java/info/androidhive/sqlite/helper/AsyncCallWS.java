package info.androidhive.sqlite.helper;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.model.Currency;

/**
 * Created by LENOVO on 12/05/2016.
 */
public class AsyncCallWS extends AsyncTask<Void, Void, Void> {

    LoadCurrencyListener mListener;
    Context context;

    public AsyncCallWS(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //Log.i(TAG, "onPreExecute");
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (context instanceof LoadCurrencyListener) {
            mListener = (LoadCurrencyListener) context;
        }

        ArrayList<Currency> cAll = CurrencyManager.getInstance().getAllCurrencies();
        InitCurrency(cAll.isEmpty());

        return null;
    }

    private void InitCurrency(boolean bIsInit)
    {
        // To init all currency
       /* for (java.util.Currency c: java.util.Currency.getAvailableCurrencies())
        {
            Currency cNew = new Currency(-1, 1, c.getCurrencyCode());
            GetDB().createCurrency(cNew);
        }*/

        if (bIsInit)
        {
            Currency cNew = new Currency(-1, 1, Consts.BASE_CURRENCY);
            CurrencyManager.getInstance().CreateCurrency(cNew);
        }

        int nProgress = 0;

        for (String strCurr: Consts.SUPPORTED_CURRENCIES)
        {
            double dResult = Services.findExchangeRateAndConvert(Consts.BASE_CURRENCY, strCurr, 1);

            if (dResult != Consts.CURRENCY_UPDATE_ERROR)
            {
                if (bIsInit)
                {
                    CurrencyManager.getInstance().CreateCurrency(new Currency(-1, dResult, strCurr));
                }
                else
                {
                    CurrencyManager.getInstance().UpdateCurrency(strCurr, dResult);
                }

                nProgress++;
                int nPercent = nProgress * 100 / Consts.SUPPORTED_CURRENCIES.length;
                mListener.SetProgress(nPercent);
            }
            else
            {
                break;
            }
        }

        CurrencyManager.getInstance().UpdateAllCurrencies();

        mListener.FinishLoading();
    }

    @Override
    protected void onPostExecute(Void result) {
     //   Log.i(TAG, "onPostExecute");
     //   Toast.makeText(MainActivity.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
    }

    public interface LoadCurrencyListener
    {
        void SetProgress(int nProgress);
        void FinishLoading();
    }
}
