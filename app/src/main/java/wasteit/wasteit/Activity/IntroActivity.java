package wasteit.wasteit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import info.androidhive.sqlite.helper.DatabaseHelper;
import info.androidhive.sqlite.helper.AsyncCallWS;
import info.androidhive.sqlite.manager.CurrencyManager;
import wasteit.wasteit.R;

public class IntroActivity extends AppCompatActivity implements AsyncCallWS.LoadCurrencyListener {

    //region DM
    // My DB
    DatabaseHelper dbHelper;

    // Views
    ProgressBar m_pProgress;
    //endregion

    //region Getter Setter
    // Get DB
    public DatabaseHelper GetDB()
    {
        return dbHelper;
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // dbHelper = new DatabaseHelper(this);
       // dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1,1);

        CurrencyManager.newInstance(this);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Loading the views
        setContentView(R.layout.activity_intro);
        m_pProgress = (ProgressBar) findViewById(R.id.intro_progress);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Loadin Currnecies value from web
        AsyncCallWS asyncCallWS = new AsyncCallWS(this);
        asyncCallWS.execute();
    }

    @Override
    public void FinishLoading()
    {
        // Start the app
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void SetProgress(int nProgress)
    {
        // Setting the loading progress
        m_pProgress.setProgress(nProgress);
    }
}
