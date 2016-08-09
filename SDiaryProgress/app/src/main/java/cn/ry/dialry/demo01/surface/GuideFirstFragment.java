package cn.ry.dialry.demo01.surface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-7-8.
 */
public class GuideFirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_surfaceview, null);
        return view;
    }

}
