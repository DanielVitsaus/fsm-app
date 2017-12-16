package br.com.lapic.thomas.syncplayer.ui.secondarymode;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.player.PlayerFragment;
import br.com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.syncplayer.ui.mode.ModeActivity;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;
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
    private static final int MY_REQUEST_WRITE_PERMISSION = 1;
    private AlertDialog.Builder builder;
    private static final ArrayList<Media> mediasAvaiables = new ArrayList<>();
    private PlayerFragment fragment;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_secondary_mode);
        setTitle(getString(R.string.secondary_mode));
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        fragment = new PlayerFragment();
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
                BottomSheet.Builder bottomSheet = new BottomSheet.Builder(this);
                int restId = R.drawable.ic_link_black_24dp;
                for (int i = 0; i<mediasAvaiables.size(); i++) {
                    Media media = mediasAvaiables.get(i);
                    if (media.getType().equals(AppConstants.IMAGE))
                        restId = R.drawable.ic_image_black_24dp;
                    else if (media.getType().equals(AppConstants.AUDIO))
                        restId = R.drawable.ic_audiotrack_black_24dp;
                    else if (media.getType().equals(AppConstants.VIDEO))
                        restId = R.drawable.ic_ondemand_video_black_24dp;
                    else if (media.getType().equals(AppConstants.URL))
                        restId = R.drawable.ic_link_black_24dp;
                    bottomSheet.sheet(i, ContextCompat.getDrawable(this, restId), media.getId());
                }
                bottomSheet.listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Media media = mediasAvaiables.get(i);
                        ArrayList<Media> medias = new ArrayList<>();
                        medias.add(media);
                        if (fragment.isVisible())
                            fragment.playMedias(medias);
                    }
                });
                bottomSheet.build();
                bottomSheet.show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        removeAllMedias();
        super.onDestroy();
    }

    @Override
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_PERMISSION);
            } else
                presenter.onPermissionsOk(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_WRITE_PERMISSION: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.onPermissionsOk(this);
                } else {
                    presenter.onError(R.string.write_permission_necessary);
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    public void showDialogChoiceGroup(final int amountGroups, final String[] classesDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (builder == null) {
                    String[] items = new String[amountGroups];
                    for (int i = 0; i < items.length; i++)
                        items[i] = "Grupo " + Integer.valueOf(i + 1);
                    builder = new AlertDialog.Builder(SecondaryModeActivity.this);
                    builder.setTitle(R.string.choice_group);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.setGroup(i+1, Integer.parseInt(classesDevice[i]));
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                }
            }
        });
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void startFragmentPlayer(Bundle bundle) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void startDownloadMedias() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = ButterKnife.findById(loadingView, R.id.progress_bar);
                TextView textView = ButterKnife.findById(loadingView, R.id.tv_message);
                textView.setText(R.string.downloading_medias);
                progressBar.setIndeterminate(false);
                progressBar.incrementProgressBy(0);
            }
        });
    }

    @Override
    public void incrementProgressBar(final int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = ButterKnife.findById(loadingView, R.id.progress_bar);
                TextView textView = ButterKnife.findById(loadingView, R.id.tv_message);
                textView.setText(R.string.downloading_medias);
                progressBar.incrementProgressBy(value);
            }
        });
    }

    public static void addMedia(Media media) {
        mediasAvaiables.add(media);
    }

    public static void removeAllMedias() {
        mediasAvaiables.clear();
    }

}
