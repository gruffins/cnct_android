package com.ryanfung.cnct.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.ViewPagerAdapter;
import com.ryanfung.cnct.animation.Listener;
import com.ryanfung.cnct.contracts.MainContract;
import com.ryanfung.cnct.fragments.ConnectionsFragment;
import com.ryanfung.cnct.fragments.MyNetworksFragment;
import com.ryanfung.cnct.fragments.NetworksFragment;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.presenters.MainPresenter;
import com.ryanfung.cnct.rx.BaseCompletableObserver;
import com.ryanfung.cnct.views.UserIconView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

import static com.ryanfung.cnct.adapters.ViewPagerAdapter.ViewPagerItem;

public class MainActivity extends Activity implements MainContract.View {

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.main_fab)
    FloatingActionButton fab;

    @BindView(R.id.main_fab_sub_items)
    ViewGroup subItemsContainerView;

    @BindView(R.id.main_add_connection)
    AppCompatImageButton addConnectionButton;

    @BindView(R.id.main_add_network)
    AppCompatImageButton addNetworkButton;

    UserIconView actionBarUserIconView;
    AppCompatTextView actionBarTitleView;
    AppCompatTextView actionBarSubtitleView;

    private MainContract.Presenter presenter;
    private boolean isShowingFabSubMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setPresenter(new MainPresenter(getPreferences(), getApi(), this));

        setupActionBar();
        setupViewPager();
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.loadUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    public void setPresenter(MainContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_sign_out:
                presenter.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onUserLoaded(@NonNull User user) {
        if (getSupportActionBar() != null) {
            actionBarUserIconView.setInitials(user.getInitials());
            actionBarTitleView.setText(user.username);
            actionBarSubtitleView.setText(user.email);
        }
    }

    @Override
    public void onSignedOut() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // =============================================================================================
    // Buttons
    // =============================================================================================

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);

        Bundle bundle = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, actionBarUserIconView, "edit_profile")
                .toBundle();

        startActivity(intent, bundle);
    }

    @OnClick(R.id.main_fab)
    public void fab() {
        fab.setEnabled(false);

        if (isShowingFabSubMenu) {
            isShowingFabSubMenu = false;

            addDisposable(new RotateAnimation(fab, 0)
                    .andThen(new FabSubMenuItemAnimation(addConnectionButton, true))
                    .andThen(new FabSubMenuItemAnimation(addNetworkButton, true))
                    .subscribeWith(new BaseCompletableObserver() {
                        @Override
                        public void onComplete() {
                            fab.setEnabled(true);
                            subItemsContainerView.setVisibility(View.GONE);
                        }
                    }));
        } else {
            isShowingFabSubMenu = true;
            subItemsContainerView.setVisibility(View.VISIBLE);

            addDisposable(new RotateAnimation(fab, 45)
                    .andThen(new FabSubMenuItemAnimation(addNetworkButton, false))
                    .andThen(new FabSubMenuItemAnimation(addConnectionButton, false))
                    .subscribeWith(new BaseCompletableObserver() {
                        @Override
                        public void onComplete() {
                            fab.setEnabled(true);
                        }
                    }));
        }
    }

    @OnClick(R.id.main_add_network)
    public void addNetwork() {
        fab();
        startActivity(new Intent(this, AddNetworkActivity.class));
    }

    @OnClick(R.id.main_add_connection)
    public void addConnection() {
        fab();
    }

    // =============================================================================================
    // Setup
    // =============================================================================================

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            View view = getLayoutInflater().inflate(R.layout.action_bar_main, null);

            actionBarUserIconView = view.findViewById(R.id.action_bar_user_icon);
            actionBarTitleView = view.findViewById(R.id.action_bar_title);
            actionBarSubtitleView = view.findViewById(R.id.action_bar_subtitle);

            getSupportActionBar().setCustomView(view);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                Arrays.asList(
                        new ViewPagerItem(NetworksFragment.newInstance(), getString(R.string.main_networks)),
                        new ViewPagerItem(ConnectionsFragment.newInstance(), getString(R.string.main_connections)),
                        new ViewPagerItem(MyNetworksFragment.newInstance(), getString(R.string.main_my_networks))
                ));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        tabLayout.setupWithViewPager(viewPager);
    }

    // =============================================================================================
    // Animations
    // =============================================================================================

    private static class RotateAnimation extends Completable {

        private View view;
        private float rotation;

        RotateAnimation(@NonNull View view, float rotation) {
            this.view = view;
            this.rotation = rotation;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            ViewPropertyAnimator animator = view.animate();
            animator.rotation(rotation);
            animator.setDuration(100);
            animator.setListener(new Listener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    observer.onComplete();
                }
            });
            animator.start();
        }
    }

    private static class FabSubMenuItemAnimation extends Completable {

        private View view;
        private boolean reverse;

        FabSubMenuItemAnimation(@NonNull View view, boolean reverse) {
            this.view = view;
            this.reverse = reverse;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            AnimatorSet set = new AnimatorSet();
            set.setDuration(50);

            if (reverse) {
                set.setInterpolator(new AccelerateInterpolator());
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "alpha", 1, 0),
                        ObjectAnimator.ofFloat(view, "translationY", 0, 50)
                );
            } else {
                set.setInterpolator(new DecelerateInterpolator());
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1),
                        ObjectAnimator.ofFloat(view, "translationY", 50, 0)
                );
            }

            set.addListener(new Listener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    observer.onComplete();
                }
            });
            set.start();
        }
    }

}
