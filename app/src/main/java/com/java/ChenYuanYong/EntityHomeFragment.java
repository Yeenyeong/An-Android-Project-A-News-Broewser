package com.java.ChenYuanYong;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntityHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_title;
    private TextView tv_text;

    private final String HTML_TEXT =
            "<p>为了对抗新型冠状病毒（COVID-19）,全世界的科研人员、医疗人员、政府工作人员和公众渴望" +
            "获得开放、全面的新冠知识。我们收集整理了现有COVID-19开放知识图谱，并进一步融合了它们，" +
            "构建了一个大规模、结构化新冠知识图谱(COKG-19)。目前，COKG-19包含了505个概念、393个" +
            "属性、26282个实例和32352个知识三元组，覆盖了医疗、健康、物资、防控、科研和人物等。此外，" +
            "COKG-19是一个中英文双语知识图谱。</p> " +
            "<p>COKG-19旨在帮助发布者和科研人员识别和链接文本中的语义知识，并提供更多智能服务和应用。我们" +
                    "将融合更多的开放知识，以不断更新COKG-19。</p>";

    public EntityHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntityHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntityHomeFragment newInstance(String param1, String param2) {
        EntityHomeFragment fragment = new EntityHomeFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entity_home, container, false);

        tv_title = view.findViewById(R.id.entity_home_page_title);
        tv_title.setText(Html.fromHtml(getString(R.string.entityPageTitle), 0));
        tv_text = view.findViewById(R.id.entity_home_page_text);
        tv_text.setText(Html.fromHtml(HTML_TEXT, 0));

        return view;
    }


}