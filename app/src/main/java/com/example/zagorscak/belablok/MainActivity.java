package com.example.zagorscak.belablok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecordClickListener,
        NewRecordButtonClickListener, BackButtonClickListener, AddRecordButtonClickListener {

    private static final String BUNDLE_RECORD = "record";
    private static final String POSITION = "position";
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String SHARED_PREFERENCES_LIST = "shared_preferences_list";
    private static final String SHARED_PREFERENCES_POINTS_TOTAL_WE = "shared_preferences_pointsTotalWe";
    private static final String SHARED_PREFERENCES_POINTS_TOTAL_THEY = "shared_preferences_pointsTotalThey";
    private static final String SHARED_PREFERENCES_POINTSTOWIN = "shared_preferences_pointsToWin";

    private ResultsFragment resultsFragment;
    private ResultEntryFragment resultEntryFragment;

    private FragmentManager fragmentManager;

    private Menu optionsMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        setUpFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options,menu);
        loadPointsToWinInMenu();
        return true;
    }

    private void loadPointsToWinInMenu()
    {
        Integer pointsToWin = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE).getInt(SHARED_PREFERENCES_POINTSTOWIN,1001);
        switch (pointsToWin)
        {
            case 501:
                optionsMenu.findItem(R.id.menuItem_pointsToWin_501).setChecked(true);
                break;
            case 701:
                optionsMenu.findItem(R.id.menuItem_pointsToWin_701).setChecked(true);
                break;
            case 1001:
                optionsMenu.findItem(R.id.menuItem_pointsToWin_1001).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuItem_reset:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Sigurno želite resetirati rezultate?").setCancelable(false)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resultsFragment.getRecordAdapter().deleteAllRecords();
                                ResultsFragment.setPointsTotalWe(0);
                                ResultsFragment.setPointsTotalThey(0);
                                reloadResultsFragment();
                            }
                        })
                        .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = dialogBuilder.create();
                dialog.setTitle("");
                dialog.show();
                return true;
            case R.id.menuItem_pointsToWin_501:
                ResultsFragment.setPointsToWin(501);
                reloadResultsFragment();
                setCheckedMenuItemPoints(item);
                return true;
            case R.id.menuItem_pointsToWin_701:
                ResultsFragment.setPointsToWin(701);
                reloadResultsFragment();
                setCheckedMenuItemPoints(item);
                return true;
            case R.id.menuItem_pointsToWin_1001:
                ResultsFragment.setPointsToWin(1001);
                reloadResultsFragment();
                setCheckedMenuItemPoints(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setCheckedMenuItemPoints(MenuItem item)
    {
        optionsMenu.findItem(R.id.menuItem_pointsToWin_501).setChecked(false);
        optionsMenu.findItem(R.id.menuItem_pointsToWin_701).setChecked(false);
        optionsMenu.findItem(R.id.menuItem_pointsToWin_1001).setChecked(false);
        item.setChecked(true);

    }

    private void setUpFragment() {
        List<BelaRecord> list;
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFERENCES_LIST,"");
        Type type = new TypeToken<List<BelaRecord>>(){}.getType();
        if(!json.equals(""))
        {
            list = gson.fromJson(json,type);
        }
        else list = new ArrayList<BelaRecord>();

        ResultsFragment.setPointsTotalWe(sharedPreferences.getInt(SHARED_PREFERENCES_POINTS_TOTAL_WE,0));
        ResultsFragment.setPointsTotalThey(sharedPreferences.getInt(SHARED_PREFERENCES_POINTS_TOTAL_THEY,0));
        ResultsFragment.setPointsToWin(sharedPreferences.getInt(SHARED_PREFERENCES_POINTSTOWIN,1001));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try
        {
            resultsFragment = ResultsFragment.newInstance(list, null, null);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot reinstantiate fragment " , e);
        }
        fragmentTransaction.add(R.id.fl_FragmentHolder,resultsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRecordClick(int position) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try
        {
            resultEntryFragment = ResultEntryFragment.newInstance(resultsFragment.getRecordAdapter().getRecords().get(position), position);

        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot reinstantiate fragment " , e);
        }
        fragmentTransaction.replace(R.id.fl_FragmentHolder,resultEntryFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewRecordButtonClick() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try
        {
            resultEntryFragment = ResultEntryFragment.newInstance(new BelaRecord(), null);

        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot reinstantiate fragment " , e);
        }

        fragmentTransaction.replace(R.id.fl_FragmentHolder,resultEntryFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackButtonClick() {
        reloadResultsFragment();
    }

    private void reloadResultsFragment()
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try
        {
            resultsFragment = ResultsFragment.newInstance(resultsFragment.getRecordAdapter().getRecords(), null, null);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot reinstantiate fragment " , e);
        }
        fragmentTransaction.replace(R.id.fl_FragmentHolder,resultsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onInsertButtonClick(Bundle b) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try
        {
            resultsFragment = ResultsFragment.newInstance(resultsFragment.getRecordAdapter().getRecords(), (BelaRecord) b.getParcelable(BUNDLE_RECORD), (Integer) b.get(POSITION));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot reinstantiate fragment " , e);
        }
        fragmentTransaction.replace(R.id.fl_FragmentHolder,resultsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(!resultsFragment.isVisible())
        {
            onBackButtonClick();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(resultsFragment.getRecordAdapter().getRecords());
        editor.putString(SHARED_PREFERENCES_LIST,json);
        editor.putInt(SHARED_PREFERENCES_POINTS_TOTAL_WE,ResultsFragment.getPointsTotalWe());
        editor.putInt(SHARED_PREFERENCES_POINTS_TOTAL_THEY,ResultsFragment.getPointsTotalThey());
        editor.putInt(SHARED_PREFERENCES_POINTSTOWIN,ResultsFragment.getPointsToWin());
        editor.apply();
    }
}
