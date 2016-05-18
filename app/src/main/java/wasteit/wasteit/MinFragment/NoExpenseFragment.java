package wasteit.wasteit.MinFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wasteit.wasteit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoExpenseFragment extends Fragment {


    public NoExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_expense, container, false);
    }

}
