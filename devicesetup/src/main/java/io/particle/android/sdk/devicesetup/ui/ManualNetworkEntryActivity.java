package io.particle.android.sdk.devicesetup.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.util.Set;

import io.particle.android.sdk.devicesetup.R;
import io.particle.android.sdk.devicesetup.commands.CommandClient;
import io.particle.android.sdk.devicesetup.commands.InterfaceBindingSocketFactory;
import io.particle.android.sdk.devicesetup.commands.ScanApCommand;
import io.particle.android.sdk.devicesetup.commands.data.WifiSecurity;
import io.particle.android.sdk.devicesetup.loaders.ScanApCommandLoader;
import io.particle.android.sdk.devicesetup.model.ScanAPCommandResult;
import io.particle.android.sdk.ui.BaseActivity;
import io.particle.android.sdk.utils.WiFi;
import io.particle.android.sdk.utils.ui.ParticleUi;
import io.particle.android.sdk.utils.ui.Ui;


public class ManualNetworkEntryActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Set<ScanAPCommandResult>> {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_network_entry);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_password_entry));

        ParticleUi.enableBrandLogoInverseVisibilityAgainstSoftKeyboard(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onConnectClicked(View view) {
        String ssid = Ui.getText(this, R.id.network_name, true);
        ScanApCommand.Scan scan = new ScanApCommand.Scan(ssid, WifiSecurity.WPA2_AES_PSK.asInt(), 0);

        CheckBox requiresPassword = Ui.findView(this, R.id.network_requires_password);
        if (requiresPassword.isChecked()) {
            startActivity(PasswordEntryActivity.buildIntent(this, scan));

        } else {
            String softApSSID = WiFi.getCurrentlyConnectedSSID(this);
            startActivity(ConnectingActivity.buildIntent(this, softApSSID, scan));
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }

    @Override
    public Loader<Set<ScanAPCommandResult>> onCreateLoader(int id, Bundle args) {
        // FIXME: make the address below use resources instead of hardcoding
        CommandClient client = CommandClient.newClientUsingDefaultSocketAddress();
        String softApSSID = WiFi.getCurrentlyConnectedSSID(this);
        InterfaceBindingSocketFactory socketFactory = new InterfaceBindingSocketFactory(this, softApSSID);
        return new ScanApCommandLoader(this, client, socketFactory);
    }

    @Override
    public void onLoadFinished(Loader<Set<ScanAPCommandResult>> loader, Set<ScanAPCommandResult> data) {
        // FIXME: perform process described here:
        // https://github.com/spark/mobile-sdk-ios/issues/56
    }

    @Override
    public void onLoaderReset(Loader<Set<ScanAPCommandResult>> loader) {
        // no-op
    }
}
