package com.natesoft.nateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle bundle = this.getIntent().getExtras();
        String[] movieDetails = bundle != null ? bundle.getStringArray("com.natesoft.nateapp.MESSAGE") : null;

        ImageView image = this.findViewById(R.id.image);
        TextView title = this.findViewById(R.id.title);
        TextView director = this.findViewById(R.id.director);
        TextView year = this.findViewById(R.id.year);
        TextView description = this.findViewById(R.id.description);

        if (movieDetails != null) {
            title.setText(movieDetails[0]);
            year.setText(movieDetails[1]);
            director.setText(movieDetails[2]);
            description.setText(movieDetails[4]);
            Picasso.get().load(movieDetails[3]).into(image);
        }
    }

}