package th.book.texts.health.healthtextbooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.Refrigerator;

/**
 * Created by KaowNeaw on 1/27/2016.
 */
public class SpinnerMatirialAdapter extends BaseAdapter {

    private List<Matirial> listMat;
    private Context context;

    public SpinnerMatirialAdapter(List<Matirial> listMat, Context context) {
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
        return listMat.get(position).getMatId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_spinner, null);

            holder = new viewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.txtSpinner);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.desc.setText(listMat.get(position).getMatName());

        return convertView;
    }

    public class viewHolder {
        TextView desc;
    }
}
