package info.androidhive.sqlite.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import info.androidhive.sqlite.model.Category;
import info.androidhive.sqlite.model.Currency;
import info.androidhive.sqlite.model.Event;
import info.androidhive.sqlite.model.Expense;

public class DatabaseHelper extends SQLiteOpenHelper {

    //region CONST
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    private static final String SEPERATOR = ";";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WasteItManager";

    // Alias
    static final String KEY_EXPENSE_ID_ALIAS = "KEY_EXPENSE_ID_ALIAS";
    static final String KEY_EXPENSE_NAME_ALIAS = "KEY_EXPENSE_NAME_ALIAS";
    static final String KEY_EXPENSE_CREATED_BY_ALIAS = "KEY_EXPENSE_CREATED_BY_ALIAS";
    static final String KEY_CATEGORY_ID_ALIAS = "KEY_CATEGORY_ID_ALIAS";
    static final String KEY_CATEGORY_NAME_ALIAS = "KEY_CATEGORY_NAME_ALIAS";
    static final String KEY_CATEGORY_CREATED_BY_ALIAS = "KEY_CATEGORY_CREATED_BY_ALIAS";
    static final String KEY_EVENT_ID_ALIAS = "KEY_EVENT_ID_ALIAS";
    static final String KEY_EVENT_NAME_ALIAS = "KEY_EVENT_NAME_ALIAS";
    static final String KEY_EVENT_CREATED_BY_ALIAS = "KEY_EVENT_CREATED_BY_ALIAS";
    static final String KEY_CURRENY_CREATED_BY_ALIAS = "KEY_CURRENCY_CREATED_BY_ALIAS";
    static final String KEY_CURRENY_ID_ALIAS = "KEY_CURRENCY_ID_ALIAS";

    // Qualifire
    static final String EXPENSE_QUALIFIRE = "expense";
    static final String CATEGORY_QUALIFIRE = "cat";
    static final String EVENT_QUALIFIRE = "event";
    static final String CURRENCY_QUALIFIRE = "currency";

    // Table Names
    private static final String TABLE_EVENT = "EVENTS";
    private static final String TABLE_CATEGORY = "CATEGORIES";
    private static final String TABLE_EXPENSE = "EXPENSES";
    private static final String TABLE_CURRENCY = "CURRENCIES";

    // Common column names
    private static final String KEY_ID = "ID";
    private static final String KEY_CREATED_AT = "CREATED_AT";

    // EXPENSE Table - column names
    private static final String KEY_EXPENSE_NAME = "NAME";
    private static final String KEY_EXPENSE_COST = "COST";
    private static final String KEY_EXPENSE_EVENT_ID = "EVENT_ID";
    private static final String KEY_EXPENSE_CATEGORY_ID = "CATEGORY_ID";
    private static final String KEY_EXPENSE_DATE = "DATE";
    private static final String KEY_EXPENSE_IS_INCLUDE = "IS_INCLUDE";
    private static final String KEY_EXPENSE_ADDITIONAL_FILES_LOCATION = "ADDITIONAL_FILES_LOCATION";

    // EXPENSE Table - column nmaes
    private static final String KEY_EVENT_NAME = "NAME";
    private static final String KEY_EVENT_MONEY_AMOUNT = "MONEY_AMOUNT";
    private static final String KEY_EVENT_START_DATE = "START_DATE";
    private static final String KEY_EVENT_END_DATE = "END_DATE";
    private static final String KEY_EVENT_PEOPLE_NUMBER = "PEOPLE_NUMBER";
    private static final String KEY_EVENT_DAYS_NUMBER = "DAYS_NUMBER";
    private static final String KEY_EVENT_CURRENCY_ID = "CURRENCY_ID";

    // CATEGORY Table - column names
    private static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";

    // EXPENSE_EVENT_CATEGORY Table - column names
    private static final String KEY_CURRENCY_CODE = "CURRENCY_ID";
    private static final String KEY_CURRENCY_VALUE = "CURRENCY_VALUE";

    //endregion

