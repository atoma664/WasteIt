package wasteit.wasteit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.model.Event;
import wasteit.wasteit.MinFragment.EventFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagerFragment extends Fragment
{
    private ArrayList<Event> m_allevents = null;

    public EventManagerFragment()
    {
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        InitView(getView());
    }

    public ArrayList<Event> getAllEvents()
    {
        return m_allevents;
    }

    /**
     * @return A new instance of fragment EventManagerFragment.
     */
    public static EventManagerFragment newInstance() {
        EventManagerFragment fragment = new EventManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (getArguments() != null) {
            }
    }

    private void InitView (View view)
    {
        // Get all the events
        m_allevents = EventsManager.getInstance().getAllEvents();

        // Add the event to the screen
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Check if the screen is empty
        if (fm.getFragments() == null)
        {
            for (int i = 0; i < getAllEvents().size(); i++)
            {
                Event e = getAllEvents().get(i);

                if (i == 0)
                {
                    Fragment fEvent = EventFragment.newInstance(e);
                    transaction.replace(R.id.event_manager_container, fEvent);
                }
                else
                {
                    Fragment fEvent = EventFragment.newInstance(e);
                    transaction.add(R.id.event_manager_container, fEvent);
                }
            }
        }

        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_event_manager, container, false);

        InitView(view);

        return view;
    }
}
