package com.opiumfive.coinapp.domain.bd

import android.arch.persistence.room.Room
import android.content.Context

private const val DATABASE_NAME = "betholder"

object DatabaseBuilder {

    fun build(context: Context) = Room
        .databaseBuilder(context.applicationContext, Database::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}