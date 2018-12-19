package com.dsp.fir.ui.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.dsp.fir.BuildConfig;
import com.dsp.fir.R;
import com.dsp.fir.data.preference.Preferences;
import com.dsp.fir.global.BaseFragment;
import com.dsp.fir.global.flow.FlowRouter;
import com.dsp.fir.presentation.main.MainPresenter;
import com.dsp.fir.presentation.main.MainView;
import com.dsp.fir.ui.fragment.charts.ChartsFragment;
import com.dsp.fir.ui.fragment.parameters.ParametersFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment
        implements AHBottomNavigation.OnTabSelectedListener, MainView {
    private static final String PARAMETERS_TAB = "PARAMETERS_TAB";
    private static final String CHARTS_TAB = "CHARTS_TAB";

    @Inject FlowRouter router;
    @InjectPresenter MainPresenter presenter;
    @Inject Provider<MainPresenter> presenterProvider;
    @Inject Preferences prefs;

    @BindView(R.id.navigationView) AHBottomNavigation navigation;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navDrawer) NavigationView navDrawer;
    @BindView(R.id.textViewAppVersion) TextView textViewAppVersion;

    private Map<String, Fragment> tabFragments;
    private AHBottomNavigationAdapter navigationAdapter;
    private Integer defaultNavigationItemId;

    public static MainFragment getInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @ProvidePresenter
    MainPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainers();
        initNavigationAdapter();
        if (savedInstanceState == null) {
            defaultNavigationItemId = navigationAdapter.getPositionByMenuId(R.id.actionParameters);
        }
    }

    private void initNavigationAdapter() {
        Preconditions.checkNotNull(getActivity());
        navigationAdapter = new AHBottomNavigationAdapter(getActivity(), R.menu.menu_main_navigation);
    }

    private void initContainers() {
        tabFragments = new HashMap<>();
        FragmentManager fm = getChildFragmentManager();
        tabFragments.put(PARAMETERS_TAB, getFragmentByTagOrAddToManager(fm, PARAMETERS_TAB, ParametersFragment.getInstance()));
        tabFragments.put(CHARTS_TAB, getFragmentByTagOrAddToManager(fm, CHARTS_TAB, ChartsFragment.getInstance()));
    }

    private Fragment getFragmentByTagOrAddToManager(
            FragmentManager fm,
            String tag,
            Fragment defaultFragment) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = defaultFragment;
            fm.beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .detach(fragment)
                    .commitNow();
        }

        return fragment;
    }

    private void openFragmentByTag(String tag) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Stream.of(tabFragments)
                .forEach(x -> {
                    if (x.getKey().equals(tag)) {
                        transaction.attach(x.getValue());
                    } else {
                        transaction.detach(x.getValue());
                    }
                });
        transaction.commitNow();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNavigation();
        if (defaultNavigationItemId != null) {
            navigation.setCurrentItem(defaultNavigationItemId, true);
            defaultNavigationItemId = null;
        }
        initNavigationDrawer();
        textViewAppVersion.setText(String.format(Locale.getDefault(), "%s: %s(%s)",
                getString(R.string.version), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }

    private void initNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                navDrawer.requestFocus();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onDestroyView() {
        navigation.removeOnTabSelectedListener();
        super.onDestroyView();
    }

    private void initNavigation() {
        Preconditions.checkNotNull(getActivity());
        navigationAdapter.setupWithBottomNavigation(navigation);
        navigation.setOnTabSelectedListener(this);
        navigation.setDefaultBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorNavigation));
        navigation.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorText));
        navigation.setInactiveColor(ContextCompat.getColor(getActivity(), R.color.colorText));
        navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        navigation.setUseElevation(false);
        navigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorNavbarNotification));
        navigation.setNotificationMarginLeft(30, 30);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        MenuItem menuItem = navigationAdapter.getMenuItem(position);
        for (int i = 0; i < 2; i++) {
            if (i == position) {
                navigation.setNotification(" ", i);
            } else {
                navigation.setNotification("", i);
            }
        }
        switch (menuItem.getItemId()) {
            case R.id.actionParameters:
                openFragmentByTag(PARAMETERS_TAB);
                break;
            case R.id.actionCharts:
                openFragmentByTag(CHARTS_TAB);
                break;
        }
        return true;
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        showSnackMessage(message);
    }

    @OnClick(R.id.buttonEmail)
    public void onButtonEmailClick() {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", getString(R.string.dev_email), null));
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    @OnClick(R.id.buttonGithub)
    public void onButtonGithibClick() {
        openBrowser(getString(R.string.dev_github));
    }

    @OnClick(R.id.buttonVK)
    public void onButtonVKClick() {
        openBrowser(getString(R.string.dev_vk));
    }

    @OnClick(R.id.button4PDA)
    public void onButton4pdaClick() {
        openBrowser(getString(R.string.dev_4pda));
    }

    @OnClick(R.id.buttonTelegram)
    public void onButtonTelegramClick() {
        openBrowser(getString(R.string.dev_telegram));
    }

    @OnClick(R.id.buttonSmartsworld)
    public void onButtonSmartsworldClick() {
        openBrowser(getString(R.string.dev_smartsworld));
    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
