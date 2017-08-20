package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.fsm_app.ui.mode.ModeActivity;

public class SecondaryModeActivity extends BaseMvpActivity<SecondaryModeView, SecondaryModePresenter> implements SecondaryModeView {

    @Inject
    protected SecondaryModePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_secondary_mode);
        setTitle(getString(R.string.secondary_mode));
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
    public void showHistoric() {
        showToast("Historico");
    }

}
