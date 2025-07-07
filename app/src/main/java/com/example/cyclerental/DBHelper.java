package com.example.cyclerental;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RentalDB.db";
    private static final int DATABASE_VERSION = 7;


    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_CONTACT = "contact";
    private static final String COL_PASSWORD = "password";
    private static final String COL_GENDER = "gender";
    private static final String COL_P_IMAGE = "imagepath";


    private static final String TABLE_CYCLES = "cycles";
    private static final String COL_CYCLE_ID = "cycle_id";
    private static final String COL_CYCLE_NAME = "name";
    private static final String COL_CYCLE_PRICE = "price";
    private static final String COL_CYCLE_TYPE = "type";
    private static final String COL_CYCLE_LOCATION = "location";
    private static final String COL_CYCLE_AVAILABILITY = "availability";
    private static final String COL_CYCLE_IMAGE = "imagepath";


    private static final String TABLE_RENTAL = "rentals";
    private static final String COL_RENTAL_ID = "rent_id";
    private static final String COL_RENTAL_NAME = "cycle_name";
    private static final String COL_RENTAL_PRICE = "cycle_price";
    private static final String COL_RENTAL_TYPE = "cycle_type";
    private static final String COL_RENTAL_LOCATION = "cycle_location";
    private static final String COL_RENTER_DATE = "renter_date";
    private static final String COL_RENTER_EMAIL = "renter_email";
    private static final String COL_RENTAL_TIME = "rental_time";
    private static final String COL_RENTER_PLAN = "rental_plan";
    private static final String COL_RENTER_DURA = "rental_duration";
    private static final String COL_RENTER_ENDLOCATION = "return_location";
    private static final String COL_RENTER_PAY = "payment_status";


    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_CONTACT + " TEXT, " +
                COL_GENDER + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_P_IMAGE + " TEXT)";
        db.execSQL(createTable);

        String query = "CREATE TABLE " + TABLE_CYCLES + " ("
                + COL_CYCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CYCLE_NAME + " TEXT,"
                + COL_CYCLE_PRICE + " REAL,"
                + COL_CYCLE_TYPE + " TEXT,"
                + COL_CYCLE_LOCATION + " TEXT,"
                + COL_CYCLE_AVAILABILITY + " INTEGER,"
                + COL_CYCLE_IMAGE + " TEXT)";
        db.execSQL(query);

        insertSampleData(db);

        String createRentalsTable = "CREATE TABLE " + TABLE_RENTAL + " (" +
                COL_RENTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RENTAL_NAME + " TEXT, " +
                COL_RENTAL_PRICE + " REAL, " +
                COL_RENTAL_TYPE + " TEXT, " +
                COL_RENTAL_LOCATION + " TEXT, " +
                COL_RENTER_DATE + " TEXT, " +
                COL_RENTER_EMAIL + " TEXT, " +
                COL_RENTER_PLAN + " TEXT, " +
                COL_RENTER_DURA + " TEXT, " +
                COL_RENTER_ENDLOCATION + " TEXT, " +
                COL_RENTER_PAY + " TEXT, " +
                COL_RENTAL_TIME + " TEXT)";
        db.execSQL(createRentalsTable);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CYCLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTAL);

        onCreate(db);

    }

    public boolean insertUser(String name, String email, String contact, String gender, String password, String imagepath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_CONTACT, contact);
        values.put(COL_GENDER, gender);
        values.put(COL_PASSWORD, password);
        values.put(COL_P_IMAGE, imagepath);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }



    public boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT name, contact, password, imagepath FROM users WHERE email = ?", new String[]{email});
    }



    public boolean updateUserProfile(String email, String name, String contact, String password, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("contact", contact);
        values.put("password", password);
        values.put("imagepath", imagePath);

        int rowsAffected = db.update("users", values, "email = ?", new String[]{email});
        return rowsAffected > 0;
    }



    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_CYCLES + " (name, price, type, location, availability, imagepath) VALUES " +
                "('Speedster 3000', 2, 'Road Bike', 'Texas', 1, 'road_bike')," +
                "('TwinCruze X2', 6, 'Tandem Bike', 'New York', 1, 'tandem_bike')," +
                "('Beach Rover Y7', 3, 'Cruiser Bike', 'Florida', 1, 'cruiser_bike')," +
                "('TrailBlazer Pro', 2, 'Trek Bike', 'Texas', 0, 'trek_bike')," +
                "('Junior Glide Zr', 3, 'Kids Bike', 'Arizona', 1, 'kids_bike')," +
                "('Velocity 2.0', 3, 'Road Bike', 'Texas', 1, 'road2_bike')," +
                "('RockHopper XT', 2, 'Mountain Bike', 'Florida', 1, 'mountain2_bike')," +
                "('DoubleDash Duo', 5, 'Tandem Bike', 'New York', 1, 'tandem2_bike')," +
                "('MiniCruze W4', 2, 'Kids Bike', 'Florida', 1, 'kids2_bike')," +
                "('PeakRider 400', 3, 'Mountain Bike', 'Arizona', 1, 'mountain_bike');");

    }

    public List<Cycle> getAllCycles() {
        List<Cycle> cycleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CYCLES, null);

        if (cursor.moveToFirst()) {
            do {
                cycleList.add(new Cycle(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cycleList;
    }

    public int insertRentalAndReturnId(String cycleName, double cyclePrice, String cycleType,
                                       String cycleLocation, String renterDate, String rentalTime,
                                       String rentalPlan, String rentalDuration,
                                       String returnLocation, String paymentStatus, String renterEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_RENTAL_NAME, cycleName);
        values.put(COL_RENTAL_PRICE, cyclePrice);
        values.put(COL_RENTAL_TYPE, cycleType);
        values.put(COL_RENTAL_LOCATION, cycleLocation);
        values.put(COL_RENTER_DATE, renterDate);
        values.put(COL_RENTAL_TIME, rentalTime);
        values.put(COL_RENTER_PLAN, rentalPlan);
        values.put(COL_RENTER_DURA, rentalDuration);
        values.put(COL_RENTER_ENDLOCATION, returnLocation);
        values.put(COL_RENTER_PAY, paymentStatus); // Initially "Pending"
        values.put(COL_RENTER_EMAIL, renterEmail);

        long result = db.insert(TABLE_RENTAL, null, values);
        db.close();

        return (result == -1) ? -1 : (int) result; // Return rental ID or -1 if failed
    }


    public boolean updatePaymentStatus(int rentalId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RENTER_PAY, newStatus); // Change status to "Paid"

        int rowsAffected = db.update(TABLE_RENTAL, values, COL_RENTAL_ID + " = ?", new String[]{String.valueOf(rentalId)});
        db.close();

        return rowsAffected > 0; // Returns true if at least one row is updated
    }


    public List<History> getRentalHistory(String email) {
        List<History> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modified query to sort by date (descending) and time (descending)
        Cursor cursor = db.rawQuery("SELECT " +
                        COL_RENTAL_NAME + ", " +
                        COL_RENTAL_PRICE + ", " +
                        COL_RENTAL_TYPE + ", " +
                        COL_RENTAL_LOCATION + ", " +
                        COL_RENTAL_TIME + ", " +
                        COL_RENTER_DATE + ", " +
                        COL_RENTER_PAY + ", " +
                        COL_RENTER_PLAN + ", " +
                        COL_RENTER_DURA + ", " +
                        COL_RENTAL_ID +
                        " FROM " + TABLE_RENTAL +
                        " WHERE " + COL_RENTER_EMAIL + " = ?" +
                        " ORDER BY " + COL_RENTER_DATE + " DESC, " + COL_RENTAL_TIME + " DESC",
                new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                History history = new History(
                        cursor.getString(0),  // name
                        cursor.getDouble(1),  // price
                        cursor.getString(2),  // type
                        cursor.getString(3),  // location
                        cursor.getString(4),  // time
                        cursor.getString(5),  // date
                        cursor.getString(6),  // payment status
                        cursor.getString(7),  // plan
                        cursor.getString(8), // duration
                        cursor.getInt(9)     // rental id
                );
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }









}


