package com.edmilson.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{

    private static final int TIME_SEPARATOR = 10;

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Get the image Resource ID and set it to the image_news ImageView
        ImageView newsImage = listItemView.findViewById(R.id.news_image);
        newsImage.setImageBitmap(currentNews.getBitmap());

        // Get the section String and set it to the section_text_view
        TextView sectionTextView = listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentNews.getSection());

        // Get the date String and set it to the dateTextView
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        if (currentNews.hasPublishedDate()){
            // Take only the first 10 characters from the publicationDate String
            dateTextView.setText(currentNews.getPublicationDate().substring(0, TIME_SEPARATOR));
            //Make sure the view is visible
            dateTextView.setVisibility(View.VISIBLE);
        }else {
            //Otherwise hide the author TextView
            dateTextView.setVisibility(View.GONE);
        }

        // Get the title String and set it to the title_text_view
        TextView titleTextView = listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getTitle());

        // Get the author String and set it to the author_text_view
        TextView authorTextView = listItemView.findViewById(R.id.author_text_view);
        if (currentNews.hasAuthorName()){
            authorTextView.setText(currentNews.getAuthor());
            //Make sure the view is visible
            authorTextView.setVisibility(View.VISIBLE);
        }else{
            //Otherwise hide the author TextView
            authorTextView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
