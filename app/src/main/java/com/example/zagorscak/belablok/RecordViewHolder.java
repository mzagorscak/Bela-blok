package com.example.zagorscak.belablok;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_Record_We;
    private TextView tv_Record_They;
    private RecordClickListener recordClickListener;

    public RecordViewHolder(@NonNull View itemView, RecordClickListener listener) {
        super(itemView);
        tv_Record_We = itemView.findViewById(R.id.tv_Record_We);
        tv_Record_They = itemView.findViewById(R.id.tv_Record_They);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordClickListener.onRecordClick(getAdapterPosition());
            }
        });
        recordClickListener = listener;
    }

    public void setRecordPointsForUs(String points)
    {
        tv_Record_We.setText(points);
    }

    public void setRecordPointsForThem(String points)
    {
        tv_Record_They.setText(points);
    }

}
