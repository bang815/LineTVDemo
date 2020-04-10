package com.bang.linetvdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DramaDetailsActivity extends AppCompatActivity {
    private TextView Name_tv,Rating_tv,Created_at_tv,Total_views_tv;
    private ImageView Thumb_img;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_details);
        Thumb_img =(ImageView) findViewById(R.id.imageThumbDetails);
        Name_tv = (TextView) findViewById(R.id.Name_tv);
        Rating_tv = (TextView)findViewById(R.id.Rating_tv);
        Created_at_tv = (TextView)findViewById(R.id.Created_at_tv);
        Total_views_tv = (TextView)findViewById(R.id.Total_views_tv);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Picasso.with(context).load(intent.getStringExtra("Drama_Thumb")).into(Thumb_img);
        Name_tv.setText(intent.getStringExtra("Drama_Name").trim());
        Rating_tv.setText(intent.getStringExtra("Drama_Rating").trim());
        Created_at_tv.setText(intent.getStringExtra("Drama_Created_at").trim());
        Total_views_tv.setText(intent.getStringExtra("Drama_Total_views").trim());

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DramaDetailsActivity.this,MainActivity.class);
        startActivity(i);
        finish();

    }

    protected void onDestroy() {
        super.onDestroy();

    }

}
