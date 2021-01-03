package be.ami.maubray.brocantedumarais.brocscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import be.ami.maubray.brocantedumarais.brocscan.models.Emplacement;
import be.ami.maubray.brocantedumarais.brocscan.models.Entry;

public class ClassHandler {
	
	private final DatabaseHandler handler;
	public SQLiteDatabase database;
	
	public ClassHandler(Context context) {
		handler = new DatabaseHandler(context);
	}
	
	/**
	 * Open database
	 */
	public void open() {
		database = handler.getWritableDatabase();
	}
	
	/**
	 * Close database
	 */
	public void close() {
		handler.close();
	}
	
	/**
	 * Insert a new emplacement
	 *
	 * @param emplacement The emplacement to add
	 */
	public void insertEmplacement(Emplacement emplacement) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.EMPLACEMENT_NUMBER, emplacement.getNumber());
		value.put(DatabaseHandler.EMPLACEMENT_CODE, emplacement.getCode());
		value.put(DatabaseHandler.EMPLACEMENT_ENTRY, emplacement.getEntry());
		value.put(DatabaseHandler.EMPLACEMENT_SCAN, emplacement.getScan());
		value.put(DatabaseHandler.EMPLACEMENT_REFUSAL, emplacement.getRefusal());
		database.insert(DatabaseHandler.TABLE_NAME_EMPLACEMENT, null, value);
	}
	
	/**
	 * Select an emplacement by number
	 *
	 * @param number The number of the emplacement to find
	 * @return The Emplacement
	 */
	public Emplacement selectByNumber(String number) {
		String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_NAME_EMPLACEMENT + " WHERE " + DatabaseHandler.EMPLACEMENT_NUMBER + " = ?";
		Cursor cursor = database.rawQuery(selectQuery, new String[]{number});
		Emplacement emplacement = cursorToEmplacement(cursor);
		cursor.close();
		return emplacement;
	}
	
	/**
	 * Select an emplacement by code
	 *
	 * @param code The code of the emplacement to find
	 * @return The Emplacement
	 */
	public Emplacement selectByCode(String code) {
		String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_NAME_EMPLACEMENT + " WHERE " + DatabaseHandler.EMPLACEMENT_CODE + " = ?";
		Cursor cursor = database.rawQuery(selectQuery, new String[]{code});
		Emplacement emplacement = cursorToEmplacement(cursor);
		cursor.close();
		return emplacement;
	}
	
	
	/**
	 * Select all the emplacements
	 *
	 * @return All the emplacements
	 */
	public List<Emplacement> selectAllEmplacement() {
		List<Emplacement> emplacementList = new ArrayList<>();
		Emplacement emplacement;
		Cursor c = database.query(DatabaseHandler.TABLE_NAME_EMPLACEMENT, null, null, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				emplacement = new Emplacement();
				emplacement.setNumber(c.getString(0));
				emplacement.setCode(c.getString(1));
				emplacement.setEntry(c.getString(2));
				emplacement.setScan(c.getString(3));
				emplacement.setRefusal(c.getInt(4));
				emplacementList.add(emplacement);
			} while (c.moveToNext());
		}
		c.close();
		return emplacementList;
	}
	
	/**
	 * Convert a cursor to an emplacement
	 *
	 * @param cursor The cursor to convert
	 * @return The emplacement
	 */
	private Emplacement cursorToEmplacement(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		Emplacement emplacement = new Emplacement();
		emplacement.setNumber(cursor.getString(0));
		emplacement.setCode(cursor.getString(1));
		emplacement.setEntry(cursor.getString(2));
		emplacement.setScan(cursor.getString(3));
		emplacement.setRefusal(cursor.getInt(4));
		return emplacement;
	}
	
	/**
	 * Delete all the emplacements
	 */
	public void deleteAll() {
		database.delete(DatabaseHandler.TABLE_NAME_EMPLACEMENT, null, null);
		database.delete(DatabaseHandler.TABLE_NAME_ENTRY, null, null);
	}
	
	/**
	 * Update an emplacement
	 *
	 * @param emplacement The emplacement to update
	 */
	public void updateEmplacement(Emplacement emplacement) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.EMPLACEMENT_NUMBER, emplacement.getNumber());
		value.put(DatabaseHandler.EMPLACEMENT_CODE, emplacement.getCode());
		value.put(DatabaseHandler.EMPLACEMENT_ENTRY, emplacement.getEntry());
		value.put(DatabaseHandler.EMPLACEMENT_SCAN, emplacement.getScan());
		value.put(DatabaseHandler.EMPLACEMENT_REFUSAL, emplacement.getRefusal());
		database.update(DatabaseHandler.TABLE_NAME_EMPLACEMENT, value, DatabaseHandler.EMPLACEMENT_NUMBER + " = ?", new String[]{emplacement.getNumber()});
	}
	
	
	// Entry concern
	
	/**
	 * Insert an entry
	 *
	 * @param entry The entry to insert
	 */
	public void insertEntry(Entry entry) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.ENTRY_ID, entry.getId());
		value.put(DatabaseHandler.ENTRY_DESCRIPTION, entry.getDescription());
		value.put(DatabaseHandler.ENTRY_VALUE, entry.getValue());
		database.insert(DatabaseHandler.TABLE_NAME_ENTRY, null, value);
	}
	
	/**
	 * Update an entry
	 *
	 * @param entry The entry to edit
	 */
	public void updateEntry(Entry entry) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.ENTRY_ID, entry.getId());
		value.put(DatabaseHandler.ENTRY_DESCRIPTION, entry.getDescription());
		value.put(DatabaseHandler.ENTRY_VALUE, entry.getValue());
		database.update(DatabaseHandler.TABLE_NAME_ENTRY, value, DatabaseHandler.ENTRY_ID + " = ?", new String[]{entry.getId()});
	}
	
	/**
	 * Select all entries
	 *
	 * @return All the entries
	 */
	public Set<String> selectAllEntry() {
		Set<String> entrySet = new TreeSet<>();
		String ent;
		Cursor cursor = database.query(DatabaseHandler.TABLE_NAME_ENTRY, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				ent = cursor.getString(1);
				entrySet.add(ent);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return entrySet;
	}
	
	/**
	 * Select one entry (by its number)
	 *
	 * @param number The number
	 * @return The Entry
	 */
	public Entry selectOneEntry(String number) {
		String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_NAME_ENTRY + " WHERE " + DatabaseHandler.ENTRY_ID + " = ?";
		Cursor cursor = database.rawQuery(selectQuery, new String[]{number});
		Entry entry = cursorToEntry(cursor);
		cursor.close();
		return entry;
	}
	
	/**
	 * Convert a Cursor to an Entry
	 *
	 * @param cursor The cursor to convert
	 * @return The converted entry
	 */
	private Entry cursorToEntry(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		Entry entry = new Entry();
		entry.setId(cursor.getString(0));
		entry.setDescription(cursor.getString(1));
		entry.setValue(cursor.getInt(2));
		return entry;
	}
}
