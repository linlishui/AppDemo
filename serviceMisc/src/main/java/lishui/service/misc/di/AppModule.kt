package lishui.service.misc.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ApplicationComponent
//import lishui.service.misc.di.qualifier.GiteeRetrofit
//import lishui.service.misc.di.qualifier.WanAndroidRetrofit
//import lishui.demo.app.net.*
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
//@Module
//@InstallIn(ApplicationComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        // "okhttp.OkHttpClient" for http logging tag
//        return OkHttpClient.Builder()
//            .dns(HttpDns())
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//            .build()
//    }
//
//    @Provides
//    @WanAndroidRetrofit
//    @Singleton
//    fun provideWanRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(WAN_ANDROID_BASE_URI)
//            .client(okHttpClient)
//            .build()
//    }
//
//    @Provides
//    @GiteeRetrofit
//    @Singleton
//    fun provideGiteeRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(GITEE_BASE_URI)
//            .client(okHttpClient)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideWanAndroidService(@WanAndroidRetrofit retrofit: Retrofit): WanAndroidService {
//        return retrofit.create(WanAndroidService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideWanAndroidGiteeService(@GiteeRetrofit retrofit: Retrofit): GiteeService {
//        return retrofit.create(GiteeService::class.java)
//    }
//}