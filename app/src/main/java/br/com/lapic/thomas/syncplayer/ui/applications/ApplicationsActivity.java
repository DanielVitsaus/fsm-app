package br.com.lapic.thomas.syncplayer.ui.applications;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.adapters.CustomAdapter;
import br.com.lapic.thomas.syncplayer.data.model.App;
import br.com.lapic.thomas.syncplayer.events.RecyclerClickListener;
import br.com.lapic.thomas.syncplayer.events.RecyclerTouchListener;
import br.com.lapic.thomas.syncplayer.injection.component.ActivityComponent;
import br.com.lapic.thomas.syncplayer.ui.base.BaseMvpActivity;
import br.com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationsActivity
        extends BaseMvpActivity<ApplicationsView, ApplicationsPresenter>
        implements ApplicationsView {

    @BindView(R.id.loading)
    protected RelativeLayout loadingView;

    @BindView(R.id.content)
    protected RelativeLayout contentView;

    @BindView(R.id.error)
    protected RelativeLayout errorView;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    private CustomAdapter mAdapter;
    private List<App> mListApplications;

    @Inject
    protected ApplicationsPresenter mPresenter;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configBars();
        setContentView(R.layout.activity_applications);
        ButterKnife.bind(this);
        setTitle(getString(R.string.primary_mode));
        initRecyclerView();
        mPresenter.getApplications();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.applications_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.use_local_app:
                callPrimaryModeActivity(null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getStringRes(int resId) {
        return getString(resId);
    }

    @Override
    public void setListApplications(List<App> listApplications) {
        this.mListApplications = listApplications;
        mAdapter.setList(listApplications);
        mAdapter.notifyDataSetChanged();
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
                contentView.setVisibility(View.VISIBLE);
            }
        });
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

    private void initRecyclerView() {
        mListApplications = new ArrayList<>();
        mAdapter = new CustomAdapter(this, mListApplications);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        implementRecyclerViewClickListeners();
    }

    private void implementRecyclerViewClickListeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                callPrimaryModeActivity(mListApplications.get(position));
            }
            @Override
            public void onLongClick(View view, int position) {}
        }));
    }


    private void callPrimaryModeActivity(App app) {
        Intent intent = new Intent(this, PrimaryModeActivity.class);
        intent.putExtra(AppConstants.APP_PARCEL, app);
        startActivity(intent);
    }

}