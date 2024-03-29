package com.example.smartmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Income_piechart_Activity extends AppCompatActivity {

    private PieChart pieChart;
    private FirebaseAuth mAuth;
    private DatabaseReference mincomeDatabase;
    private ArrayList<PieEntry> pieEntryArrayList;
    private ArrayList<Data> dataArrayList ;
    private ArrayList<String> strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_piechart);

        pieChart=findViewById(R.id.income_pie_chart);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser =mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mincomeDatabase = FirebaseDatabase.getInstance().getReference().child("Income_Data").child(uid);

        pieEntryArrayList = new ArrayList<>();
        strings= new ArrayList<>();


        mincomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataArrayList =  new ArrayList<>();
                for (DataSnapshot mysnapshot: snapshot.getChildren()){

                    Data data = mysnapshot.getValue(Data.class);
                    dataArrayList.add(new Data(data.getType(),data.getAmount()));
                }
                for (int i=0;i<dataArrayList.size();i++){
                    String type=dataArrayList.get(i).getType();
                    int amount = dataArrayList.get(i).getAmount();

                    // Log.i(TAG, "value of "+i+amount);
                    //strings.add(type);
                    pieEntryArrayList.add(new PieEntry(amount,type));
                }


                showchart(pieEntryArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void showchart(ArrayList<PieEntry> pieEntryArrayList) {

        PieDataSet dataSet = new PieDataSet(pieEntryArrayList,"Income");

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        dataSet.setColors(colors);

        PieData dataa = new PieData(dataSet);
        dataa.setDrawValues(true);
        dataa.setValueFormatter(new PercentFormatter(pieChart));
        dataa.setValueTextSize(12f);
        dataa.setValueTextColors(Collections.singletonList(Color.BLUE));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("INCOME");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Income Data");
        pieChart.setDescription(description);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        pieChart.setData(dataa);
        pieChart.animateX(2500);
        pieChart.invalidate();
    }
}