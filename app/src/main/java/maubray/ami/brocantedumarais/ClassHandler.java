package maubray.ami.brocantedumarais;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ClassHandler {

    private DatabaseHandler dbHelper;
    public static SQLiteDatabase db;

    public ClassHandler(Context context) {
        dbHelper = new DatabaseHandler(context);
    }

    // Ouvrir db
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Fermer db
    public void close() {
        dbHelper.close();
    }

    // Insérer un nouveau emplacement
    public void insertEmplacement(Emplacement emp) {
        ContentValues value = new ContentValues();
        value.put(dbHelper.EMPLACEMENT_NUMBER, emp.getNumber());
        value.put(dbHelper.EMPLACEMENT_CODE, emp.getCode());
        value.put(dbHelper.EMPLACEMENT_ENTRY, emp.getEntry());
        value.put(dbHelper.EMPLACEMENT_SCAN, emp.getScan());
        value.put(dbHelper.EMPLACEMENT_REFUS, emp.getRefus());
        db.insert(dbHelper.TABLE_NAME_EMPLACEMENT, null, value);
    }

    // Sélectionner un emplacement par son numéro
    public Emplacement selectByNumber(String num) {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NAME_EMPLACEMENT + " WHERE " + dbHelper.EMPLACEMENT_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{num});
        Emplacement temp = cursorToEmplacement(cursor);
        cursor.close();
        return temp;
    }

    // Sélectrionner un emplacement par son code
    public Emplacement selectByCode(String code) {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NAME_EMPLACEMENT + " WHERE " + dbHelper.EMPLACEMENT_CODE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{code});
        Emplacement temp = cursorToEmplacement(cursor);
        cursor.close();
        return temp;
    }


    // Sélectionner tous les emplacements
    public List<Emplacement> selectAllEmplacement() {
        List<Emplacement> table = new ArrayList<>();
        Emplacement emp;
        Cursor c = db.query(dbHelper.TABLE_NAME_EMPLACEMENT, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                emp = new Emplacement();
                emp.setNumber(c.getString(0));
                emp.setCode(c.getString(1));
                emp.setEntry(c.getString(2));
                emp.setScan(c.getString(3));
                emp.setRefus(c.getInt(4));
                table.add(emp);
            } while (c.moveToNext());
        }
        c.close();
        return table;
    }

    // Transformer un cursor en un emplacement
    private Emplacement cursorToEmplacement(Cursor c) {
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        Emplacement emp = new Emplacement();
        emp.setNumber(c.getString(0));
        emp.setCode(c.getString(1));
        emp.setEntry(c.getString(2));
        emp.setScan(c.getString(3));
        emp.setRefus(c.getInt(4));
        return emp;
    }

    // Supprimer tous les emplacements
    public void deleteAll() {
        db.delete(dbHelper.TABLE_NAME_EMPLACEMENT, null, null);
        db.delete(dbHelper.TABLE_NAME_ENTRY, null, null);
    }

    // Mettre un jour un emplacement
    public void updateEmplacement(Emplacement emp) {
        ContentValues value = new ContentValues();
        value.put(dbHelper.EMPLACEMENT_NUMBER, emp.getNumber());
        value.put(dbHelper.EMPLACEMENT_CODE, emp.getCode());
        value.put(dbHelper.EMPLACEMENT_ENTRY, emp.getEntry());
        value.put(dbHelper.EMPLACEMENT_SCAN, emp.getScan());
        value.put(dbHelper.EMPLACEMENT_REFUS, emp.getRefus());
        db.update(dbHelper.TABLE_NAME_EMPLACEMENT, value, dbHelper.EMPLACEMENT_NUMBER + " = ?", new String[]{emp.getNumber()});
    }


    // Ce qui concerne les entrées
    public void insertEntry(Entry ent) {
        ContentValues value = new ContentValues();
        value.put(dbHelper.ENTRY_ID, ent.getId());
        value.put(dbHelper.ENTRY_LIBELLE, ent.getLibelle());
        value.put(dbHelper.ENTRY_VALUE, ent.getValue());
        db.insert(dbHelper.TABLE_NAME_ENTRY, null, value);
    }

    public void updateEntry(Entry ent) {
        ContentValues value = new ContentValues();
        value.put(dbHelper.ENTRY_ID, ent.getId());
        value.put(dbHelper.ENTRY_LIBELLE, ent.getLibelle());
        value.put(dbHelper.ENTRY_VALUE, ent.getValue());
        db.update(dbHelper.TABLE_NAME_ENTRY, value, dbHelper.ENTRY_ID + " = ?", new String[]{ent.getId()});
    }

    public Set<String> selectAllEntry(){
        Set<String> table = new TreeSet<>();
        String ent;
        Cursor c = db.query(dbHelper.TABLE_NAME_ENTRY, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                ent = c.getString(1);
                table.add(ent);
            } while (c.moveToNext());
        }
        c.close();
        return table;
    }

    public Entry selectOneEntry(String num){
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NAME_ENTRY + " WHERE " + dbHelper.ENTRY_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{num});
        Entry temp = cursorToEntry(cursor);
        cursor.close();
        return temp;
    }

    // Transformer un cursor en une entry
    private Entry cursorToEntry(Cursor c) {
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        Entry ent = new Entry();
        ent.setId(c.getString(0));
        ent.setLibelle(c.getString(1));
        ent.setValue(c.getInt(2));
        return ent;
    }
}
