package com.example.alarm.data.repository

import com.example.alarm.domain.entity.Photo
import com.example.alarm.domain.repository.PhotoRepository
import com.example.alarm.data.database.dao.PhotoDao
import com.example.alarm.data.repository.mapper.PhotoDbToDomainMapper
import com.example.alarm.data.repository.mapper.PhotoDomainToDbMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao,
    private val domainToDbMapper: PhotoDomainToDbMapper,
    private val dbToDomainMapper: PhotoDbToDomainMapper
) : PhotoRepository {

    override suspend fun savePhoto(photo: Photo): Long {
        return photoDao.insert(domainToDbMapper.map(photo))
    }

    override suspend fun getPhoto(id: String): Photo? {
        val entity = photoDao.getByExternalId(id) ?: return null
        return dbToDomainMapper.map(entity)
    }

    override suspend fun getPhotos(): List<Photo> {
        return photoDao.getAll().map(dbToDomainMapper::map)
    }

    override fun observePhotos(): Flow<List<Photo>> {
        return photoDao.observeAll().map { list -> list.map(dbToDomainMapper::map) }
    }
}
