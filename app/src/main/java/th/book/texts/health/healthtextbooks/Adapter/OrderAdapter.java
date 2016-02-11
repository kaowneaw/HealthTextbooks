package th.book.texts.health.healthtextbooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.Refrigerator;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class OrderAdapter extends BaseAdapter {

    private List<Matirial> listMat;
    private Context context;
    AQuery aq;
    final String PATH = "http://www.jaa-ikuzo.com/htb/img/mat/";

    public OrderAdapter(List<Matirial> listMat, Context context) {
        this.listMat = listMat;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listMat.size();
    }

    @Override
    public Object getItem(int position) {
        return listMat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_mat, null);

            holder = new viewHolder();
            holder.matName = (TextView) convertView.findViewById(R.id.matName);
            holder.imgMat = (ImageView) convertView.findViewById(R.id.imgMat);
            holder.matAmount = (EditText) convertView.findViewById(R.id.matAmount);
            holder.matPrice = (TextView) convertView.findViewById(R.id.matPrice);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        aq = new AQuery(convertView);
        holder.matName.setText(listMat.get(position).getMatName());
        holder.matPrice.setText("ราคาต่อหน่วย " + listMat.get(position).getPrice() + " บาท");
        aq.id(holder.imgMat).progress(R.id.progress).image(PATH + listMat.get(position).getImg());


        return convertView;
    }

    public class viewHolder {
        TextView matName;
        ImageView imgMat;
        TextView matPrice;
        EditText matAmount;
    }

}
