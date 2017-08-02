package br.com.lapic.thomas.fsm_app.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.injection.component.ActivityComponent;
import br.com.lapic.thomas.fsm_app.ui.base.BaseMvpActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
        extends BaseMvpActivity<MainView, MainPresenter>
        implements MainView {

    @Inject
    protected MainPresenter mPresenter;

    @BindView(R.id.button)
    protected Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @OnClick(R.id.button)
    public void onClickButton(View view){
        Toast.makeText(this, "teste", Toast.LENGTH_SHORT).show();
    }
}
