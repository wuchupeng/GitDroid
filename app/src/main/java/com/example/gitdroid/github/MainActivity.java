package com.example.gitdroid.github;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.favorite.FavoriteFragment;
import com.example.gitdroid.gank.GankFragment;
import com.example.gitdroid.github.hotrepo.HotRepoFragment;
import com.example.gitdroid.github.hotuser.HotUserFragment;
import com.example.gitdroid.github.login.LoginActivity;
import com.example.gitdroid.github.login.UserRepo;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 当前应用主页面
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //抽屉（包含内容+侧滑菜单）
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    //侧滑菜单视图
    @BindView(R.id.navigationView)
    NavigationView navigationView;

    //热门仓库Fragment
    private HotRepoFragment hotRepoFragment;
    //热门开发者Fragment
    private HotUserFragment hotUserFragment;
    //我的收藏Fragment
    private FavoriteFragment favoriteFragment;
    //每日干货Fragment
    private GankFragment gankFragment;

    private Button btnLogin;
    private ImageView ivIcon;

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        //设置当前视图（也就是说，更改了当前视图内容，将导致onContentChanged方法触发）
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        //Actionbar处理
        setSupportActionBar(toolbar);
        //设置navigationView的监听器
        navigationView.setNavigationItemSelectedListener(this);
        //构建抽屉的监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        //根据drawerlayout同步其当前状态
        toggle.syncState();
        //设置抽屉监听
        drawerLayout.addDrawerListener(toggle);
        btnLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);
        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
                finish();
            }
        });
        //默认显示的是热门仓库Fragment
        hotRepoFragment = new HotRepoFragment();
        replaceFragment(hotRepoFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //没有授权的话
        if (UserRepo.isEmpty()){
            btnLogin.setText(R.string.login_github);
            return;
        }
        btnLogin.setText(R.string.switch_account);
        //设置title
        getSupportActionBar().setTitle(UserRepo.getUser().getLogin());
        //设置用户头像
        String photoUrl = UserRepo.getUser().getAvatar();
        ImageLoader.getInstance().displayImage(photoUrl, ivIcon);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    //侧滑菜单监听器
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //将默认选中项"手动"设置为false
        if (item.isChecked()){
            item.setChecked(false);
        }
        //根据选择做切换
        switch (item.getItemId()){
            //热门仓库
            case R.id.github_hot_repo:
                if (!hotRepoFragment.isAdded()){
                    replaceFragment(hotRepoFragment);
                }
                break;
            //热门开发者
            case R.id.github_hot_coder:
                if (hotUserFragment == null) hotUserFragment = new HotUserFragment();
                if (!hotUserFragment.isAdded()){
                    replaceFragment(hotUserFragment);
                }
                break;
            case R.id.arsenal_my_repo:
                if (favoriteFragment == null) favoriteFragment = new FavoriteFragment();
                if (!favoriteFragment.isAdded()){
                    replaceFragment(favoriteFragment);
                }
                break;
            case R.id.tips_daily:
                if (gankFragment == null) gankFragment = new GankFragment();
                if (!gankFragment.isAdded()){
                    replaceFragment(gankFragment);
                }
                break;
        }
        //关闭drawerLayout
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        //返回true，代表将该菜单项变为checked状态
        return true;
    }
}
