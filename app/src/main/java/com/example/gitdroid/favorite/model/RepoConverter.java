package com.example.gitdroid.favorite.model;

import android.support.annotation.NonNull;

import com.example.gitdroid.github.hotrepo.repolist.modle.Repo;

import java.util.ArrayList;
import java.util.List;

/**
 * 将Repo(热门仓库)转换为LocalRepo(本地仓库)对象，为了实现仓库的收藏功能
 * Created by 93432 on 2016/8/5.
 */
public class RepoConverter {

    private RepoConverter(){};

    /**
     * 将Repo(热门仓库)转换为LocalRepo(本地仓库)对象，默认为未分类
     * @param repo
     * @return
     */
    public static @NonNull LocalRepo convert(@NonNull Repo repo){
        LocalRepo localRepo = new LocalRepo();
        localRepo.setAvatar(repo.getOwner().getAvatar());
        localRepo.setDescription(repo.getDescription());
        localRepo.setFullName(repo.getFullName());
        localRepo.setId(repo.getId());
        localRepo.setName(repo.getName());
        localRepo.setStarsCount(repo.getStarCount());
        localRepo.setForkCount(repo.getForkCount());
        localRepo.setRepoGroup(null);
        return localRepo;
    }

    public static @NonNull List<LocalRepo> converAll(@NonNull List<Repo> repos){
        ArrayList<LocalRepo> localRepos = new ArrayList<>();
        for (Repo repo : repos){
            localRepos.add(convert(repo));
        }
        return localRepos;
    }
}
