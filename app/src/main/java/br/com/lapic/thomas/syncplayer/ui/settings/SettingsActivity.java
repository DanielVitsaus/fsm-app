package br.com.lapic.thomas.syncplayer.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SettingsActivity extends BaseMvpActivity<SettingsView, SettingsPresenter> implements SettingsView{

    @BindView(R.id.switch_png)
    protected Switch switchPng;

    @BindView(R.id.switch_jpeg)
    protected Switch switchJpeg;

    @BindView(R.id.switch_mpeg4)
    protected Switch switchMpeg4;

    @BindView(R.id.switch_3gp)
    protected Switch switch3gp;

    @BindView(R.id.switch_mov)
    protected Switch switchMov;

    @BindView(R.id.switch_mpeg3)
    protected Switch switchMpeg3;

    @BindView(R.id.switch_wav)
    protected Switch switchWav;

    @Inject
    protected SettingsPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.supported_medias);
        initSwitches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSwitches() {
        switchPng.setChecked(presenter.getSupportPng());
        switchJpeg.setChecked(presenter.getSupportJpeg());
        switchMpeg4.setChecked(presenter.getSupportMpeg4());
        switch3gp.setChecked(presenter.getSupport3gp());
        switchMov.setChecked(presenter.getSupportMov());
        switchMpeg3.setChecked(presenter.getSupportMpeg3());
        switchWav.setChecked(presenter.getSupportWav());
    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @OnCheckedChanged(R.id.switch_png)
    public void onChangedSwitchPng(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportPng(isChecked);
    }

    @OnCheckedChanged(R.id.switch_jpeg)
    public void onChangedSwitchJpeg(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportJpeg(isChecked);
    }

    @OnCheckedChanged(R.id.switch_mpeg4)
    public void onChangedSwitchMpeg4(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportMpeg4(isChecked);
    }

    @OnCheckedChanged(R.id.switch_3gp)
    public void onChangedSwitch3gp(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupport3gp(isChecked);
    }

    @OnCheckedChanged(R.id.switch_mov)
    public void onChangedSwitchMov(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportMov(isChecked);
    }

    @OnCheckedChanged(R.id.switch_mpeg3)
    public void onChangedSwitchMpeg3(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportMpeg3(isChecked);
    }

    @OnCheckedChanged(R.id.switch_wav)
    public void onChangedSwitchWav(CompoundButton buttonView, boolean isChecked) {
        presenter.setSupportWav(isChecked);
    }

}
