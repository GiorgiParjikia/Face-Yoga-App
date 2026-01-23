package ru.netology.faceyoga.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.faceyoga.data.media.FirebaseVideoUrlResolver
import ru.netology.faceyoga.data.media.VideoUrlResolver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaModule {

    @Binds
    @Singleton
    abstract fun bindVideoUrlResolver(impl: FirebaseVideoUrlResolver): VideoUrlResolver
}
