package cn.ry.dialry.demo01.recycleview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 15-12-30.
 */
public class FilterActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private List<String> mDatas1;
    private List<String> mDatas2;
    private HomeAdapter mAdapter1;
    private HomeAdapter mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initData();

        mRecyclerView1 = (RecyclerView) findViewById(R.id.id_recyclerview1);
        mAdapter1 = new HomeAdapter(this, mDatas1);
        mRecyclerView1.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView2 = (RecyclerView) findViewById(R.id.id_recyclerview2);
        mAdapter2 = new HomeAdapter(this, mDatas2);
        mRecyclerView2.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());

        initEvent();
    }

    private void initEvent() {
        mAdapter1.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter1.removeData(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mAdapter2.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter1.addData(mAdapter1.getItemCount());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    protected void initData() {
        mDatas1 = new ArrayList<String>();
        for (int i = 1; i < 10; i++) {
            mDatas1.add("" +  i);
        }

        mDatas2 = new ArrayList<String>();
        for (int i = 10; i < 20; i++) {
            mDatas2.add("" +  i);
        }
    }
}
