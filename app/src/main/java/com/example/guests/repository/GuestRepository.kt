package com.example.guests.repository

import android.content.ContentValues
import android.content.Context
import com.example.guests.constants.DataBaseConstants
import com.example.guests.model.GuestModel
import java.lang.Exception

class GuestRepository private constructor(context: Context) {
    private val guestDataBase = GuestDataBase(context)

    // Singleton
    companion object {
        private lateinit var repository: GuestRepository

        fun getInstance(context: Context): GuestRepository {
            if (!Companion::repository.isInitialized) {
                repository = GuestRepository(context)
            }
            return repository
        }
    }

    fun insert(guest: GuestModel): Boolean {
        return try {
            val db = guestDataBase.writableDatabase
            val presence = if (guest.presence) 1 else 0
            val values = ContentValues()
            values.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)
            values.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, presence)
            db.insert(DataBaseConstants.GUEST.TABLE_NAME, null, values)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun update(guest: GuestModel): Boolean {
        return try {
            val db = guestDataBase.writableDatabase
            val presence = if (guest.presence) 1 else 0
            val values = ContentValues()
            values.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)
            values.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, presence)
            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(guest.id.toString())
            db.update(DataBaseConstants.GUEST.TABLE_NAME, values, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun delete(guestId: Int): Boolean {
        return try {
            val db = guestDataBase.writableDatabase
            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(guestId.toString())
            db.delete(DataBaseConstants.GUEST.TABLE_NAME, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAll(): List<GuestModel> {
        val tableName = DataBaseConstants.GUEST.TABLE_NAME
        val columnId = DataBaseConstants.GUEST.COLUMNS.ID
        val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
        val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE
        val guestList = mutableListOf<GuestModel>()
        try {
            val db = guestDataBase.readableDatabase
            val columns = arrayOf(
                columnId,
                columnName,
                columnPresence
            )
            val cursor = db.query(tableName, columns, null, null, null, null, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guest = GuestModel(id, name, presence == 1)
                    guestList.add(guest)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return guestList
        }
        return guestList
    }
}