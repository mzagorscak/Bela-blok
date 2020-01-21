package com.example.zagorscak.belablok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {


    private List<BelaRecord> records;

    private RecordClickListener recordClickListener;

    @SuppressLint("UseSparseArrays")
    public RecordAdapter(RecordClickListener recordClickListener, List<BelaRecord> r) {
        this.recordClickListener = recordClickListener;
        records = new ArrayList<>();
        if(r != null)
        {
            records.addAll(r);
        }
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recordView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record,parent,false);
        return new RecordViewHolder(recordView, recordClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        String record_We = this.records.get(position).getPoints_We_Sum().toString();
        String record_They = this.records.get(position).getPoints_They_Sum().toString();
        holder.setRecordPointsForUs(record_We);
        holder.setRecordPointsForThem(record_They);
    }



    @Override
    public int getItemCount() {
        return records.size();
    }

    public List<BelaRecord> getRecords()
    {
        List<BelaRecord> r = new ArrayList<>();
        r.addAll(records);
        return r;
    }

    public void addRecords(List<BelaRecord> r)
    {
            records.clear();
            records.addAll(r);
            notifyDataSetChanged();
    }

    public void addRecord(BelaRecord r,Integer position)
    {
        if (records.size() >= position && r != null)
        {
            records.add(position,r);
            notifyItemInserted(position);
        }
    }

    public void deleteAllRecords()
    {
        records.clear();
        notifyDataSetChanged();
    }

    public void editRecord(BelaRecord edited_Record, Integer position)
    {
        records.get(position).setPoints_We(edited_Record.getPoints_We());
        records.get(position).setPoints_They(edited_Record.getPoints_They());
        records.get(position).setCall_Points_We(edited_Record.getCall_Points_We());
        records.get(position).setCall_Points_They(edited_Record.getCall_Points_They());
        records.get(position).setPoints_We_Sum(edited_Record.getPoints_We_Sum());
        records.get(position).setPoints_They_Sum(edited_Record.getPoints_They_Sum());
    }

    public Integer getCurrentPointsForUs()
    {
        Integer sum = 0;
        for (Integer i=0;i<records.size();i++)
        {
            sum += records.get(i).getPoints_We_Sum();
        }
        return sum;
    }

    public Integer getCurrentPointsForThem()
    {
        Integer sum = 0;
        for (Integer i=0;i<records.size();i++)
        {
            sum += records.get(i).getPoints_They_Sum();
        }
        return sum;
    }

}
