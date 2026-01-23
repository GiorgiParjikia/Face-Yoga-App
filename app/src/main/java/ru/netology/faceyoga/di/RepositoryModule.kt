package ru.netology.faceyoga.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.data.repository.ProgramRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProgramRepository(impl: ProgramRepositoryImpl): ProgramRepository
}
