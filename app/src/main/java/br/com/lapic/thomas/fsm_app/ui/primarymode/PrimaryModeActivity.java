package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.multicast.MulticastGroup;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.player.Player;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.fsm_app.ui.mode.ModeActivity;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrimaryModeActivity
        extends BaseMvpActivity<PrimaryModeView, PrimaryModePresenter>
        implements PrimaryModeView {

    @BindView(R.id.loading)
    protected RelativeLayout loadingView;

    @BindView(R.id.content)
    protected RelativeLayout contentView;

    @BindView(R.id.error)
    protected RelativeLayout errorView;

    @BindView(R.id.video_view)
    protected VideoView mVideoView;

    @BindView(R.id.image_view)
    protected ImageView mImageView;

    @BindView(R.id.start_button)
    protected Button startButton;

    @Inject
    protected PrimaryModePresenter mPresenter;

    private  final String TAG = this.getClass().getSimpleName();
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ArrayList<Media> mMedias;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_primary_mode);
        setTitle(getString(R.string.primary_mode));
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public PrimaryModePresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.primary_mode_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leave_primary_mode:
                mPresenter.onLeavePrimaryMode();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.material_green_700));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.material_green_a200)));
    }

    @Override
    public void callModeActivity() {
        startActivity(new Intent(this, ModeActivity.class));
        finish();
    }

    @Override
    public void showLoading(int resIdMessage) {
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        TextView textView = ButterKnife.findById(loadingView, R.id.tv_message);
        textView.setText(resIdMessage);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                mVideoView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.VISIBLE);
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
    public void setListMedias(ArrayList<Media> medias) {
        this.mMedias = medias;
    }

    @Override
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else
                presenter.onPermissionsOk(this);
        }
    }

    @Override
    public void callPlayer() {
        Intent intent = new Intent(this, Player.class);
        intent.putParcelableArrayListExtra(AppConstants.MEDIAS_PARCEL, mMedias);
        startActivity(intent);
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.onPermissionsOk(this);
                } else {
                    presenter.onError(R.string.read_permission_necessary);
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.start_button)
    public void onClickStart(View view) {
        presenter.onClickStart();
    }

}
