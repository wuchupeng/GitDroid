package com.example.gitdroid.github.network;

import com.example.gitdroid.github.hotrepo.repolist.modle.RepoResult;
import com.example.gitdroid.github.hotuser.HotUserResult;
import com.example.gitdroid.github.login.modle.AccessTokenResult;
import com.example.gitdroid.github.login.modle.User;
import com.example.gitdroid.github.repoinfo.RepoContentResult;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 93432 on 2016/7/28.
 */
public class GitHubClient implements GitHubApi{

    private GitHubApi gitHubApi;

    private static GitHubClient gitHubClient;

    public static GitHubClient getInstance(){
        if (gitHubClient == null){
            gitHubClient = new GitHubClient();
        }
        return gitHubClient;
    }

    private GitHubClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //添加token拦截器
                .addInterceptor(new TokenInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                //Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //构建API
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    @Override
    public Call<AccessTokenResult> getOAuthToken(@Field("client_id") String client, @Field("client_secret") String clientSecret, @Field("code") String code) {
        return gitHubApi.getOAuthToken(client,clientSecret,code);
    }

    @Override
    public Call<User> getUserInfo() {
        return gitHubApi.getUserInfo();
    }

    @Override
    public Call<RepoResult> searchRepo(@Query("q") String query, @Query("page") int pageId) {
        return gitHubApi.searchRepo(query,pageId);
    }

    @Override
    public Call<RepoContentResult> getReadme(@Path("owner") String owner, @Path("repo") String repo) {
        return gitHubApi.getReadme(owner, repo);
    }

    @Override
    public Call<ResponseBody> markDown(@Body RequestBody body) {
        return gitHubApi.markDown(body);
    }

    @Override
    public Call<HotUserResult> searchUsers(@Query("q") String query, @Query("page") int pageId) {
        return gitHubApi.searchUsers(query,pageId);
    }

    @Override
    public Call<User> getUser(@Path("login") String login) {
        return gitHubApi.getUser(login);
    }
}
