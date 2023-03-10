package com.akmalmf24.githubuser.core.source

import com.akmalmf24.githubuser.abstraction.data.Resource
import com.akmalmf24.githubuser.core.GithubServices
import com.akmalmf24.githubuser.core.response.DetailUser
import com.akmalmf24.githubuser.core.response.SearchResponse
import com.akmalmf24.githubuser.core.response.Users
import com.akmalmf24.githubuser.core.source.base.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Akmal Muhamad Firdaus on 04/03/2023 00:58.
 * akmalmf007@gmail.com
 */
class GithubDataSource private constructor(private val apiService: GithubServices) : RemoteDataSource(){
    companion object {
        @Volatile
        private var instance: GithubDataSource? = null

        fun getInstance(service: GithubServices): GithubDataSource =
            instance ?: synchronized(this) {
                instance ?: GithubDataSource(service)
            }
    }

    suspend fun popularUser(): Flow<Resource<List<Users>>> {
        return flow {
            emit(Resource.loading())
            emit(safeApiCall { apiService.users() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchUser(username: String): Flow<Resource<SearchResponse>> {
        return flow {
            emit(Resource.loading())
            emit(safeApiCall { apiService.searchUsers(username) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun detailUser(username: String): Flow<Resource<DetailUser>> {
        return flow {
            emit(Resource.loading())
            emit(safeApiCall { apiService.detailUser(username) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowsUser(username: String, type: String): Flow<Resource<List<Users>>> {
        return flow {
            emit(Resource.loading())
            emit(safeApiCall { apiService.userFollows(username, type) })
        }.flowOn(Dispatchers.IO)
    }
}