package cn.ry.dialry.demo01.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-4-9.
 */
public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cityinfo> cityinfos;
    private Context context;

    public CityAdapter(Context context, List<Cityinfo> cityinfos) {
        this.context = context;
        this.cityinfos = cityinfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_city, null);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHoler h = (ViewHoler) holder;
        h.textView.setText(cityinfos.get(position).getCity_name());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHoler(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textView);
        }


    }
}
