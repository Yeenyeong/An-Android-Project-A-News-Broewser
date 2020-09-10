package com.java.ChenYuanYong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticContentAdapter extends BaseExpandableListAdapter {
    private List<String> regions;
    private Map<String, RegionData> countryData;
    private Context context;

    public StatisticContentAdapter() {
    }

    public StatisticContentAdapter(List<String> regions, Map<String, RegionData> countryData, Context context) {
        this.regions = regions;
        this.countryData = countryData;
        this.context = context;
    }

    @Override
    public int getGroupCount() {    //组的数量
        return regions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {    //子项数量
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {     //某组
        return regions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {  //某子项
        return countryData.get(regions.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.statistic_expand_group, null);
            groupHold = new GroupHold();
            groupHold.region = convertView.findViewById(R.id.region);
            groupHold.region_confirmed = convertView.findViewById(R.id.region_confirmed);
            groupHold.region_dead = convertView.findViewById(R.id.region_dead);
            groupHold.region_cured = convertView.findViewById(R.id.region_cured);

            convertView.setTag(groupHold);

        } else {
            groupHold = (GroupHold) convertView.getTag();
        }

        String region = regions.get(groupPosition);
        groupHold.region.setText(region);
        RegionData regionData = countryData.get(region);
        RegionData.DailyData latestData = regionData.getLatestData();
        groupHold.region_confirmed.setText(String.valueOf(latestData.confirmed));
        groupHold.region_cured.setText(String.valueOf(latestData.cured));
        groupHold.region_dead.setText(String.valueOf(latestData.dead));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        final ChildHold childHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.statistic_expand_child, null);
            childHold = new ChildHold();
            childHold.lineChart = convertView.findViewById(R.id.statistic_lineChart);
            convertView.setTag(childHold);
        } else {
            childHold = (ChildHold) convertView.getTag();
        }

        List<RegionData.DailyData> dataList = countryData.get(regions.get(groupPosition)).getData();


        // Line Chart
        {
//            childHold.lineChart.setBackgroundColor(Color.WHITE);

            // disable description text
            childHold.lineChart.getDescription().setEnabled(true);
            childHold.lineChart.getDescription().setPosition(160, 20);
            childHold.lineChart.getDescription().setText("近10日数据");

            // enable touch gestures
            childHold.lineChart.setTouchEnabled(true);

            // set listeners
            childHold.lineChart.setDrawGridBackground(false);

            // enable scaling and dragging
            childHold.lineChart.setDragEnabled(true);
            childHold.lineChart.setScaleEnabled(true);

            // force pinch zoom along both axis
            childHold.lineChart.setPinchZoom(true);

            XAxis xAxis;
            {   // // X-Axis Style // //
                xAxis = childHold.lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                // vertical grid lines
                xAxis.enableGridDashedLine(10f, 10f, 0f);
            }

            YAxis yAxis;
            {   // // Y-Axis Style // //
                yAxis = childHold.lineChart.getAxisLeft();

                // disable dual axis (only use LEFT axis)
                childHold.lineChart.getAxisRight().setEnabled(false);

                // horizontal grid lines
                yAxis.enableGridDashedLine(10f, 10f, 0f);

                // axis range

                int minimum = 2147483647, maximum = -1;
                for (RegionData.DailyData d : dataList) {
                    minimum = Math.min(minimum, d.confirmed);
                    maximum = Math.max(maximum, d.confirmed);
                }
                int diff = maximum - minimum;
                yAxis.setAxisMaximum((float) (maximum + diff * 0.2));
                yAxis.setAxisMinimum((float) (minimum - diff * 0.2));
            }

            // draw points over time
            childHold.lineChart.animateX(500);

            // get the legend (only possible after setting data)
            Legend lineChartLegend = childHold.lineChart.getLegend();

            // draw legend entries as lines
            lineChartLegend.setForm(Legend.LegendForm.LINE);
            ArrayList<Entry> val1 = new ArrayList<>();

            LineDataSet set1;

            // Confirmed Data
            {
                for (int i = 0; i < dataList.size(); i++) {
                    int val = dataList.get(i).confirmed;
                    val1.add(new Entry(i + 1, val));
                }

                if (childHold.lineChart.getData() != null &&
                        childHold.lineChart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet) childHold.lineChart.getData().getDataSetByIndex(0);
                    set1.setValues(val1);
                    set1.notifyDataSetChanged();
                    childHold.lineChart.getData().notifyDataChanged();
                    childHold.lineChart.notifyDataSetChanged();
                } else {
                    // create a dataset and give it a type
                    set1 = new LineDataSet(val1, "累计确诊");

                    set1.setDrawIcons(false);

                    // draw dashed line
                    set1.enableDashedLine(10f, 5f, 0f);

                    // black lines and points
                    set1.setColor(Color.RED);
                    set1.setCircleColor(Color.RED);

                    // line thickness and point size
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);

                    // draw points as solid circles
                    set1.setDrawCircleHole(false);

                    // customize legend entry
                    set1.setFormLineWidth(1f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);

                    // text size of values
                    set1.setValueTextSize(9f);

                    // draw selection line as dashed
                    set1.enableDashedHighlightLine(10f, 5f, 0f);

                    // set the filled area
                    set1.setDrawFilled(true);
                    set1.setFillFormatter(new IFillFormatter() {
                        @Override
                        public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                            return childHold.lineChart.getAxisLeft().getAxisMinimum();
                        }
                    });

                    // set color of filled area
                    set1.setFillColor(Color.RED);
                }
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            childHold.lineChart.setData(data);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;    //默认返回false,改成true表示组中的子条目可以被点击选中
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupHold {
        TextView region, region_confirmed, region_dead, region_cured;
    }

    class ChildHold {
        LineChart lineChart;
    }
}