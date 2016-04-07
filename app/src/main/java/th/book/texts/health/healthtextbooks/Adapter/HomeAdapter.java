package th.book.texts.health.healthtextbooks.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.w3c.dom.Text;

import java.util.List;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.ReciveDetail;
import th.book.texts.health.healthtextbooks.model.Refrigerator;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class HomeAdapter extends BaseAdapter {

    private List<ReciveDetail> listRecive;
    private Context context;
    AQuery aq;
    final String PATH = "http://www.jaa-ikuzo.com/htb/";

    public HomeAdapter(List<ReciveDetail> listRecive, Context context) {
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

        viewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_home, null);

            holder = new viewHolder();
            holder.name_recipe = (TextView) convertView.findViewById(R.id.name_recipe);
            holder.img_content = (ImageView) convertView.findViewById(R.id.img_content);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.img_profile = (ImageView) convertView.findViewById(R.id.img_profile);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        holder.name_recipe.setText(listRecive.get(position).getRecipeName());
        holder.date.setText(listRecive.get(position).getRecipeDate());
        aq.id(holder.img_profile).progress(R.id.progress).image("https://graph.facebook.com/" + listRecive.get(position).getPersonId() + "/picture?width=200&height=200");
        final viewHolder finalHolder = holder;
        aq.ajax(PATH + listRecive.get(position).getImg(), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {
                super.callback(url, object, status);
                //You will get Bitmap from object.
                Drawable d = new BitmapDrawable(context.getResources(), object);
                finalHolder.img_content.setBackground(d);
            }
        });
        holder.name.setText(listRecive.get(position).getPersonName());
        return convertView;
    }

    public class viewHolder {

        TextView name_recipe;
        ImageView img_content;
        TextView date;
        TextView name;
        ImageView img_profile;
    }

}
