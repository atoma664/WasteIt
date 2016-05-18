package info.androidhive.sqlite.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.model.Category;
import info.androidhive.sqlite.model.Event;

/**
 * Created by LENOVO on 05/05/2016.
 */
public class EventsManager implements ManagerInterface {


    //region DM
    DatabaseHelper m_dbHelper;
    ArrayList<Event> m_all_events;
    static EventsManager m_instance;
    //endregion

    //region Getter Setter
    public DatabaseHelper getDB() {
        return m_dbHelper;
    }

    public ArrayList<Event> getAllEvents()
    {
        return m_all_events;
    }

    public void setAllEvents(ArrayList<Event> m_allevents)
    {
        this.m_all_events = m_allevents;
    }
    //endregion

    /**
     * private ctor
      * @param context
     */
    private EventsManager(Context context)
    {
        m_dbHelper = new DatabaseHelper(context);
        m_all_events = m_dbHelper.getAllEvents();
    }

    /**
     * Create or return new instance of this object
     * @param context
     * @return
     */
    public static EventsManager newInstance(Context context)
    {
        // Init if null
        if (m_instance == null)
        {
            m_instance = new EventsManager(context);
        }

        return m_instance;
    }

    public Event getEvent(int nID)
    {
        return (getDB().getEvent(nID));
    }

    public int DeleteEvent(int nEventID)
    {
        int nDeleted = getDB().deleteEvent(nEventID);

        m_all_events = getDB().getAllEvents();

        return nDeleted;
    }

    /**
     *
     * @param event to create
     * @return the event ID
     */
    public int CreateNewEvent(Event event)
    {
        int nEvent = (int)getDB().createEvent(event);

        getAllEvents().add(event);

        return nEvent;
    }

    /**
     * Return intance, must call createinstance before use
      * @return instance or null if newInstance was not called
     */
    public static EventsManager getInstance()
    {
        if (m_instance == null)
        {
            Log.println(Log.ERROR, "Event Manager", "Must Create new instant");
        }

        return m_instance;
    }

    public int UpdateEvent(Event event)
    {
        int nRowNum = getDB().updateEvent(event);

        m_all_events = getDB().getAllEvents();

        return nRowNum;
    }

    @Override
    public void CloseManager() {
        getDB().closeDB();
    }
}
