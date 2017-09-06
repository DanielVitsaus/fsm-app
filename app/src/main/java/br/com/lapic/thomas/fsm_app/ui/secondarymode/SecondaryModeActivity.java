package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.helper.ChatConnection;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.fsm_app.ui.mode.ModeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondaryModeActivity extends BaseMvpActivity<SecondaryModeView, SecondaryModePresenter> implements SecondaryModeView {

    @BindView(R.id.loading)
    protected RelativeLayout loadingView;

    @BindView(R.id.content)
    protected RelativeLayout contentView;

    @BindView(R.id.error)
    protected RelativeLayout errorView;

    @Inject
    protected SecondaryModePresenter mPresenter;

    private final String TAG = this.getClass().getSimpleName();
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_secondary_mode);
        setTitle(getString(R.string.secondary_mode));
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public SecondaryModePresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.secondary_mode_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.historic:
                mPresenter.onShowHistoric();
                break;
            case R.id.leave_secondary_mode:
                mPresenter.onLeaveSecondaryMode();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private void configBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.material_blue_700));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.material_blue_a200)));
    }

    @Override
    public void callModeActivity() {
        startActivity(new Intent(this, ModeActivity.class));
        finish();
    }

    @Override
    public void showLoading(int resIdMessage) {
        loadingView.setVisibility(View.VISIBLE);
        TextView textView = ButterKnife.findById(loadingView, R.id.tv_message);
        textView.setText(resIdMessage);
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showError(int resId) {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        TextView textView = ButterKnife.findById(errorView, R.id.tv_error);
        textView.setText(resId);
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void showHistoric() {
        showToast(R.string.historic_item);
    }

    WifiP2pManager.PeerListListener mPeerListListener;

//    private Handler mUpdateHandler;
//    private ChatConnection mConnection;

    @Override
    public void initWifiP2P(NsdServiceInfo mService) {
//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.e(TAG, "OnSuccess discover");
//            }
//
//            @Override
//            public void onFailure(int reasonCode) {
//                Log.e(TAG, "onFailure discover");
//            }
//        });


    }

    @Override
    public void showDialogChoiceGroup(final int amountGroups) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] items = new String[amountGroups];
                for (int i = 0; i < items.length; i++)
                    items[i] = "Grupo " + Integer.valueOf(i+1);
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondaryModeActivity.this);
                builder.setTitle(R.string.choice_group);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.setGroup(i);
                    }
                });
                builder.create().show();
            }
        });

    }

}
