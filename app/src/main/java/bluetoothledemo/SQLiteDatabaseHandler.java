package bluetoothledemo;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TiresDB";
    private static final String TABLE_NAME = "Tires";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CURTEMP = "curTemp";
    private static final String KEY_BASETEMP = "baseTemp";
    private static final String KEY_CURPRES = "curPres";
    private static final String KEY_BASEPRES = "basePres";
    private static final String[] COLUMNS = { KEY_ADDRESS, KEY_CURTEMP, KEY_BASETEMP,KEY_CURPRES,KEY_BASEPRES };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Tires ( "
                + "key_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "address TEXT, " +
                "curTemp INTEGER, " +
                "baseTemp INTEGER, " +
                "curPres INTEGER, " +
                "basePres INTEGER" + ")";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(Tire tire) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "address = ?", new String[] { tire.getAddress() });
    }

    public SQLiteDatabase getDB(){
        return this.getWritableDatabase();
    }
    public Tire getTire(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " address = ?", new String[] { address }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            return new Tire(cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5));
        }else{
            System.out.println(cursor);
            System.out.println("ERROR!!");
            return new Tire("1",1,1,1,1);
        }
    }

    public List<Tire> allTires() {

        List<Tire> tires = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);
        Tire tire;

        if (cursor.moveToFirst()) {
            do {
                tire = new Tire(cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5));
                tires.add(tire);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tires;
    }

    public void addTire(Tire tire) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS , tire.getAddress());
        values.put(KEY_CURTEMP, tire.getCurTemp());
        values.put(KEY_BASETEMP, tire.getBaseTemp());
        values.put(KEY_CURPRES , tire.getCurPres());
        values.put(KEY_BASEPRES, tire.getBasePres());

        // insert
        db.insert(TABLE_NAME,null, values);

    }

    public void updateTire(Tire tire) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS , tire.getAddress());
        values.put(KEY_CURTEMP, tire.getCurTemp());
        values.put(KEY_BASETEMP, tire.getBaseTemp());
        values.put(KEY_CURPRES , tire.getCurPres());
        values.put(KEY_BASEPRES, tire.getBasePres());

        db.update(TABLE_NAME, values, "address = ?", new String[] { tire.getAddress() });

    }

    public boolean tireExists(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " address = ?", new String[] { address }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            try {
                if (cursor.getString(0) != null)
                    cursor.close();
                    return true;
            }catch(Exception e){
                cursor.close();
                return false;
            }
        }
        cursor.close();
        return false;
    }

    public void updateTireRunning(String address, int temp, int pressure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CURTEMP, temp);
        values.put(KEY_CURPRES , pressure);
        db.update(TABLE_NAME, values, "address = ?", new String[] { address });
    }
}