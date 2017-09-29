package br.com.lapic.thomas.syncplayer.ui.applications;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import butterknife.ButterKnife;

public class ApplicationsActivity
        extends BaseMvpActivity<ApplicationsView, ApplicationsPresenter>
        implements ApplicationsView {

    @Inject
    protected ApplicationsPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_applications);
        ButterKnife.bind(this);
        setTitle(getString(R.string.primary_mode));
    }

    @NonNull
    @Override
    public ApplicationsPresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public String getStringRes(int resId) {
        return getString(resId);
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

}
