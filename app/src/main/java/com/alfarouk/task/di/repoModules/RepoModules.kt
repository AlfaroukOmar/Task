package com.alfarouk.task.di.repoModules

import com.alfarouk.task.data.RepositoryImpl
import com.alfarouk.task.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModules {

    @Binds
    abstract fun providesRepo(repositoryImpl: RepositoryImpl): Repository

}