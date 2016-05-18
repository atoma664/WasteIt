package info.androidhive.sqlite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by LENOVO on 04/05/2016.
 */
public class Expense implements Serializable {

    private int m_nID;
    private String m_strName;
    private double m_dCost;
    private Date m_dDate;
    private boolean m_bToInclude;
    private ArrayList<String> m_arrAdditionalFileLocation;
    private Event m_eEvent;
    private Category m_cCategory;

    public Category getCategory() {
        return m_cCategory;
    }

    public void setCategory(Category m_cCategory) {
        this.m_cCategory = m_cCategory;
    }

    public Event getEvent() {
        return m_eEvent;
    }

    public void setEvent(Event m_eEvent) {
        this.m_eEvent = m_eEvent;
    }

    public Expense(String m_strName, Category m_nCategory, double m_dCost, Date m_dDate, Event m_nEvent, boolean m_bToInclude, ArrayList<String> m_arrAdditionalFileLocation) {
        this.m_strName = m_strName;
        this.m_cCategory = m_nCategory;
        this.m_dCost = m_dCost;
        this.m_dDate = m_dDate;
        this.m_eEvent = m_nEvent;
        this.m_bToInclude = m_bToInclude;

        if (m_arrAdditionalFileLocation != null) {
            this.m_arrAdditionalFileLocation = m_arrAdditionalFileLocation;
        }
        else
        {
            this.m_arrAdditionalFileLocation = new ArrayList<String>();
        }
    }

    public void setName(String m_strName) {
        this.m_strName = m_strName;
    }

    public void setDate(Date m_dDate) {
        this.m_dDate = m_dDate;
    }

    public void setToInclude(boolean m_bToInclude) {
        this.m_bToInclude = m_bToInclude;
    }

    public Expense() {
        m_arrAdditionalFileLocation = new ArrayList<>();
    }

    public void setID(int m_nID) {
        this.m_nID = m_nID;
    }

    public double getCost() {
        return m_dCost;
    }

    public void setCost(double dCost) {
        this.m_dCost = dCost;
    }

    public int getID() {
        return m_nID;
    }

    public String getName() {
        return m_strName;
    }

    public Date getDate() {
        return m_dDate;
    }

    public boolean IsToInclude() {
        return m_bToInclude;
    }

    public ArrayList<String> getAdditionalFileLocation() {
        return m_arrAdditionalFileLocation;
    }
}
