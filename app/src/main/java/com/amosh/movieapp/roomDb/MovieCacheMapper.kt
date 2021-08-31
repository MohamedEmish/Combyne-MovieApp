package com.amosh.movieapp.roomDb

import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.movieapp.models.mappers.EntityMapper
import javax.inject.Inject

class MovieCacheMapper
@Inject
constructor() : EntityMapper<MovieCacheEntity, MovieObject> {
    override fun mapFromEntity(entity: MovieCacheEntity): MovieObject {
        return MovieObject(
            id = entity.id,
            title = entity.title,
            releaseDate = entity.releaseDate as? String ?: "",
            seasons = entity.seasons,
        )
    }

    override fun mapToEntity(domainModel: MovieObject): MovieCacheEntity {
        return MovieCacheEntity(
            id = domainModel.id,
            title = domainModel.title,
            releaseDate = domainModel.releaseDate as? String ?: "",
            seasons = domainModel.seasons
        )
    }

    fun mapFromEntityList(entities: List<MovieCacheEntity>): List<MovieObject> {
        return entities.map { mapFromEntity(it) }
    }
}