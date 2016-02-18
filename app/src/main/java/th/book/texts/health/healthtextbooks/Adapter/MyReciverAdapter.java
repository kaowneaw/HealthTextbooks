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
import th.book.texts.health.healthtextbooks.model.ReciveDetail;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class MyReciverAdapter extends BaseAdapter {

    private List<ReciveDetail> listRecive;
    private Context context;
    AQuery aq;
    final String PATH = "http://www.jaa-ikuzo.com/htb/img/mat/";

    public MyReciverAdapter(List<ReciveDetail> listRecive, Context context) {
        this.listRecive = listRecive;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listRecive.size();
    }

    @Override
    public Object getItem(int position) {
        return listRecive.get(position);
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
            convertView = inflater.inflate(R.layout.item_myrecive, null);

            holder = new viewHolder();
            holder.recive_name = (TextView) convertView.findViewById(R.id.recive_name);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        aq = new AQuery(convertView);
        holder.recive_name.setText(listRecive.get(position).getRecipeName());

        return convertView;
    }

    public class viewHolder {
        TextView recive_name;

    }

}
