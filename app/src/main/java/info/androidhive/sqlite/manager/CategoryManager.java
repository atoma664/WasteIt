package info.androidhive.sqlite.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.model.Category;

/**
 * Created by LENOVO on 13/05/2016.
 */
public class CategoryManager implements ManagerInterface
{
    ArrayList<Category> m_all_categories;

    public DatabaseHelper getDB() {
        return m_db;
    }

    DatabaseHelper m_db;
    static CategoryManager m_instance;

    public ArrayList<Category> getAllCategories()
    {
        return m_all_categories;
    }

    public void setAllCategories(ArrayList<Category> m_all_categories) {
        this.m_all_categories = m_all_categories;
    }

    private CategoryManager(Context context)
    {
        m_db = new DatabaseHelper(context);
        m_all_categories = m_db.getAllCategories();

        if (m_all_categories.isEmpty())
        {
            InitCategories();
        }
    }

    public static CategoryManager newInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance = new CategoryManager(context);
        }

        return m_instance;
    }

    @Override
    public void CloseManager() {

    }

    // Init the categories at first time use
    public void InitCategories()
    {
        getDB().createCategory(new Category("Clothes"));
        getDB().createCategory(new Category("Food"));
        getDB().createCategory(new Category("Bills"));
        getDB().createCategory(new Category("Attractions"));
        getDB().createCategory(new Category("Trips"));
        getDB().createCategory(new Category("Gifts"));
        getDB().createCategory(new Category("House"));
        getDB().createCategory(new Category("Gadgets"));
        getDB().createCategory(new Category("Other"));

        m_all_categories = getDB().getAllCategories();
    }

    public static CategoryManager getInstance()
    {
        if (m_instance == null)
        {
            Log.println(Log.ERROR, "Category Manager", "Must Create new instant");
        }

        return m_instance;
    }
}
