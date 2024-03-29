package com.devmasterteam.tasks.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "priority")
class PriorityModel {

    @PrimaryKey
    @ColumnInfo(name="id")
    @SerializedName("Id")
    var id: Int = 0

    @ColumnInfo(name="description")
    @SerializedName("Description")
    var description: String = ""
}