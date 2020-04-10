package com.bang.linetvdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;



public class ListAdapter extends ArrayAdapter<Drama> {

    ArrayList<Drama> dramas;
    Context context;
    int resource;

    public ListAdapter(Context context , int resource, ArrayList<Drama> dramas){
        super(context ,resource ,dramas);
        this.dramas=dramas;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_layout, null , true);
        }

        Drama drama = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageThumb);
        Picasso.with(context).load(drama.getThumb()).into(imageView);
        TextView txtName =  (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(drama.getName());
        TextView txtRating =  (TextView) convertView.findViewById(R.id.txtRating);
        txtRating.setText(drama.getRating());
        TextView txtDate =  (TextView) convertView.findViewById(R.id.txtDate);
        txtDate.setText(drama.getCreated_at());

        return convertView;
    }
}