    //region Column Strings
    public static final String ExpensesColumnString =  EXPENSE_QUALIFIRE + "." + KEY_ID + " AS " + KEY_EXPENSE_ID_ALIAS + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_NAME  + " AS " + KEY_EXPENSE_NAME_ALIAS + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_CREATED_AT + " AS " + KEY_EXPENSE_CREATED_BY_ALIAS + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_DATE + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_COST + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_EVENT_ID + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_IS_INCLUDE + ", " +
            EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_CATEGORY_ID;

    public static final String EventColumnString = EVENT_QUALIFIRE + "." + KEY_ID + " AS " + KEY_EVENT_ID_ALIAS + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_NAME  + " AS " + KEY_EVENT_NAME_ALIAS + ", " +
            EVENT_QUALIFIRE + "." + KEY_CREATED_AT + " AS " + KEY_EVENT_CREATED_BY_ALIAS + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_START_DATE + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_MONEY_AMOUNT + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_END_DATE + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_PEOPLE_NUMBER + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_CURRENCY_ID + ", " +
            EVENT_QUALIFIRE + "." + KEY_EVENT_DAYS_NUMBER;

    public static String CategoriesColumnString = CATEGORY_QUALIFIRE + "." + KEY_ID + " AS " + KEY_CATEGORY_ID_ALIAS + ", " +
            CATEGORY_QUALIFIRE + "." + KEY_CATEGORY_NAME  + " AS " + KEY_CATEGORY_NAME_ALIAS + ", " +
            CATEGORY_QUALIFIRE + "." + KEY_CREATED_AT + " AS " + KEY_CATEGORY_CREATED_BY_ALIAS;

    public static String CurrencyColumnString = CURRENCY_QUALIFIRE + "." + KEY_ID + " AS " + KEY_CURRENY_ID_ALIAS + ", " +
            CURRENCY_QUALIFIRE + "." + KEY_CURRENCY_CODE  + ", " +
            CURRENCY_QUALIFIRE + "." + KEY_CURRENCY_VALUE + ", " +
            CURRENCY_QUALIFIRE + "." + KEY_CREATED_AT + " AS " + KEY_CURRENY_CREATED_BY_ALIAS;
    //endregion

    //region Create Table Statement
    // Table Create Statements
    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE "
            + TABLE_EXPENSE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EXPENSE_NAME + " TEXT NOT NULL,"
            + KEY_EXPENSE_COST + " REAL NOT NULL,"
            + KEY_EXPENSE_EVENT_ID + " INTEGER NOT NULL,"
            + KEY_EXPENSE_CATEGORY_ID + " INTEGER NOT NULL,"
            + KEY_EXPENSE_DATE + " DATETIME NOT NULL,"
            + KEY_EXPENSE_IS_INCLUDE + " INTEGER,"
            + KEY_EXPENSE_ADDITIONAL_FILES_LOCATION + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_EVENT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EVENT_NAME + " TEXT NOT NULL,"
            + KEY_EVENT_MONEY_AMOUNT + " REAL NOT NULL,"
            + KEY_EVENT_START_DATE + " DATETIME NOT NULL,"
            + KEY_EVENT_END_DATE + " DATETIME NOT NULL,"
            + KEY_EVENT_DAYS_NUMBER + " INTEGER NOT NULL,"
            + KEY_EVENT_PEOPLE_NUMBER + " INTEGER NOT NULL,"
            + KEY_EVENT_CURRENCY_ID + " INTEGER NOT NULL,"
            + KEY_CREATED_AT + " DATETIME" + ")";


    // Tag table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CATEGORY_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE "
            + TABLE_CURRENCY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CURRENCY_CODE + " TEXT NOT NULL,"
            + KEY_CURRENCY_VALUE + " REAL NOT NULL,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    //endregion

    //region Ctor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //endregion

