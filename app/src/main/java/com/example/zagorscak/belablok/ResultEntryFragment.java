package com.example.zagorscak.belablok;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ResultEntryFragment extends Fragment implements View.OnClickListener {

    private static final String BUNDLE_RECORD = "record";
    private static final String POSITION = "position";

    private Button btn_Back;
    private Button btn_Call_20;
    private Button btn_Call_50;
    private Button btn_Call_100;
    private Button btn_Call_150;
    private Button btn_Call_200;
    private Button btn_DeleteCalls;
    private Button btn_Insert;
    private CheckedTextView ctv_Points_Calls_We;
    private CheckedTextView ctv_Points_Calls_They;
    private CheckedTextView ctv_Points_We_Sum;
    private CheckedTextView ctv_Points_They_Sum;

    private NumberPicker np_Points_Game1;
    private NumberPicker np_Points_Game2;
    private NumberPicker np_Points_Game3;

    private BelaRecord record;

    private BackButtonClickListener backButtonClickListener;
    private AddRecordButtonClickListener addRecordButtonClickListener;

    public ResultEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI(view);
    }

    public static ResultEntryFragment newInstance(@Nullable BelaRecord r, @Nullable Integer position)
    {
        ResultEntryFragment fragment = new ResultEntryFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_RECORD,r);
        if(position != null)
        {
            args.putInt(POSITION,position);
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeUI(View view) {
        btn_Back = view.findViewById(R.id.btn_Back);
        btn_Call_20 = view.findViewById(R.id.btn_Call_20);
        btn_Call_20.setOnClickListener(this);
        btn_Call_50 = view.findViewById(R.id.btn_Call_50);
        btn_Call_50.setOnClickListener(this);
        btn_Call_100 = view.findViewById(R.id.btn_Call_100);
        btn_Call_100.setOnClickListener(this);
        btn_Call_150 = view.findViewById(R.id.btn_Call_150);
        btn_Call_150.setOnClickListener(this);
        btn_Call_200 = view.findViewById(R.id.btn_Call_200);
        btn_Call_200.setOnClickListener(this);
        btn_DeleteCalls = view.findViewById(R.id.btn_DeleteCalls);
        btn_Insert = view.findViewById(R.id.btn_Insert);
        ctv_Points_Calls_We = view.findViewById(R.id.ctv_Points_Calls_We);
        ctv_Points_Calls_They = view.findViewById(R.id.ctv_Points_Calls_They);
        ctv_Points_We_Sum = view.findViewById(R.id.ctv_Points_We_Sum);
        ctv_Points_They_Sum = view.findViewById(R.id.ctv_Points_They_Sum);

        np_Points_Game1 = view.findViewById(R.id.np_Points_Game1);
        np_Points_Game1.setMinValue(0);
        np_Points_Game1.setMaxValue(1);
        np_Points_Game2 = view.findViewById(R.id.np_Points_Game2);
        np_Points_Game2.setMinValue(0);
        np_Points_Game2.setMaxValue(9);
        np_Points_Game3 = view.findViewById(R.id.np_Points_Game3);
        np_Points_Game3.setMinValue(0);
        np_Points_Game3.setMaxValue(9);


        NumberPicker.OnValueChangeListener onNumberPickerValueChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(np_Points_Game1.getValue() == 1)
                {
                    np_Points_Game2.setMaxValue(6);
                    if(np_Points_Game2.getValue() == 6)
                    {
                        np_Points_Game3.setMaxValue(2);
                    }
                    else np_Points_Game3.setMaxValue(9);
                }
                else
                {
                    np_Points_Game2.setMaxValue(9);
                    np_Points_Game3.setMaxValue(9);
                }
                Integer value = getValueFromNumberPickers();

                if(ctv_Points_We_Sum.isChecked())
                {
                    record.setPoints_We(value);
                    record.setPoints_They(162 -  value);
                }
                else
                {

                    record.setPoints_They(value);
                    record.setPoints_We(162 -  value);
                }
                setUpPoints();
            }
        };

        np_Points_Game1.setOnValueChangedListener(onNumberPickerValueChangedListener);
        np_Points_Game2.setOnValueChangedListener(onNumberPickerValueChangedListener);
        np_Points_Game3.setOnValueChangedListener(onNumberPickerValueChangedListener);

        record = new BelaRecord();


        if (getArguments() != null) {
            BelaRecord argRecord = getArguments().getParcelable(BUNDLE_RECORD);
            if(argRecord != null)
            {
                record.setPoints_We(argRecord.getPoints_We());
                record.setPoints_They(argRecord.getPoints_They());
                record.setCall_Points_We(argRecord.getCall_Points_We());
                record.setCall_Points_They(argRecord.getCall_Points_They());
                record.setPoints_We_Sum(argRecord.getPoints_We_Sum());
                record.setPoints_They_Sum(argRecord.getPoints_They_Sum());
            }
        }
        setUpPoints();
        ctv_Points_We_Sum.setChecked(true);
        ctv_Points_Calls_We.setChecked(true);
        if(!record.getPoints_We().toString().equals("0"))
        {
            setValueToNumberPickers(record.getPoints_We());
        }
        else
        {
            setValueToNumberPickers(0);
        }
        setUpListeners();
    }

    private void setUpListeners()
    {

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonClickListener.onBackButtonClick();
            }
        });

        btn_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putParcelable(BUNDLE_RECORD,record);
                if(getArguments() != null && getArguments().get(POSITION)!= null)
                {
                    args.putInt(POSITION, getArguments().getInt(POSITION));
                }
                if(record.getPoints_We() !=0 || record.getPoints_They() != 0)
                {
                    addRecordButtonClickListener.onInsertButtonClick(args);
                }
                else
                {
                    Toast.makeText(getContext(),"Unos je prazan.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_DeleteCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctv_Points_Calls_We.getText().equals("0") || !ctv_Points_Calls_They.getText().equals("0")) {
                    record.setPoints_We_Sum(record.getPoints_We_Sum()-record.getCall_Points_We());
                    record.setPoints_They_Sum(record.getPoints_They_Sum()-record.getCall_Points_They());
                    record.setCall_Points_We(0);
                    record.setCall_Points_They(0);
                    setUpPoints();
                }
            }
        });

        ctv_Points_We_Sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctv_Points_We_Sum.isChecked()) {
                    ctv_Points_We_Sum.setChecked(true);
                    ctv_Points_They_Sum.setChecked(false);
                    record.setPoints_We(getValueFromNumberPickers());
                    record.setPoints_They(162 -  record.getPoints_We());
                    setUpPoints();

                }
            }
        });

        ctv_Points_They_Sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctv_Points_They_Sum.isChecked()) {
                    ctv_Points_They_Sum.setChecked(true);
                    ctv_Points_We_Sum.setChecked(false);
                    record.setPoints_They(getValueFromNumberPickers());
                    record.setPoints_We(162 -  record.getPoints_They());
                    setUpPoints();

                }
            }
        });

        ctv_Points_Calls_We.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctv_Points_Calls_We.isChecked())
                {
                    ctv_Points_Calls_We.setChecked(true);
                    ctv_Points_Calls_They.setChecked(false);
                }
            }
        });

        ctv_Points_Calls_They.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctv_Points_Calls_They.isChecked())
                {
                    ctv_Points_Calls_They.setChecked(true);
                    ctv_Points_Calls_We.setChecked(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Integer call_Points;
        if(ctv_Points_Calls_We.isChecked())
        {
            call_Points = record.getCall_Points_We();
        }
        else call_Points = record.getCall_Points_They();

        switch (v.getId())
        {

            case R.id.btn_Call_20:
                call_Points += 20;
                break;
            case R.id.btn_Call_50:
                call_Points += 50;
                break;
            case R.id.btn_Call_100:
                call_Points += 100;
                break;
            case R.id.btn_Call_150:
                call_Points += 150;
                break;
            case R.id.btn_Call_200:
                call_Points += 200;
                break;
            default:
                break;
        }

        if(ctv_Points_Calls_We.isChecked())
        {
            record.setCall_Points_We(call_Points);
        }
        else
        {
            record.setCall_Points_They(call_Points);
        }

        setUpPoints();
    }

    private Integer getValueFromNumberPickers()
    {
        return np_Points_Game1.getValue()*100+np_Points_Game2.getValue()*10+np_Points_Game3.getValue();
    }

    private void setValueToNumberPickers(Integer number)
    {
        Integer np1 = number/100;
        Integer np2 = (number/10)%10;
        Integer np3 = number%10;

        np_Points_Game1.setValue(np1);
        np_Points_Game2.setValue(np2);
        np_Points_Game3.setValue(np3);
    }

    private void setUpPoints()
    {
        record.setPoints_We_Sum(record.getPoints_We()+record.getCall_Points_We());
        record.setPoints_They_Sum(record.getPoints_They()+record.getCall_Points_They());
        ctv_Points_We_Sum.setText(record.getPoints_We_Sum().toString());
        ctv_Points_They_Sum.setText(record.getPoints_They_Sum().toString());
        setUpCallPoints();
    }

    private void setUpCallPoints()
    {
        ctv_Points_Calls_We.setText(record.getCall_Points_We().toString());
        ctv_Points_Calls_They.setText(record.getCall_Points_They().toString());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BackButtonClickListener) {
            this.backButtonClickListener = (BackButtonClickListener) context;
        }
        if (context instanceof AddRecordButtonClickListener) {
            this.addRecordButtonClickListener = (AddRecordButtonClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backButtonClickListener = null;
        addRecordButtonClickListener = null;
    }
}
