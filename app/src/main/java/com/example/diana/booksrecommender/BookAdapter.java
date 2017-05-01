package com.example.diana.booksrecommender;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Diana on 4/30/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Book currentBook = getItem(position);

        // Get the current book title
        String currentBookTitle = currentBook.getTitle();
        // Find the TextView with view book_title
        TextView bookTitleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        // Finally set its text
        bookTitleTextView.setText(currentBookTitle);

        // Get the current book author
        String currentBookAuthor = currentBook.getAuthors();
        // Find the TextView with view book_author
        TextView bookAuthorTextView = (TextView) listItemView.findViewById(R.id.book_author);

        bookAuthorTextView.setText(currentBookAuthor);

        String currentBookImgUrl = currentBook.getImgUrl();

        ImageView bookImageView = (ImageView) listItemView.findViewById(R.id.book_img);
        Glide.with(getContext()).load(currentBookImgUrl).into(bookImageView);


        return listItemView;
    }


}
