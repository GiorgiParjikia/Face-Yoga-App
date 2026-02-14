package ru.netology.faceyoga.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.faceyoga.data.repository.ArticlesRepository
import ru.netology.faceyoga.data.repository.ArticlesRepositoryImpl
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.data.repository.ProgramRepositoryImpl
import ru.netology.faceyoga.data.repository.ProgressRepository
import ru.netology.faceyoga.data.repository.ProgressRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProgramRepository(impl: ProgramRepositoryImpl): ProgramRepository

    @Binds
    @Singleton
    abstract fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository
}
