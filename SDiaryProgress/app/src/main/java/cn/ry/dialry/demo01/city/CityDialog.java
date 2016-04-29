package cn.ry.dialry.demo01.city;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-4-9.
 */
public class CityDialog extends DialogFragment {
    private RecyclerView provinceRV;
    private RecyclerView cityRV;
    private RecyclerView counyRV;

    private List<Cityinfo> provinceList = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> cityMap = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> counyMap = new HashMap<String, List<Cityinfo>>();


    public static CityDialog newInstance(int style) {
        CityDialog dialogFragment = new CityDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("style", style);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int styleNum = getArguments().getInt("style", 0);
        int style = 0;
        switch (styleNum) {
            case 0:
                style = DialogFragment.STYLE_NORMAL;//默认样式
                break;
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;//无标题样式
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;//无边框样式
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;//不可输入，不可获得焦点样式
                break;
        }
        setStyle(style, 0);//设置样式
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_city, null);
        provinceRV = (RecyclerView) view.findViewById(R.id.provinceRV);
        provinceRV.setLayoutManager(new LinearLayoutManager(provinceRV.getContext()));
        cityRV = (RecyclerView) view.findViewById(R.id.cityRV);
        cityRV.setLayoutManager(new LinearLayoutManager(cityRV.getContext()));
        counyRV = (RecyclerView) view.findViewById(R.id.counyRV);
        counyRV.setLayoutManager(new LinearLayoutManager(counyRV.getContext()));
        initData();
        return view;
    }

    private void initData() {
        getaddressinfo();

        CityAdapter provinceAdapter = new CityAdapter(getActivity(), provinceList);
        provinceRV.setAdapter(provinceAdapter);

        CityAdapter cityAdapter = new CityAdapter(getActivity(), cityMap.get(provinceList.get(0)));
        cityRV.setAdapter(cityAdapter);

        CityAdapter counyAdapter = new CityAdapter(getActivity(), counyMap.get(cityMap.get(provinceList.get(0)).get(0)));
        counyRV.setAdapter(counyAdapter);

    }

    // 获取城市信息
    private void getaddressinfo() {
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(getActivity(), "area.json");
        provinceList = parser.getJSONParserResult(area_str, "area0");
        cityMap = parser.getJSONParserResultArray(area_str, "area1");
        counyMap = parser.getJSONParserResultArray(area_str, "area2");
    }
}