    //region DDL
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_CURRENCY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY);

        // create new tables
        onCreate(db);
    }
    //endregion

    //region Create
    /*
    * Creating a category
    */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getCategoryName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert rowgetDateTime
        long category_id = db.insert(TABLE_CATEGORY, null, values);

        return category_id;
    }

    public long createCurrency(Currency currency) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CURRENCY_VALUE, currency.getValue());
        values.put(KEY_CURRENCY_CODE, currency.getCurrencyCode());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert rowgetDateTime
        long currency_id = db.insert(TABLE_CURRENCY, null, values);

        return currency_id;
    }

    /*
    * Creating an event
    */
    public long createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getName());
        values.put(KEY_EVENT_MONEY_AMOUNT, event.getMoneyAmount());
        values.put(KEY_EVENT_START_DATE,  getDateOnlyDateTime(event.getStartDate()));
        values.put(KEY_EVENT_END_DATE,  getDateOnlyDateTime(event.getEndDate()));
        values.put(KEY_EVENT_DAYS_NUMBER,  event.getDaysNum());
        values.put(KEY_EVENT_PEOPLE_NUMBER,  event.getNumOfPeople());
        values.put(KEY_EVENT_CURRENCY_ID,  event.getCurrency().getID());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert rowgetDateTime
        long event_id = db.insert(TABLE_EVENT, null, values);

        return event_id;
    }

    /*
   * Creating a todo
   */
    public long createExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_NAME, expense.getName());
        values.put(KEY_EXPENSE_COST, expense.getCost());
        values.put(KEY_EXPENSE_CATEGORY_ID, expense.getCategory().getID());
        values.put(KEY_EXPENSE_EVENT_ID, expense.getEvent().getID());
        values.put(KEY_EXPENSE_DATE, getDateOnlyDateTime(expense.getDate()));
        values.put(KEY_EXPENSE_IS_INCLUDE, expense.IsToInclude());

        String strFilePath = "";

        for (String CurrLocation: expense.getAdditionalFileLocation())
        {
            strFilePath += CurrLocation;

            strFilePath += SEPERATOR;
        }

        values.put(KEY_EXPENSE_ADDITIONAL_FILES_LOCATION, strFilePath);
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long expense_id = db.insert(TABLE_EXPENSE, null, values);

        return expense_id;
    }
    //endregion

    //region Get By ID
    /*
 * get single todo
 */
    public Event getEvent(int event_id)
    {
        String selectQuery = "SELECT " + EventColumnString + ", " + CurrencyColumnString
                + " FROM " + TABLE_EVENT + " " + EVENT_QUALIFIRE + ", "  + TABLE_CURRENCY + " " + CURRENCY_QUALIFIRE
                + " WHERE " + EVENT_QUALIFIRE + "." + KEY_ID + " = " + event_id +
                " AND " + EVENT_QUALIFIRE + "." + KEY_EVENT_CURRENCY_ID + " = " + CURRENCY_QUALIFIRE + "." + KEY_ID;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null)
                c.moveToFirst();

            if (c.getCount() != 0)
            {
                return ReadEventToObject(c);
            }
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
        }

        return null;
    }

    public Category getCategory(int category_id)
    {
        String selectQuery = "SELECT  " + CategoriesColumnString + " FROM " + TABLE_CATEGORY + " " + CATEGORY_QUALIFIRE + " WHERE "
                + KEY_ID + " = " + category_id;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null)
                c.moveToFirst();

            Category category = ReadCategoryToObject(c);

            return category;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }

    /*
* get single todo
*/
    public Expense getExpense(int expense_id)
    {
        String selectQuery = "SELECT  " + ExpensesColumnString + ", " +
                CategoriesColumnString + ", " +
                EventColumnString +
                " FROM " + TABLE_EXPENSE  + " " + EXPENSE_QUALIFIRE + ", " +
                TABLE_EVENT  + " " + EVENT_QUALIFIRE + ", " +
                TABLE_CATEGORY + " " + CATEGORY_QUALIFIRE +
                " WHERE expense." + KEY_EXPENSE_EVENT_ID + " = event." + KEY_ID +
                " AND expense." + KEY_EXPENSE_CATEGORY_ID + " = cat." + KEY_ID +
                " AND expense." + KEY_ID + " = " + expense_id;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null)
                c.moveToFirst();

            Expense expense = ReadExpenseToObject(c);

            String[] str = (c.getString(c.getColumnIndex(KEY_EXPENSE_ADDITIONAL_FILES_LOCATION))).split(SEPERATOR);

            for (String strCurr : str) {
                expense.getAdditionalFileLocation().add(strCurr);
            }

            return expense;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }
    //endregion

    //region Get All
    /**
     * getting all todos
     * */
    public ArrayList<Event> getAllEvents()
    {
        ArrayList<Event> allevents = new ArrayList<Event>();
        String selectQuery = "SELECT " + EventColumnString + ", " + CurrencyColumnString
                + " FROM " + TABLE_EVENT + " " + EVENT_QUALIFIRE + ", "  + TABLE_CURRENCY + " " + CURRENCY_QUALIFIRE
                + " WHERE " + EVENT_QUALIFIRE + "." + KEY_EVENT_CURRENCY_ID + " = " + CURRENCY_QUALIFIRE + "." + KEY_ID;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Event event = ReadEventToObject(c);

                    // adding to todo list
                    allevents.add(event);
                } while (c.moveToNext());
            }

            return allevents;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return (null);
        }
    }

    public ArrayList<Currency> getAllCurrencies()
    {
        ArrayList<Currency> allcurr = new ArrayList<Currency>();
        String selectQuery = "SELECT " + CurrencyColumnString + " FROM " + TABLE_CURRENCY + " " + CURRENCY_QUALIFIRE;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Currency curr = ReadCurrencyToObject(c);

                    // adding to todo list
                    allcurr.add(curr);
                } while (c.moveToNext());
            }

            return allcurr;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }

    /**
     * getting all todos
     * */
    public ArrayList<Expense> getAllExpenses()
    {
        ArrayList<Expense> allexepses = new ArrayList<Expense>();
        String selectQuery = "SELECT  " + ExpensesColumnString + ", "
                + EventColumnString + ", "
                + CategoriesColumnString +
                " FROM " + TABLE_EXPENSE  + " " + EXPENSE_QUALIFIRE + ", " +
                TABLE_EVENT  + " " + EVENT_QUALIFIRE + ", " +
                TABLE_CATEGORY + " " + CATEGORY_QUALIFIRE +
                " WHERE expense." + KEY_EXPENSE_EVENT_ID + " = event." + KEY_ID +
                " AND expense." + KEY_EXPENSE_CATEGORY_ID + " = cat." + KEY_ID;

        //  Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Expense expense = ReadExpenseToObject(c);

                allexepses.add(expense);
            } while (c.moveToNext());
        }

        return allexepses;
    }

    /**
     * getting all todos
     * */
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> allCategories = new ArrayList<Category>();
        String selectQuery = "SELECT  " + CategoriesColumnString +
                " FROM " + TABLE_CATEGORY + " " + CATEGORY_QUALIFIRE;

        //  Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Category cat = ReadCategoryToObject(c);

                allCategories.add(cat);
            } while (c.moveToNext());
        }

        return allCategories;
    }
    //endregion

    //region Read To Object
    private Category ReadCategoryToObject(Cursor c)
    {
        Category cat = new Category();

        cat.setID(c.getInt((c.getColumnIndex(KEY_CATEGORY_ID_ALIAS))));
        cat.setCategoryName((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME_ALIAS))));

        return cat;
    }

    private Event ReadEventToObject(Cursor c)
    {
        Event event = new Event();

        event.setId(c.getInt(c.getColumnIndex(KEY_EVENT_ID_ALIAS)));
        event.setName(c.getString(c.getColumnIndex(KEY_EVENT_NAME_ALIAS)));
        event.setMoneyAmount(c.getFloat(c.getColumnIndex(KEY_EVENT_MONEY_AMOUNT)));
        event.setStartDate(getDateFromDateTime(c.getString(c.getColumnIndex(KEY_EVENT_START_DATE))));
        event.setEndDate(getDateFromDateTime(c.getString(c.getColumnIndex(KEY_EVENT_END_DATE))));
        event.setDaysNum(c.getInt(c.getColumnIndex(KEY_EVENT_DAYS_NUMBER)));
        event.setNumOfPeople(c.getInt(c.getColumnIndex(KEY_EVENT_PEOPLE_NUMBER)));
        event.setCurrency(ReadCurrencyToObject(c));

        return event;
    }

    private Currency ReadCurrencyToObject(Cursor c)
    {
        Currency curr = new Currency();

        curr.setID(c.getInt(c.getColumnIndex(KEY_CURRENY_ID_ALIAS)));
        curr.setValue(c.getDouble(c.getColumnIndex(KEY_CURRENCY_VALUE)));
        curr.setCurrencyCode(c.getString(c.getColumnIndex(KEY_CURRENCY_CODE)));

        return curr;
    }

    private Expense ReadExpenseToObject(Cursor c)
    {
        Expense expense = new Expense();

        expense.setID(c.getInt(c.getColumnIndex(KEY_EXPENSE_ID_ALIAS)));
        expense.setName(c.getString(c.getColumnIndex(KEY_EXPENSE_NAME_ALIAS)));
        expense.setCost(c.getFloat(c.getColumnIndex(KEY_EXPENSE_COST)));
        expense.setDate(getDateFromDateTime(c.getString(c.getColumnIndex(KEY_EXPENSE_DATE))));
        expense.setToInclude(c.getInt(c.getColumnIndex(KEY_EXPENSE_IS_INCLUDE)) == 1);

        expense.setEvent(ReadEventToObject(c));
        expense.setCategory(ReadCategoryToObject(c));

        return expense;
    }

    private Expense ReadGeneralExpenseToObject(Cursor c)
    {
        Expense expense = new Expense();

        expense.setID(Expense.GENERAL_EXPENSE_ID);
        expense.setName(Expense.GENERAL_EXPENSE_NAME);
        expense.setCost(c.getFloat(c.getColumnIndex("SUM("+ KEY_EXPENSE_COST + ")")));
        expense.setDate(getDateFromDateTime(c.getString(c.getColumnIndex(KEY_EXPENSE_DATE))));
        expense.setToInclude(false);

        expense.setEvent(getEvent(c.getInt(c.getColumnIndex(KEY_EXPENSE_EVENT_ID))));
        expense.setCategory(null);

        return expense;
    }
    //endregion

    //region Tools
    /**
     * get datetime
     * */
    private String getDateTime(Date dDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String str =  dateFormat.format(dDate);
        return str;
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * get datetime
     * */
    private String getTodayDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd 00:00:00", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * get datetime
     * */
    private String getDateOnlyDateTime(Date dDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd 00:00:00", Locale.getDefault());

        String str =  dateFormat.format(dDate);
        return str;
    }

    /**
     * get datetime
     * */
    private Date getDateFromDateTime(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try
        {
            return dateFormat.parse(strDate);
        }
        catch (Exception e)
        {
            return (new Date());
        }
    }
    //endregion

    //region Special Select
    /*
 * getting all todos under single tag
 * */
    public double getTotalExpensesByEventAndDate(int eventid, Date currDat) {

        String selectQuery = "SELECT  SUM(" + KEY_EXPENSE_COST + ") FROM " + TABLE_EXPENSE +
               " WHERE " + KEY_EXPENSE_EVENT_ID + " = " + eventid + " AND " + KEY_EXPENSE_DATE + " <= Datetime('" +
                getDateOnlyDateTime(currDat) + "')";

        // Try to execute
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                double dSum = c.getDouble(0);
                return dSum;
            }

            return 0;
        }
        catch (Exception e)
        {
            WriteToLog(e,selectQuery);
            return (Consts.SAVE_ERROR);
        }
    }

    private void WriteToLog(Exception e, String strMessage)
    {
        Log.e(LOG, e.getStackTrace()[e.getStackTrace().length - 1].getMethodName() + " Cant Execute :" + strMessage);
    }

    /**
     * getting all todos
     * */
    public ArrayList<Event> getCurrentEvents() {
        ArrayList<Event> allevents = new ArrayList<Event>();
        String selectQuery = "SELECT  " + EventColumnString + ", " + CurrencyColumnString +
                             " FROM " + TABLE_EVENT + " "+ EVENT_QUALIFIRE + ", " +
                                        TABLE_CURRENCY + " " + CURRENCY_QUALIFIRE +
                             " WHERE " + EVENT_QUALIFIRE + "." + KEY_EVENT_START_DATE + " <= '" + getTodayDateTime() +
                             "' AND " + EVENT_QUALIFIRE  + "." + KEY_EVENT_END_DATE + " >= '" + getTodayDateTime() + "' AND "
                                        + EVENT_QUALIFIRE + "." + KEY_EVENT_CURRENCY_ID + " = " + CURRENCY_QUALIFIRE + "." + KEY_ID;

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst())
            {
                do {
                    Event event = ReadEventToObject(c);

                    // adding to todo list
                    allevents.add(event);
                } while (c.moveToNext());
            }

            return allevents;
        }
        catch (Exception e)
        {
            Log.e(LOG, "GET_CURRENT_EVENTS: Cant execute " + selectQuery);

            return (null);
        }
    }

    public ArrayList<Expense> getAllExpensesByDateAndEvent(int nEventID, Date d) {
        ArrayList<Expense> allexepses = new ArrayList<Expense>();

        String selectQuery = "SELECT  " + ExpensesColumnString + ", "
                + EventColumnString + ", "
                + CategoriesColumnString + ", "
                + CurrencyColumnString + " " +
                " FROM " + TABLE_EXPENSE  + " " + EXPENSE_QUALIFIRE + ", " +
                TABLE_EVENT  + " " + EVENT_QUALIFIRE + ", " +
                TABLE_CURRENCY  + " " + CURRENCY_QUALIFIRE + ", " +
                TABLE_CATEGORY + " " + CATEGORY_QUALIFIRE +
                " WHERE expense." + KEY_EXPENSE_EVENT_ID + " = event." + KEY_ID +
                " AND " + EXPENSE_QUALIFIRE +"." + KEY_EXPENSE_CATEGORY_ID + " = cat." + KEY_ID +
                " AND " + EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_EVENT_ID + " = " + nEventID +
                " AND " + EVENT_QUALIFIRE + "." + KEY_EVENT_CURRENCY_ID + " = " + CURRENCY_QUALIFIRE + "." + KEY_ID +
                " AND " + EXPENSE_QUALIFIRE + "." + KEY_EXPENSE_DATE + " = '" + getDateOnlyDateTime(d) + "'";

        //  Log.e(LOG, selectQuery);

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Expense expense = ReadExpenseToObject(c);

                    // adding to todo list
                    allexepses.add(expense);
                } while (c.moveToNext());
            }

            return allexepses;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }
    //endregion

    //region Delete by ID
    /*
    * Deleting an Expense
    */
    public int deleteExpense(long expenseid)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_EXPENSE, KEY_ID + " = ?",
                    new String[]{String.valueOf(expenseid)});
        }
        catch (Exception e)
        {
            WriteToLog(e, "Delete Expense");
            return Consts.SAVE_ERROR;
        }
    }

    /*
    * Deleting an event
    */
    public int deleteEvent(long expenseid)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EVENT, KEY_ID + " = ?",
                    new String[]{String.valueOf(expenseid)});

            String whereClause = KEY_EXPENSE_EVENT_ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(expenseid)};
            return db.delete(TABLE_EXPENSE, whereClause, whereArgs);
        }
        catch (Exception e)
        {
            WriteToLog(e, "Delete event");
            return Consts.SAVE_ERROR;
        }
    }
    //endregion

    /*
 * Updating a todo
 */
    public int updateEvent(Event event)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getName());
        values.put(KEY_EVENT_MONEY_AMOUNT, event.getMoneyAmount());
        values.put(KEY_EVENT_START_DATE, getDateOnlyDateTime(event.getStartDate()));
        values.put(KEY_EVENT_END_DATE, getDateOnlyDateTime(event.getEndDate()));
        values.put(KEY_EVENT_PEOPLE_NUMBER, event.getNumOfPeople());
        values.put(KEY_EVENT_DAYS_NUMBER, event.getDaysNum());

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            deleteExpensesByEventDates(event);

            // updating row
            return db.update(TABLE_EVENT, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(event.getID())});
        }
        catch (Exception e)
        {
            WriteToLog(e, "Update Event");
            return (Consts.SAVE_ERROR);
        }
    }

    public int updateExpense(Expense expense)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_NAME, expense.getName());
        values.put(KEY_EXPENSE_COST, expense.getCost());
        values.put(KEY_EXPENSE_CATEGORY_ID, expense.getCategory().getID());

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            // updating row
            return db.update(TABLE_EXPENSE, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(expense.getID())});
        }
        catch (Exception e)
        {
            WriteToLog(e, "Update Expense");
            return Consts.SAVE_ERROR;
        }
    }

    public int updateCurrencyValue(Currency curr)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENCY_VALUE, curr.getValue());

        try
        {
            // updating row
            SQLiteDatabase db = this.getWritableDatabase();

            return db.update(TABLE_EVENT, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(curr.getID())});
        }
        catch (Exception e)
        {
            WriteToLog(e, "Update Currency");
            return Consts.SAVE_ERROR;
        }
    }

    //region Delete by ID
    /*
    * Deleting an Expense
    */
    public int deleteExpensesByEventDates(Event event)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return (db.delete(TABLE_EXPENSE, KEY_EXPENSE_EVENT_ID + " = ? AND " + KEY_EXPENSE_DATE + " NOT BETWEEN ? AND ?",
                    new String[]{String.valueOf(event.getID()),
                            getDateOnlyDateTime(event.getStartDate()),
                            getDateOnlyDateTime(event.getEndDate())}));
        }
        catch (Exception e)
        {
            WriteToLog(e, "Delete Expense");
            return Consts.SAVE_ERROR;
        }
    }

    public ArrayList<Expense> getGeneralExpensesByDay()
    {
        ArrayList<Expense> expensespredate = new ArrayList<Expense>();

        String selectQuery = "SELECT  " + KEY_EXPENSE_EVENT_ID + ", SUM(" + KEY_EXPENSE_COST + "), " + KEY_EXPENSE_DATE +
                " FROM " + TABLE_EXPENSE +
                " GROUP BY " + KEY_EXPENSE_EVENT_ID + ", " + KEY_EXPENSE_DATE +
                " ORDER BY " + KEY_EXPENSE_DATE;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Expense expense = ReadGeneralExpenseToObject(c);

                    expensespredate.add(expense);

                    // adding to todo list
                } while (c.moveToNext());
            }

            return expensespredate;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }

    public HashMap<Date, Double> getExpensesSumPerDay()
    {
        HashMap<Date, Double> expensespredate = new HashMap<Date, Double>();

        String selectQuery = "SELECT SUM(" + KEY_EXPENSE_COST + "), " + KEY_EXPENSE_DATE +
                " FROM " + TABLE_EXPENSE +
                " GROUP BY " + KEY_EXPENSE_DATE +
                " ORDER BY " + KEY_EXPENSE_DATE;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {

                    Date date = getDateFromDateTime(c.getString(c.getColumnIndex(KEY_EXPENSE_DATE)));
                    Double dValue = c.getDouble(0);
                    expensespredate.put(date, dValue);
                    // adding to todo list
                } while (c.moveToNext());
            }

            return expensespredate;
        }
        catch (Exception e)
        {
            WriteToLog(e, selectQuery);
            return null;
        }
    }


    //region Delete by ID
    /*
    * Deleting an Expense
    */
    public int UpdateCurrencyValueByCode(String strCode, double dValue)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENCY_VALUE, dValue);

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            // updating row
            return db.update(TABLE_CURRENCY, values, KEY_CURRENCY_CODE + " = ?",
                    new String[]{strCode});
        }
        catch (Exception e)
        {
            WriteToLog(e, "UpdateCurrency");
            return (Consts.SAVE_ERROR);
        }
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}