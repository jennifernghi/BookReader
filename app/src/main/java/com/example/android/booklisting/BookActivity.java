package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    final static String LOG_TAG = BookActivity.class.getSimpleName();
    static final String URL = "https://www.googleapis.com/books/v1/volumes?q=love&maxResults=20";
    private BookAdapter mAdapter = null;
    private ArrayList<Book> books = null;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(mEmptyTextView);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        new BookDownloader().execute(URL);

    }

    private class BookDownloader extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            books = Utils.fetchBookData(urls[0]);

            return books;

        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.GONE);

            mEmptyTextView.setText("No book found!");

            mAdapter.clear();

            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
        }
    }

}
