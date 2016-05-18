package info.androidhive.sqlite.model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by LENOVO on 04/05/2016.
 */
public class Event implements Serializable {


    private int m_nId;
    private String m_strName;
    private double m_dMoneyAmount;
    private Date m_dStartDate;
    private Date m_dEndDate;
    private int m_nDaysNum;
    private int m_nNumOfPeople;
    private info.androidhive.sqlite.model.Currency m_cCurrency;

    public info.androidhive.sqlite.model.Currency getCurrency() {
        return m_cCurrency;
    }

    public void setCurrency(info.androidhive.sqlite.model.Currency m_cCurrency) {
        this.m_cCurrency = m_cCurrency;
    }

    public Event()  {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param m_nId ID.
     * @param m_strName Name.
     * @param m_dMoneyAmount Money.
     * @param m_dStartDate Start Date.
     * @param m_dEndDate End Date.
     * @param m_nNumOfPeople Number Of People.
     * @return A new instance of Event
     */
    public Event(int m_nId, String m_strName, double m_dMoneyAmount, Date m_dStartDate, Date m_dEndDate, int m_nDaysNum, int m_nNumOfPeople, info.androidhive.sqlite.model.Currency cCurrncy) {
        this.m_nId = m_nId;
        this.m_strName = m_strName;
        this.m_dMoneyAmount = m_dMoneyAmount;
        this.m_dStartDate = m_dStartDate;
        this.m_dEndDate = m_dEndDate;
        this.m_nDaysNum = m_nDaysNum;
        this.m_nNumOfPeople = m_nNumOfPeople;
        this.m_cCurrency = cCurrncy;
    }

    public void setId(int m_nId) {
        this.m_nId = m_nId;
    }

    public int getID() {
        return m_nId;
    }

    public String getName() {
        return m_strName;
    }

    public double getMoneyAmount() {
        return m_dMoneyAmount;
    }

    public Date getStartDate() {
        return m_dStartDate;
    }

    public Date getEndDate() {
        return m_dEndDate;
    }

    public int getDaysNum() {
        return m_nDaysNum;
    }

    public int getNumOfPeople() {
        return m_nNumOfPeople;
    }

    public void setName(String m_strName) {
        this.m_strName = m_strName;
    }

    public void setMoneyAmount(double m_dMoneyAmount) {
        this.m_dMoneyAmount = m_dMoneyAmount;
    }

    public void setStartDate(Date m_dStartDate) {
        this.m_dStartDate = m_dStartDate;
    }

    public void setEndDate(Date m_dEndDate) {
        this.m_dEndDate = m_dEndDate;
    }

    public void setDaysNum(int m_nDaysNum) {
        this.m_nDaysNum = m_nDaysNum;
    }

    public void setNumOfPeople(int m_nNumOfPeople) {
        this.m_nNumOfPeople = m_nNumOfPeople;
    }

    public String toString()
    {
        return getName();// +" (" + Services.FromDateToString(getStartDate()) + " - " + Services.FromDateToString(getEndDate()) + ")";
    }
}
