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

    fun get(id: Int): GuestModel? {
        val tableName = DataBaseConstants.GUEST.TABLE_NAME
        val columnId = DataBaseConstants.GUEST.COLUMNS.ID
        val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
        val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE
        var guest : GuestModel? = null
        try {
            val db = guestDataBase.readableDatabase
            val columns = arrayOf(
                columnId,
                columnName,
                columnPresence
            )
            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())
            val cursor = db.query(
                tableName, columns,
                selection, args,
                null, null, null
            )
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    guest = GuestModel(id, name, presence == 1)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return guest
        }
        return guest
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
            val cursor = db.query(
                tableName, columns,
                null, null,
                null, null, null
            )
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

    fun getPresent(): List<GuestModel> {
        val tableName = DataBaseConstants.GUEST.TABLE_NAME
        val columnId = DataBaseConstants.GUEST.COLUMNS.ID
        val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
        val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE
        val guestPresentList = mutableListOf<GuestModel>()
        try {
            val db = guestDataBase.readableDatabase
            val cursor = db.rawQuery(
                "SELECT $columnId, $columnName, $columnPresence " +
                "FROM $tableName WHERE $columnPresence = 1", null
            )
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guestPresent = GuestModel(id, name, presence == 1)
                    guestPresentList.add(guestPresent)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return guestPresentList
        }
        return guestPresentList
    }

    fun getAbsent(): List<GuestModel> {
        val tableName = DataBaseConstants.GUEST.TABLE_NAME
        val columnId = DataBaseConstants.GUEST.COLUMNS.ID
        val columnName = DataBaseConstants.GUEST.COLUMNS.NAME
        val columnPresence = DataBaseConstants.GUEST.COLUMNS.PRESENCE
        val guestAbsentList = mutableListOf<GuestModel>()
        try {
            val db = guestDataBase.readableDatabase
            val cursor = db.rawQuery(
                "SELECT $columnId, $columnName, $columnPresence " +
                        "FROM $tableName WHERE $columnPresence = 0", null
            )
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columnId))
                    val name = cursor.getString(cursor.getColumnIndex(columnName))
                    val presence = cursor.getInt(cursor.getColumnIndex(columnPresence))
                    val guestAbsent = GuestModel(id, name, presence == 1)
                    guestAbsentList.add(guestAbsent)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return guestAbsentList
        }
        return guestAbsentList
    }
}