package de.symeda.sormas.app.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.component.AbstractRootTabActivity;

public class SamplesActivity extends AbstractRootTabActivity {

    private SamplesListFilterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.samples_activity_layout);
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.main_menu_samples) + " - " + ConfigProvider.getUser().getUserRole());
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new SamplesListFilterAdapter(getSupportFragmentManager());
        createTabViews(adapter);
        pager.setCurrentItem(currentTab);

        SyncSamplesTask.syncSamples(getSupportFragmentManager());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.samples_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_reload:
                SyncSamplesTask.syncSamples(getSupportFragmentManager());
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
