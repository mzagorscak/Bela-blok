package com.example.zagorscak.belablok;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment {

    private static final String BUNDLE_RECORD = "record";
    private static final String BUNDLE_RECORD_LIST = "record_list";
    private static final String POSITION = "position";

    private TextView tv_Points_Current_We;
    private TextView tv_Points_Current_They;
    private TextView tv_Points_Total_We;
    private TextView tv_Points_Total_They;
    private Button btn_NewRecord;

    private static Integer pointsTotalWe = 0;
    private static Integer pointsTotalThey = 0;

    private static Integer pointsToWin = 0;

    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private RecordClickListener recordClickListener;
    private NewRecordButtonClickListener newRecordButtonClickListener;

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initalizeUI(view);
        setupRecylcer(view);
        checkForWin();
        setPoints();
        btn_NewRecord.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                newRecordButtonClickListener.onNewRecordButtonClick();
            }
        });
        tv_Points_Current_We.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pointsToWinTheGame = pointsToWin - recordAdapter.getCurrentPointsForUs();
                Toast.makeText(getContext(),"Do izlaska nedostaje: "+ pointsToWinTheGame.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        tv_Points_Current_They.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pointsToWinTheGame = pointsToWin - recordAdapter.getCurrentPointsForThem();
                Toast.makeText(getContext(),"Do izlaska nedostaje: "+ pointsToWinTheGame.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    //List<BelaRecord> r -> fragment se svaki put mora recreatat
    //@Nullable BelaRecord newRecord -> parametar koji se dodaje ili ureduje listu koja se nalazi u recordAdapter-u
    //@Nullable Integer position -> ako == null onda se dodaje, ako != null onda se ureduje postojeci zapis
    public static ResultsFragment newInstance(List<BelaRecord> r, @Nullable BelaRecord newRecord, @Nullable Integer position)
    {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_RECORD_LIST, (ArrayList<? extends Parcelable>) r);
        if(newRecord != null)
        {
            args.putParcelable(BUNDLE_RECORD,newRecord);
        }
        if(position != null)
        {
            args.putInt(POSITION, position);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    private void checkForWin()
    {
        if(weWin())
        {
            Toast.makeText(getContext(),"Mi smo pobjedili.", Toast.LENGTH_SHORT).show();
            pointsTotalWe++;
            tv_Points_Total_We.setText(pointsTotalWe.toString());
            recordAdapter.deleteAllRecords();
            if(getArguments() != null)
            {
                this.getArguments().clear();
            }
        }
        else if(theyWin())
        {
            Toast.makeText(getContext(),"Vi ste pobjedili.", Toast.LENGTH_SHORT).show();
            pointsTotalThey++;
            tv_Points_Total_They.setText(pointsTotalThey.toString());
            recordAdapter.deleteAllRecords();
            if(getArguments() != null)
            {
                this.getArguments().clear();
            }
        }
    }

    private boolean weWin()
    {
        return recordAdapter.getCurrentPointsForUs() >= pointsToWin  && recordAdapter.getCurrentPointsForUs()>recordAdapter.getCurrentPointsForThem();
    }

    private boolean theyWin()
    {
        return recordAdapter.getCurrentPointsForThem() >= pointsToWin && recordAdapter.getCurrentPointsForUs()<recordAdapter.getCurrentPointsForThem();
    }

    private void initalizeUI(View view) {
        tv_Points_Current_We = view.findViewById(R.id.tv_Points_Current_We);
        tv_Points_Current_They = view.findViewById(R.id.tv_Points_Current_They);
        tv_Points_Total_We = view.findViewById(R.id.tv_Points_Total_We);
        tv_Points_Total_They = view.findViewById(R.id.tv_Points_Total_They);
        btn_NewRecord = view.findViewById(R.id.btn_NewRecord);
    }

    @SuppressLint("SetTextI18n")
    private void setPoints()
    {
        tv_Points_Current_We.setText(recordAdapter.getCurrentPointsForUs().toString());
        tv_Points_Current_They.setText(recordAdapter.getCurrentPointsForThem().toString());
        tv_Points_Total_We.setText(pointsTotalWe.toString());
        tv_Points_Total_They.setText(pointsTotalThey.toString());
    }

    private void setupRecylcer(View view) {
        List<BelaRecord> records;
        BelaRecord record = null;
        if (getArguments() != null) {
            records = getArguments().getParcelableArrayList(BUNDLE_RECORD_LIST);
            record = getArguments().getParcelable(BUNDLE_RECORD);
        }
        else
        {
            records = new ArrayList<>();
        }
        recyclerView = view.findViewById(R.id.rv_Records);
        recyclerView.setLayoutManager(new LinearLayoutManager((Context) recordClickListener));
        recordAdapter = new RecordAdapter(recordClickListener, records);
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.addRecords(records);
        if(getArguments() != null)
        {
            if(getArguments().get(POSITION)== null)
            {
                recordAdapter.addRecord(record,recordAdapter.getItemCount());
            }
            else if(record != null)
            {
                recordAdapter.editRecord(record,getArguments().getInt(POSITION));
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RecordClickListener) {
            this.recordClickListener = (RecordClickListener) context;
        }
        if (context instanceof NewRecordButtonClickListener) {
            this.newRecordButtonClickListener = (NewRecordButtonClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.recordClickListener = null;
        this.newRecordButtonClickListener = null;
    }

    public RecordAdapter getRecordAdapter() {
        return recordAdapter;
    }

    public static void setPointsTotalWe(Integer points)
    {
        pointsTotalWe = points;
    }

    public static void setPointsTotalThey(Integer points)
    {
        pointsTotalThey = points;
    }

    public static Integer getPointsTotalWe()
    {
        return pointsTotalWe;
    }

    public static Integer getPointsTotalThey()
    {
        return pointsTotalThey;
    }

    public static Integer getPointsToWin() {
        return pointsToWin;
    }

    public static void setPointsToWin(Integer points) {
        pointsToWin = points;
    }
}
