package com.java.ChenYuanYong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EntityDetailsActivity extends AppCompatActivity {

    private class DescriptionHolder {
        private String label;
        private String text;
        private String imgUrl;
    }

    private class PropertyHolder {
        private String property_type;
        private String property_description;
    }

    private class RelationHolder {
        private String relation;
        private String label;
        private Boolean forward;
    }

    DescriptionHolder descriptionHolder;
    private ArrayList<PropertyHolder> propertyList;
    private ArrayList<RelationHolder> relationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);

        Intent intent = getIntent();
        String[] description = intent.getStringArrayExtra("Description");
        final String[] properties = intent.getStringArrayExtra("Property");
        String[] relations = intent.getStringArrayExtra("Relation");

        initDescription(description);
        initRelation(relations);
        initProperty(properties);

        TextView tv_label = findViewById(R.id.entity_label);
        TextView tv_text = findViewById(R.id.entity_item_description);
        EntityImageView img_view = findViewById(R.id.entity_img);

        tv_label.setText(descriptionHolder.label);
        tv_text.setText(descriptionHolder.text);
        img_view.getImage(descriptionHolder.imgUrl);

        if (descriptionHolder.text.equals("") && descriptionHolder.imgUrl.equals("")){
            LinearLayout ll = findViewById(R.id.entity_description_ll);
            ll.setVisibility(View.GONE);
            tv_text.setVisibility(View.GONE);
            img_view.setVisibility(View.GONE);
        } else if (!descriptionHolder.imgUrl.equals("")){
          img_view.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        }

        if (!relationList.isEmpty()) {
            ExpandableListView relation_list_view = findViewById(R.id.entity_relation_list_view);
            relation_list_view.setAdapter(new BaseExpandableListAdapter() {
                @Override
                public int getGroupCount() {
                    return 1;
                }

                @Override
                public int getChildrenCount(int i) {
                    return relationList.size();
                }

                @Override
                public Object getGroup(int i) {
                    return relationList;
                }

                @Override
                public Object getChild(int i, int i1) {
                    return relationList.get(i1);
                }

                @Override
                public long getGroupId(int i) {
                    return i;
                }

                @Override
                public long getChildId(int i, int i1) {
                    return i1;
                }

                @Override
                public boolean hasStableIds() {
                    return false;
                }

                @Override
                public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_relation_preface, null);

                    return view;
                }

                @Override
                public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_relation_item, null);

                    TextView tv_relation = view.findViewById(R.id.relation_label);
                    TextView tv_label = view.findViewById(R.id.relation_obj_name);
                    ImageView img_arrow = view.findViewById(R.id.relation_arrow);

                    tv_relation.setText(relationList.get(i1).relation);
                    tv_label.setText(relationList.get(i1).label);
                    if (relationList.get(i1).forward) { img_arrow.setImageResource(R.drawable.rightwise_arrow); }
                    else { img_arrow.setImageResource(R.drawable.leftwise_arrow); }

                    return view;
                }

                @Override
                public boolean isChildSelectable(int i, int i1) {
                    return false;
                }
            });
        }

        if (!propertyList.isEmpty()) {
            ExpandableListView property_list_view = findViewById(R.id.entity_property_list_view);
            property_list_view.setAdapter(new BaseExpandableListAdapter() {
                @Override
                public int getGroupCount() {
                    return 1;
                }

                @Override
                public int getChildrenCount(int i) {
                    return propertyList.size();
                }

                @Override
                public Object getGroup(int i) {
                    return propertyList;
                }

                @Override
                public Object getChild(int i, int i1) {
                    return propertyList.get(i1);
                }

                @Override
                public long getGroupId(int i) {
                    return i;
                }

                @Override
                public long getChildId(int i, int i1) {
                    return i1;
                }

                @Override
                public boolean hasStableIds() {
                    return false;
                }

                @Override
                public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_relation_preface, null);
                    TextView tv_preface = view.findViewById(R.id.preface);
                    tv_preface.setText(R.string.attribute);

                    return view;
                }

                @Override
                public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_entity_property, null);
                    TextView tv_type = view.findViewById(R.id.property_type);
                    TextView tv_text = view.findViewById(R.id.property_text);

                    tv_type.setText(propertyList.get(i1).property_type);
                    tv_text.setText(propertyList.get(i1).property_description);

                    return view;
                }

                @Override
                public boolean isChildSelectable(int i, int i1) {
                    return false;
                }
            });
        }
    }

    private void initDescription(String[] desc) {
        descriptionHolder = new DescriptionHolder();
        descriptionHolder.label = desc[0];
        descriptionHolder.text = desc[1];
        descriptionHolder.imgUrl = desc[2];
    }

    private void initRelation(String[] rela) {
        relationList = new ArrayList<>();
        if (rela[0].equals("None")) return;

        for (int i = 0; i < rela.length; i += 3) {
            RelationHolder relationHolder = new RelationHolder();
            relationHolder.relation = rela[i];
            relationHolder.label = rela[i + 1];
            relationHolder.forward = new Boolean(rela[i + 2]);

            relationList.add(relationHolder);
        }
    }

    private void initProperty(String[] prop) {
        propertyList = new ArrayList<>();
        if (prop[0].equals("None")) return;

        for (int i = 0; i < prop.length; i += 2) {
            PropertyHolder propertyHolder = new PropertyHolder();
            propertyHolder.property_type = prop[i];
            propertyHolder.property_description = prop[i + 1];

            propertyList.add(propertyHolder);
        }
    }

}