package th.book.texts.health.healthtextbooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Refrigerator;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class RefrigeratorAdapter extends BaseAdapter {

    private List<Refrigerator> listRefri;
    private Context context;
    AQuery aq;
    final String PATH = "http://www.jaa-ikuzo.com/htb/img/mat/";

    public RefrigeratorAdapter(List<Refrigerator> listRefri, Context context) {
        this.listRefri = listRefri;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listRefri.size();
    }

    @Override
    public Object getItem(int position) {
        return listRefri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_myrefrag, null);

            holder = new viewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.expireIn = (TextView) convertView.findViewById(R.id.expireIn);
            holder.imgMat = (ImageView) convertView.findViewById(R.id.imgMat);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        holder.desc.setText(listRefri.get(position).getMatName());
        holder.amount.setText(listRefri.get(position).getAmount() + "");
        holder.expireIn.setText("เหลือเวลาอีก "+listRefri.get(position).getExpireIn()+" วันก่อนหมดอายุ");
        aq.id( holder.imgMat).progress(R.id.progress).image(PATH + listRefri.get(position).getImg());
        return convertView;
    }

    public class viewHolder {
        TextView desc;
        TextView amount;
        TextView expireIn;
        ImageView imgMat;
    }

}
