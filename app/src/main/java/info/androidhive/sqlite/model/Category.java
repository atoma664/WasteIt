package info.androidhive.sqlite.model;

import java.io.Serializable;

/**
 * Created by LENOVO on 04/05/2016.
 */
public class Category implements Serializable {

    private int m_nID;
    private String m_strCategoryName;

    public Category() {
    }

    public Category(String strName) {

        setCategoryName(strName);
    }

    public void setID(int m_nID) {
        this.m_nID = m_nID;
    }

    public int getID() {
        return m_nID;
    }

    public String getCategoryName() {
        return m_strCategoryName;
    }

    public void setCategoryName(String strCategoryName) {
        this.m_strCategoryName = strCategoryName;
    }

    @Override
    public String toString() {
        return this.m_strCategoryName;
    }

}
