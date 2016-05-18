package wasteit.wasteit;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.CategoryManager;
import info.androidhive.sqlite.manager.CurrencyManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;

public class FastNewExpenseDialogActivity extends AppCompatActivity implements NewExpenseDialogFragment.OnCreateExpenseListener{

    private Event m_event;

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void UpdateExpenses()
    {
        Intent intent = new Intent(this,AddExpenseWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AddExpenseWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_new_expense_dialog);

        Intent intent = getIntent();
        m_event =(Event) intent.getExtras().getSerializable("CurrentEvent");

        ExpenseManager.newInstance(this);
        CategoryManager.newInstance(this);
        CurrencyManager.newInstance(this);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NewExpenseDialogFragment newEpense = NewExpenseDialogFragment.newInstance(m_event, Services.GetToDayAtStart(), null);
        newEpense.show(ft, "NewExpense");
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;//setting the gravity just like any view
        lp.x = 10;
        lp.y = 10;
        lp.width = 200;
        lp.height = 100;
        getWindowManager().updateViewLayout(view, lp);
    }
}
