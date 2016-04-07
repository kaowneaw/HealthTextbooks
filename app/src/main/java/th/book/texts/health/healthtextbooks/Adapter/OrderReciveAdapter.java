package th.book.texts.health.healthtextbooks.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import th.book.texts.health.healthtextbooks.model.Order;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class OrderReciveAdapter extends BaseAdapter {

    private List<Order> listOrder;
    private Context context;

    public OrderReciveAdapter(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return listOrder.get(position);
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
            convertView = inflater.inflate(R.layout.item_order_recive, null);

            holder = new viewHolder();
            holder.recive_date = (TextView) convertView.findViewById(R.id.recive_date);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.recive_date.setText("รายการสั่งซื้อวันที่ " + listOrder.get(position).getOrderDate());
        int status = listOrder.get(position).getOrderStatus();
        holder.status.setText(getStatus(status));
        if (status == 0) {
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setTextColor(Color.GREEN);
        }
        return convertView;
    }

    public class viewHolder {
        TextView recive_date;
        TextView status;
    }

    private String getStatus(int status) {
        if (status == 0) {
            return "รอการดำเนินการ";
        } else {
            return "กำลังดำเนินการ";
        }
    }

}
