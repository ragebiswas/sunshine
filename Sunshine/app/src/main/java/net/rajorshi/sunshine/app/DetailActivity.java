package net.rajorshi.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_view) {
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String location = preferences.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));
            Uri uri = Uri.parse("geo:0,0?q=" + location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW);
            mapIntent.setData(uri);
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        private String mForecastStr;
        private static final String HASHTAG = "#SunshineApp";
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);

            /* Set share action provider */
            MenuItem menuItem = menu.findItem(R.id.action_share);
            ShareActionProvider shareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(getShareIntent());
            }
            else {
                Log.e(LOG_TAG, "Share action provider is null!");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.detail_text);
            mForecastStr = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(mForecastStr);
            return rootView;
        }

        private Intent getShareIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + HASHTAG);
            return shareIntent;
        }
    }
}
