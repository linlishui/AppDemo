package lishui.module.gitee.net


/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */
@Deprecated("历史遗留项参考")
interface GiteeService {

    // 请求Token
//    @POST("oauth/token")
//    @FormUrlEncoded
//    suspend fun requestToken(
//        @Field("username") userName: String,
//        @Field("password") password: String,
//        @Field("grant_type") grantType: String = "password",
//        @Field("client_id") clientId: String = GITEE_CLIENT_ID,
//        @Field("client_secret") clientSecret: String = GITEE_CLIENT_SECRET,
//        @Field("scope") scope: String = "projects user_info gists",
//    ): GiteeToken

    // 更新Token
//    @POST("oauth/token")
//    @FormUrlEncoded
//    suspend fun refreshToken(
//        @Field("refresh_token") refreshToken: String,
//        @Field("grant_type") grantType: String = "refresh_token"
//    ): GiteeToken

    // 获取仓库列表， 这里使用Query注解会将请求参数添加到GET请求中
//    @GET("api/v5/user/repos")
//    suspend fun listRepoList(
//        @Query("access_token") token: String,
//        @Query("type") type: String = "all",
//        @Query("sort") sort: String = "updated",
//        @Query("q") keyword: String = "",
//        @Query("page") page: Int = 1,
//    ): List<GiteeUserRepo>

    // 获取仓库目录
//    @GET("/api/v5/repos/{owner}/{repo}/git/trees/{sha}")
//    suspend fun listRepoData(
//        @Path("owner") owner: String,
//        @Path("repo") repo: String,
//        @Path("sha") sha: String,
//        @Query("access_token") token: String,
//        @Query("recursive") recursive: Int = 0
//    ): RepoData

    // 获取文件内容
//    @GET("api/v5/repos/{owner}/{repo}/git/blobs/{sha}")
//    suspend fun requestRepoBlobData(
//        @Path("owner") owner: String,
//        @Path("repo") repo: String,
//        @Path("sha") sha: String,
//        @Query("access_token") token: String
//    ): BlobData
}