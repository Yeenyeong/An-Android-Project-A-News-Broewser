package com.java.ChenYuanYong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticContentAdapter extends BaseExpandableListAdapter {
    private List<String> regions;
    private Map<String,RegionData> countryData;
    private Context context;

    public StatisticContentAdapter() {}

    public StatisticContentAdapter(List<String> regions, Map<String,RegionData> countryData, Context context) {
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
        ChildHold childHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.statistic_expand_child, null);
            childHold = new ChildHold();
            childHold.barChart = convertView.findViewById(R.id.statistic_barChart);
            childHold.lineChart = convertView.findViewById(R.id.statistic_lineChart);
            convertView.setTag(childHold);
        } else {
            childHold = (ChildHold) convertView.getTag();
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
        TextView  region, region_confirmed, region_dead, region_cured;
    }

    class ChildHold {
        BarChart barChart;
        LineChart lineChart;
    }
}