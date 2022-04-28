package lishui.service.misc.di
//
//import android.content.Context
//import androidx.room.Room
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ApplicationComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.Dispatchers
//import lishui.demo.app.data.DB_FILE_NAME
//import lishui.demo.app.data.local.AppDatabase
//import lishui.service.misc.di.qualifier.DefaultCoroutineDispatcher
//import lishui.service.misc.di.qualifier.IOCoroutineDispatcher
//import lishui.service.misc.di.qualifier.MainCoroutineDispatcher
//import lishui.service.misc.di.qualifier.UnconfinedCoroutineDispatcher
//import javax.inject.Singleton
//
//@Module
//@InstallIn(ApplicationComponent::class)
//object RepositoryModule {
//
//    @Singleton
//    @Provides
//    fun provideDataBase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
//        context.applicationContext,
//        AppDatabase::class.java,
//        DB_FILE_NAME
//    ).build()
//
//    @Singleton
//    @IOCoroutineDispatcher
//    @Provides
//    fun provideIoDispatcher() = Dispatchers.IO
//
//    @Singleton
//    @DefaultCoroutineDispatcher
//    @Provides
//    fun provideDefaultDispatcher() = Dispatchers.Default
//
//    @Singleton
//    @UnconfinedCoroutineDispatcher
//    @Provides
//    fun provideUnconfinedDispatcher() = Dispatchers.Unconfined
//
//    @Singleton
//    @MainCoroutineDispatcher
//    @Provides
//    fun provideMainDispatcher() = Dispatchers.Main
//}