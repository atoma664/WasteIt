package wasteit.wasteit.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.Date;

import info.androidhive.sqlite.helper.Services;
import info.androidhive.sqlite.manager.EventsManager;
import info.androidhive.sqlite.manager.ExpenseManager;
import info.androidhive.sqlite.model.Event;
import wasteit.wasteit.Dialog.FastNewExpenseDialogActivity;
import wasteit.wasteit.R;
import wasteit.wasteit.Activity.StartActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AddExpenseWidgetConfigureActivity AddExpenseWidgetConfigureActivity}
 */
public class AddExpenseWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = null;

        // Recieve the event
        int eventID = AddExpenseWidgetConfigureActivity.loadEventID(context, appWidgetId);
        Event eCurrent = EventsManager.newInstance(context).getEvent(eventID);

        // If the event still exist
        if (eCurrent != null)
        {
            views = new RemoteViews(context.getPackageName(), R.layout.add_expense_widget);

            views.setTextViewText(R.id.widget_currency, eCurrent.getCurrency().toString());

            // Get today info
            Date today = Services.GetToDayAtStart();
            Date yesterday = Services.GetNextPrevDate(today, -1);

            // Calc the days past
            int nDaysLeft = eCurrent.getDaysNum() - Services.DaysBetween(eCurrent.getStartDate(), today);
            int nDayPast = eCurrent.getDaysNum() - nDaysLeft;

            // Calc the total balance
            double dSpentTillNow = ExpenseManager.newInstance(context).getTotalExpensesByEventAndDate(eventID, today);
            double dSpentTillToday = ExpenseManager.newInstance(context).getTotalExpensesByEventAndDate(eventID, yesterday);
            double dTotalBalance = Services.ReturnRound(eCurrent.getMoneyAmount() - dSpentTillNow);

            // My Daily Expenses
            double dSpentToday = dSpentTillNow - dSpentTillToday;
            double dLeftToday;

            // Calc my daily budget
            double dAllowance;

            // If the event was over
            if (nDaysLeft <= 0)
            {
                dAllowance = dTotalBalance;
            }
            else
            {
                dAllowance = Services.ReturnRound((eCurrent.getMoneyAmount() - dSpentTillToday) / nDaysLeft);
            }

            // Calc the precent
            dLeftToday = dAllowance - dSpentToday;
            int nDailyPercent = (int) Math.floor(dLeftToday * 100 / dAllowance);

            // Set the balance title
            if (nDaysLeft < 0)
            {
                views.setTextViewText(R.id.widget_blance_title, context.getString(R.string.widget_event_over));

                // calc the percent do to the last day
                nDailyPercent =(int) Math.round(dTotalBalance * 100 / eCurrent.getMoneyAmount());
            }
            else if (nDaysLeft > eCurrent.getDaysNum())
            {
                views.setTextViewText(R.id.widget_blance_title, context.getString(R.string.widget_event_not_start));
            }

            // Set the background color accurding to the amount
            if (dLeftToday < 0)
            {
                views.setInt(R.id.widget_back, "setBackgroundColor", context.getResources().getColor(R.color.redTransparent));
            }
            else
            {
                views.setInt(R.id.widget_back, "setBackgroundColor", context.getResources().getColor(R.color.lightBlueTransparent));
            }

            // Setting the views info
            views.setTextViewText(R.id.widget_event_daily_balance, String.valueOf(Services.ReturnRound(dLeftToday)));
            views.setTextViewText(R.id.widget_event_name, eCurrent.getName());
            views.setProgressBar(R.id.widget_daily_progress, 100, nDailyPercent, false);

            // Setting the click button accurding to the state of the event
           if (Services.GetToDayAtStart().before(eCurrent.getStartDate()) ||
                   Services.GetToDayAtStart().after(eCurrent.getEndDate()))
           {
               Intent intent = new Intent(context, StartActivity.class);
               intent.putExtra("StartEvent", eCurrent.getID());
               PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

               views.setTextViewText(R.id.widget_button_create, context.getString(R.string.widget_show_event));
               views.setOnClickPendingIntent(R.id.widget_button_create, pendingIntent);
           }
           else
           {
               Intent intent = new Intent(context, FastNewExpenseDialogActivity.class);
               intent.putExtra("CurrentEvent", eCurrent);
               //  intent.putExtra("StartFragment", "NewExpenseFragment");
               PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                views.setOnClickPendingIntent(R.id.widget_button_create, pendingIntent);
           }
        }
        else
        {
            views = new RemoteViews(context.getPackageName(), R.layout.event_deleted_widget);

            Intent launchActivity = new Intent(context, AddExpenseWidgetConfigureActivity.class);
            launchActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            launchActivity.setData(Uri.withAppendedPath(Uri.parse("abc" + "://widget/id/"), String.valueOf(appWidgetId)));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_button_create, pendingIntent);
            ComponentName thisWidget = new ComponentName(context, AddExpenseWidget.class);

           // Intent resultValue = new Intent();
           // resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //   setResult(RESULT_OK, resultValue);
            //  finish();
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            AddExpenseWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

