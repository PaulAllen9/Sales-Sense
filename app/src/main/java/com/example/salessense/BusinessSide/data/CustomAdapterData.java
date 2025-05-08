package com.example.salessense.BusinessSide.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/*
 * Author: ????????
 * Created: ????????
 * Modified by: Brian Yang - 5/6/25
 * - Removed ImageView and added MPAndroidChart support
 *
 */
public class CustomAdapterData extends RecyclerView.Adapter<CustomAdapterData.MyViewHolder>{

    private Context context;
    private ArrayList<String> tableTitles;
    // private ArrayList<Integer> tableImages;
    private List<List<String>> chartLabelsList;

    List<List<BarEntry>> chartDataList; // Brian: Each row gets its own chart data


    public CustomAdapterData(Context context, ArrayList<String> tableTitles,
                             List<List<BarEntry>> chartDataList,
                             List<List<String>> chartLabelsList) {
        this.context = context;
        this.tableTitles = tableTitles;
        this.chartDataList = chartDataList;
        this.chartLabelsList = chartLabelsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the chart card layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.data_display_card, parent, false);

        return new MyViewHolder(view);
    }


    // Brian add: all
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tableTitleTV.setText(String.valueOf(tableTitles.get(position)));
        // holder.dataVisual.setImageResource(tableImages.get(position));

        // Get the chart entries for this card
        List<BarEntry> entries = chartDataList.get(position);

        // Create the dataset
        BarDataSet dataSet = new BarDataSet(entries, "Data Set");
        dataSet.setColor(Color.parseColor("#FF9800")); // Orange bars
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Create BarData and set it to the chart
        BarData barData = new BarData(dataSet);
        holder.barChart.setData(barData);

        // Format axes
        XAxis xAxis = holder.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // Aligns labels
        xAxis.setValueFormatter(new IndexAxisValueFormatter(chartLabelsList.get(position)));

        YAxis leftAxis = holder.barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        holder.barChart.getAxisRight().setEnabled(false);

        // Additional chart styling
        holder.barChart.getDescription().setEnabled(false);
        holder.barChart.setFitBars(true);
        holder.barChart.animateY(800);

        holder.barChart.invalidate(); // Refresh chart
    }

    @Override
    public int getItemCount() {
        return tableTitles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tableTitleTV;
        //ImageView dataVisual;
        BarChart barChart; // Brian added: Replaces imageView


        public MyViewHolder(View itemView) {
            super(itemView);
            tableTitleTV=itemView.findViewById(R.id.tableTitleTV);
            barChart = itemView.findViewById(R.id.barChart);
        }
    }


}


