package com.razzaghi.electionhelper.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.razzaghi.electionhelper.db.AppDatabase
import com.razzaghi.electionhelper.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideElectionDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()



    @Singleton
    @Provides
    fun provideCommentDAO(db: AppDatabase) = db.commentDAO

    @Singleton
    @Provides
    fun providePresidentDAO(db: AppDatabase) = db.presidentDAO

}