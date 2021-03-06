package com.bang.linetvdemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListAdapter extends ArrayAdapter<Drama> implements Filterable{

    private List<Drama> dramas;
    private List<Drama> origDramas;
    private List<Drama> sortDramas;
    private Context context;
    private Filter dramaFilter;

    public ListAdapter(List<Drama> dramas, Context context){
        super(context, R.layout.list_layout, dramas);
        this.dramas=dramas;
        this.context = context;
        this.origDramas = dramas;
        this.sortDramas = new ArrayList<>(dramas);

    }

    public int getCount() {
        return dramas.size();
    }

    public Drama getItem(int position) {
        return dramas.get(position);
    }

    public long getItemId(int position) {
        return dramas.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        DramaHolder holder = new DramaHolder();

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.list_layout, null);

            ImageView imageView = (ImageView) v.findViewById(R.id.imageThumb);
            TextView txtName =  (TextView) v.findViewById(R.id.txtName);
            TextView txtRating =  (TextView) v.findViewById(R.id.txtRating);
            TextView txtDate =  (TextView) v.findViewById(R.id.txtDate);
            holder.thubView = imageView;
            holder.nameView = txtName;
            holder.ratingView =txtRating;
            holder.createView = txtDate;
            v.setTag(holder);

        }else {
            holder = (DramaHolder) v.getTag();
        }
            Drama drama = dramas.get(position);
            Glide.with(context).load(drama.getThumb()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thubView);
            holder.nameView.setText(drama.getName());
            holder.ratingView.setText(drama.getRating());
            holder.createView.setText(drama.getCreated_at());


            return v;

    }

    private static class DramaHolder {
        public ImageView thubView;
        public TextView nameView;
        public TextView ratingView;
        public TextView createView;
    }


    public Filter getFilter() {
        if (dramaFilter == null){
            dramaFilter = new DramaFilter();
        }
        return dramaFilter;
    }

    private class DramaFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase().trim();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length()>0) {
                    search(constraint.toString());
                    List<Drama> filteredItem = new ArrayList<Drama>();
                    int i = 0;
                    while (sortDramas.get(i).getScore() > 0) {
                        filteredItem.add(sortDramas.get(i));
                        i++;
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        result.values = origDramas;
                        result.count = origDramas.size();
                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count == 0) {
                    dramas = (List<Drama>)results.values;
                    notifyDataSetChanged();
                }
                else {
                    dramas = (List<Drama>)results.values;
                    notifyDataSetChanged();
                }
            }
    }

    public void search(String search){
        int score;
        for (Drama index : sortDramas){
            score =0;
            for(int i=0;i<search.length();i++){
                if(index.getName().contains(String.valueOf(search.charAt(i)))){
                    score++;
                }
            }
            index.setScore(score);
        }
        Collections.sort(sortDramas, new Comparator<Drama>() {
            @Override
            public int compare(Drama d, Drama t1) {
                return t1.getScore()-d.getScore();
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }
        });
    }
}
