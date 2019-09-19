package com.edmilson.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        android.app.LoaderManager.LoaderCallbacks<List<News>> {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    // News List Adapter
    private NewsAdapter mAdapter;
    private ListView newsListView;
    //URL for earthquake data from the TheGuardian dataset
    private static String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?";
    //private static String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?api-key=165c09db-8ba7-44b9-a899-5a59c5f71c47&show-fields=thumbnail&show-tags=contributor";
    //Constant value for the GUARDIAN API Key
    private static final String GUARDIAN_API_KEY = "165c09db-8ba7-44b9-a899-5a59c5f71c47";
    //Constant value for the News loader ID. We can choose any integer number.
    private static final int NEWS_LOADER_ID = 1;
    //The Current News Section
    private String mCurrentNewsSection;
    // This TextView is shown when the list is empty
    private TextView mEmptyStateTextView;
    // Declaration of the LoaderManager
    LoaderManager mLoaderManager;
    // this ProgressBar is shown when the app is connecting with the internet
    private ProgressBar mLoadingProgressBar;
    // Is true if is the first time the App is loading News, and false otherwise
    private boolean isFirstTimeNewsLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Creating the Navigation Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawerLayout);






        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();







        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        // Create a new adapter which obtains a empty News List as entry
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Find the {@link newsListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_news.xml layout file.
        newsListView = findViewById(R.id.list);

        // Make the {@link ListView} use the {@link mAdapter} we created above, so that the
        // {@link newsListView} will display list items for each {@link News} in the list.
        newsListView.setAdapter(mAdapter);
        // This section will be loaded at app startup
        mCurrentNewsSection = getString(R.string.url_section_name_world);
        mEmptyStateTextView = findViewById(R.id.empty_text_view);
        newsListView.setEmptyView(findViewById(R.id.empty_text_view));

        mLoadingProgressBar = findViewById(R.id.loading_spinner);
        // Load the News to the ListView
        loadNewsToListView();

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link News} object at the given position the user clicked on
                News clickedNews = mAdapter.getItem(position);
                // Create a new intent for the URI visualization of the news
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Convert the URL String to an object URI
                intent.setData(Uri.parse(clickedNews.getNewsUrl()));
                // Send the intent to launch a new activity
                startActivity(intent);
            }
        });
    }

    /**
     * This method Load the News to the ListView
     */
    private void loadNewsToListView() {
        if(isConnectedOrIsConnecting()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            mLoaderManager = getLoaderManager();
            // Initialize the loader
            mLoaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }else{
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mEmptyStateTextView.setText(R.string.no_internet);
            mLoadingProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * This method ReLoad with new URL the News to the ListView
     */
    private void reloadNewsToListView(){
        // Hide the ListView before starts to load new data/show progress bar or network message
        newsListView.setVisibility(View.GONE);
        if(isConnectedOrIsConnecting()){
            // Restart the loader
            mLoaderManager.restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
            mEmptyStateTextView.setVisibility(View.GONE);
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }else{
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mEmptyStateTextView.setText(R.string.no_internet);
            mLoadingProgressBar.setVisibility(View.GONE);
            newsListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method checks is the app is connected or is connecting to the internet
     */
    private boolean isConnectedOrIsConnecting() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * This method is called when a menu item is selected from the Navigation Drawer
     * @param menuItem is the item selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_item_world: {
                mCurrentNewsSection = getString(R.string.url_section_name_world);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_business: {
                mCurrentNewsSection = getString(R.string.url_section_name_business);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_travel: {
                mCurrentNewsSection = getString(R.string.url_section_name_travel);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_technology: {
                mCurrentNewsSection = getString(R.string.url_section_name_technology);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_science: {
                mCurrentNewsSection = getString(R.string.url_section_name_science);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_sport: {
                mCurrentNewsSection = getString(R.string.url_section_name_sport);
                if (mLoaderManager == null){
                    loadNewsToListView();
                }else{
                    reloadNewsToListView();
                }
                break;
            }
            case R.id.nav_item_settings: {
                Toast.makeText(this, "Open Settings", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_item_about: {
                Toast.makeText(this, "Open About", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * When the navDrawer is opened and the user presses the Back Button, only the navDrawer is
     * closed and the activity continue opened
     */
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        String newsQuantity = sharedPrefs.getString(getString(R.string.settings_news_quantity_key),
                getString(R.string.settings_news_quantity_default));

        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", GUARDIAN_API_KEY);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("order-by", "newest"/*orderBy*/);
        uriBuilder.appendQueryParameter("page-size", "20"/*newsQuantity*/);
        //uriBuilder.appendQueryParameter("from-date", );
        //uriBuilder.appendQueryParameter("to-date", );

        // If the App is starting loading News for the first time, the Section parameter won't be appended,
        // so the app will show New from various section.
        if (!isFirstTimeNewsLoad){
            uriBuilder.appendQueryParameter("section", mCurrentNewsSection);
        }else{
            isFirstTimeNewsLoad = false;
        }
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Clean the adapter's data from the early News
        mAdapter.clear();
        // Add the data set to the adapter, if there is a valid {@link News}s list
        // This will update the ListView.
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
        }
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        // ProgressBar que é mostrada quando o conteudo da Internet está sendo carregado
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Reset the Loader, so we can clean all the adapter's data.
        mAdapter.clear();
    }

    //Menu main creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }








}
