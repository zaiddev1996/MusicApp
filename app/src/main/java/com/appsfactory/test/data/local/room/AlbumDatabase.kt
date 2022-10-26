package com.appsfactory.test.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appsfactory.test.data.remote.dto.AlbumResultDto

@Database(
    entities = [AlbumResultDto.AlbumDto::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(RoomTypeConverter::class)
abstract class AlbumDatabase : RoomDatabase() {
    abstract val dao: AlbumDao
}