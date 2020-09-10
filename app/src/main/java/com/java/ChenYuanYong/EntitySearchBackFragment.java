package com.java.ChenYuanYong;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntitySearchBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntitySearchBackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Entity> entity_list;
    private ListViewAdapter listAdapter;

    private LayoutInflater inflater;
    private View aView;


    public EntitySearchBackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntitySearchBackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntitySearchBackFragment newInstance(String param1, String param2) {
        EntitySearchBackFragment fragment = new EntitySearchBackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        entity_list = new ArrayList<>();
        inflater = LayoutInflater.from(getContext());
        listAdapter = new ListViewAdapter();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entity_search_back, container, false);
        aView = view;

        ListView list_view = view.findViewById(R.id.entity_list_view);
        list_view.setAdapter(listAdapter);
        list_view.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), EntityDetailsActivity.class);
                String[] description = {
                        entity_list.get(i).getLabel(),
                        entity_list.get(i).getEnwiki()+entity_list.get(i).getBaidu()+entity_list.get(i).getZhwiki(),
                        entity_list.get(i).getImgUrl()
                };
                intent.putExtra("Description", description);

                try {
                    int len = entity_list.get(i).getRelations().length();
                    //System.out.println(len);
                    if (len > 0) {
                        String[] relations = new String[len * 3];
                        for (int k = 0; k < len; ++ k) {
                            JSONObject obj = entity_list.get(i).getRelations().getJSONObject(k);
                            relations[k * 3] = obj.getString("relation");
                            relations[k * 3 + 1] = obj.getString("label");
                            relations[k * 3 + 2] = new Boolean(obj.getBoolean("forward")).toString();
                        }
                        intent.putExtra("Relation", relations);
                    }
                    else {
                        String[] warning = { "None" };
                        intent.putExtra("Relation", warning);
                    }

                    len = entity_list.get(i).getProperties().length() * 2;
                    //System.out.println("prop="+len);
                    if (len > 0) {
                        String[] properties = new String[len];

                        Iterator<String> keys = entity_list.get(i).getProperties().keys();
                        int k = 0;
                        while (keys.hasNext()) {
                            properties[k] = keys.next();
                            properties[k + 1] = entity_list.get(i).getProperties().getString(properties[k]);
                            k += 2;
                        }

                        intent.putExtra("Property", properties);
                    }
                    else {
                        String[] warning = { "None" };
                        intent.putExtra("Property", warning);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }

                getContext().startActivity(intent);
            }
        });



        return view;
    }

    public void setEntity_list(ArrayList<Entity> ettlist) {
        entity_list.clear();
        entity_list.addAll(ettlist);
        TextView tv_result = aView.findViewById(R.id.entity_search_back_info);
        tv_result.setText("共搜索到"+ entity_list.size() +"个实体");
        listAdapter.notifyDataSetChanged();

    }

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (entity_list == null) ? 0 : entity_list.size();
        }

        @Override
        public Object getItem(int i) {
            return (entity_list == null || entity_list.isEmpty()) ? 0 : entity_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.layout_entity_item, null);

            TextView tv_name = view.findViewById(R.id.entity_item_name);
            tv_name.setText(entity_list.get(i).getLabel());

            return view;
        }
    }

}