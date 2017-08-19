package br.com.lapic.thomas.fsm_app.ui.mode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import butterknife.ButterKnife;

public class ModeActivity
        extends BaseMvpActivity<ModeView, ModePresenter>
        implements ModeView {

    @Inject
    protected ModePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public ModePresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void onClickButton(View view) {
        String tagButton = view.getTag().toString();
        mPresenter.onClickButton(tagButton);
        Toast.makeText(this, tagButton, Toast.LENGTH_SHORT).show();
    }

}
