package com.java.ChenYuanYong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterContentAdapter extends BaseExpandableListAdapter {
    private List<String> labels = new ArrayList<>(); // group标签
    private Map<String, List<Event>> cluster = new HashMap<>(); // child数据
    private Context context;

    public ClusterContentAdapter() {
    }

    public ClusterContentAdapter(List<String> labels, Map<String, List<Event>> cluster, Context context) {
        this.labels = labels;
        this.cluster.putAll(cluster);
        this.context = context;
    }

    public void updateData(List<String> labels, Map<String, List<Event>> cluster){
        this.labels = labels;
        this.cluster.putAll(cluster);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {    //组的数量
        return labels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {    //子项数量
        return cluster.get(labels.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {     //某组
        return labels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {  //某子项
        return cluster.get(labels.get(groupPosition));
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
            convertView = LayoutInflater.from(context).inflate(R.layout.cluster_group, null);
            groupHold = new GroupHold();
            groupHold.groupText = convertView.findViewById(R.id.cluster_group);

            convertView.setTag(groupHold);

        } else {
            groupHold = (GroupHold) convertView.getTag();
        }

        groupHold.groupText.setText(labels.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHold childHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cluster_child, null);
            childHold = new ChildHold();
            childHold.childText = convertView.findViewById(R.id.cluster_child);
            convertView.setTag(childHold);
        } else {
            childHold = (ChildHold) convertView.getTag();
        }

        String title = cluster.get(labels.get(groupPosition)).get(childPosition).title;

        if (title.length()>15)
            title = title.substring(0, 20)+"...";

        childHold.childText.setText(title);

        childHold.childText.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ClusterEventDetails.class);
                intent.putExtra("DATE",cluster.get(labels.get(groupPosition)).get(childPosition).date);
                intent.putExtra("TITLE",cluster.get(labels.get(groupPosition)).get(childPosition).title);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;    //默认返回false,改成true表示组中的子条目可以被点击选中
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupHold {
        TextView groupText;
    }

    class ChildHold {
        TextView childText;
    }
}