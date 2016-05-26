package wasteit.wasteit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.Consts;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.manager.SettingsManager;
import info.androidhive.sqlite.model.Currency;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    Spinner m_currencySpinner = null;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        view.findViewById(R.id.setting_save).setOnClickListener(this);
        view.findViewById(R.id.setting_cancel).setOnClickListener(this);

        m_currencySpinner = (Spinner)view.findViewById(R.id.setting_default_currency);

        ArrayList<Currency> alCurrencies = CurrencyManager.getInstance().getAllCurrencies();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Currency> adapter =
                new ArrayAdapter<info.androidhive.sqlite.model.Currency>(this.getContext(), android.R.layout.simple_spinner_item, alCurrencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_currencySpinner.setAdapter(adapter);

        int nCurrencyID = SettingsManager.getInstance().getDefaultCurrency().getID();
        int nCurrencyIndex = 0;

        // Set the saved currency to be the selected one
        if (nCurrencyID != Consts.CURRENCY_UPDATE_ERROR)
        {
            for (Currency currency : alCurrencies)
            {
                if (currency.getID() == nCurrencyID)
                {
                    break;
                }

                nCurrencyIndex++;
            }

            m_currencySpinner.setSelection(nCurrencyIndex);
        }

        ArrayAdapter<CharSequence> arrLanguages =
                ArrayAdapter.createFromResource(getContext(), R.array.string_languages, android.R.layout.simple_spinner_item );

        Spinner spLanguages = (Spinner)view.findViewById(R.id.setting_languages);
        spLanguages.setAdapter(arrLanguages);

       // String strLanguage = getActivity().getPreferences(Context.MODE_PRIVATE).getString(Consts.DEFAULT_LANGUAGE, Consts.DEFAULT_LANGUAGE_STRING);

        /*int nCurrencyIndex = 0;

        // Set the saved currency to be the selected one
        if (strLanguage != Consts.DEFAULT_LANGUAGE_STRING)
        {
            for (String strLanguage : arrLanguages.get)
            {
                if (currency.getID() == nCurrencyID)
                {
                    break;
                }

                nCurrencyIndex++;
            }

            currencySpinner.setSelection(nCurrencyIndex);
        }*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        //    throw new RuntimeException(context.toString()
         //           + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case (R.id.setting_cancel):
            {
                getFragmentManager().popBackStack();

                break;
            }
            case (R.id.setting_save):
            {
                SettingsManager.getInstance().setDefaultCurrency((Currency) m_currencySpinner.getSelectedItem());

                Toast t = Toast.makeText(getContext(), getString(R.string.toast_setting_save), Toast.LENGTH_SHORT);
                t.show();
                getFragmentManager().popBackStack();

                break;
            }
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
