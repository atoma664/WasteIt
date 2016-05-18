package wasteit.wasteit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import info.androidhive.sqlite.model.Event;

public class CreateEventWidgetActivity extends AppCompatActivity implements NewEventFragment.OnNewEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_widget);
    }

    @Override
    public void showTruitonDatePickerDialog(View view, Date start, Date end)
    {
        StartActivity.showDatePickerDialog(view, start, end, getSupportFragmentManager());
    }

    @Override
    public void ShowEventDetails(Event newEvent) {
        finish();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void CancelNewEditEvent() {
        finish();
    }

    @Override
    public void UpdateEvent() {
        finish();
    }
}
