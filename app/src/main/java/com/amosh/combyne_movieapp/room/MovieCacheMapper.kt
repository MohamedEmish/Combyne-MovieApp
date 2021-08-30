package com.amosh.combyne_movieapp.room

import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.combyne_movieapp.util.EntityMapper
import javax.inject.Inject

class MovieCacheMapper
@Inject
constructor() : EntityMapper<MovieCacheEntity, MovieObject> {
    override fun mapFromEntity(entity: MovieCacheEntity): MovieObject {
        return MovieObject(
            id = entity.id,
            objectId = entity.objectId,
            createdAt = entity.createdAt as? String ?: "",
            updatedAt = entity.updatedAt as? String ?: "",
            title = entity.title,
            releaseDate = entity.releaseDate as? String ?: "",
            seasons = entity.seasons,
            aCL = MovieObject.ACL("", null, null, null)
        )
    }

    override fun mapToEntity(domainModel: MovieObject): MovieCacheEntity {
        return MovieCacheEntity(
            id = domainModel.id,
            objectId = domainModel.objectId,
            createdAt = domainModel.createdAt as? String ?: "",
            updatedAt = domainModel.updatedAt as? String ?: "",
            title = domainModel.title,
            releaseDate = domainModel.releaseDate as? String ?: "",
            seasons = domainModel.seasons
        )
    }

    fun mapFromEntityList(entities: List<MovieCacheEntity>): List<MovieObject> {
        return entities.map { mapFromEntity(it) }
    }
}