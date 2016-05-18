package info.androidhive.sqlite.model;

import java.io.Serializable;

/**
 * Created by LENOVO on 12/05/2016.
 */
public class Currency implements Serializable
{
    private int m_nID;
    private double m_dValue;
    private String m_strCurrencyCode;

    public Currency(int m_nID, double m_dValue, String m_strCurrencyCode) {
        this.m_nID = m_nID;
        this.m_dValue = m_dValue;
        this.m_strCurrencyCode = m_strCurrencyCode;
    }

    @Override
    public String toString() {
        return java.util.Currency.getInstance(m_strCurrencyCode).getSymbol();
    }

    public Currency() {
    }

    public int getID() {
        return m_nID;
    }

    public void setID(int m_nID) {
        this.m_nID = m_nID;
    }

    public double getValue() {
        return m_dValue;
    }

    public void setValue(double m_dValue) {
        this.m_dValue = m_dValue;
    }

    public String getCurrencyCode() {
        return m_strCurrencyCode;
    }

    public void setCurrencyCode(String m_strCurrencyCode) {
        this.m_strCurrencyCode = m_strCurrencyCode;
    }
}
