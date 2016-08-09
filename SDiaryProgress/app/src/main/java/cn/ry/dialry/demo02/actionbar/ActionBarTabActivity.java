package cn.ry.dialry.demo02.actionbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-8-9.
 */
public class ActionBarTabActivity extends AppCompatActivity implements
        ActionBar.TabListener{
    private static final String SELECTED_ITEM = "selected_item";

    ViewPager viewPager;
    ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar_tab);
        // 获取ActionBar对象
        actionBar = getSupportActionBar();
        // 获取ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        // 创建一个FragmentPagerAdapter对象，该对象负责为ViewPager提供多个Fragment
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            // 获取第position位置的Fragment
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new DummyFragment();
                Bundle args = new Bundle();
                args.putInt(DummyFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                return fragment;
            }

            // 该方法的返回值i表明该Adapter总共包括多少个Fragment
            @Override
            public int getCount() {
                return 3;
            }

            // 该方法的返回值决定每个Fragment的标题
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "第一页";
                    case 1:
                        return "第二页";
                    case 2:
                        return "第三页";
                }
                return null;
            }
        };
        // 设置ActionBar使用Tab导航方式
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // 遍历pagerAdapter对象所包含的全部Fragment。
        // 每个Fragment对应创建一个Tab标签
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            actionBar
                    .addTab(actionBar.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        // 为ViewPager组件设置FragmentPagerAdapter
        viewPager.setAdapter(pagerAdapter); // ①
        // 为ViewPager组件绑定事件监听器
        viewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    // 当ViewPager显示的Fragment发生改变时激发该方法
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    // 当指定Tab被选中时激发该方法
    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition()); // ②
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(SELECTED_ITEM)) {
            // 选中前面保存的索引对应的Fragment页
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(SELECTED_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 将当前选中的Fragment页的索引保存到Bundle中
        outState.putInt(SELECTED_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }
}
