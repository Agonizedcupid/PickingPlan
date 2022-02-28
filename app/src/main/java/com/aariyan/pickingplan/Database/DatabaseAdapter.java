package com.aariyan.pickingplan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.aariyan.pickingplan.Model.PlanModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {


    DatabaseHelper helper;
    private List<PlanModel> planList = new ArrayList<>();


    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    //Insert Header:
    public long insertPlans(int intAutoPicking, String Storename, String Quantity, String ItemCode, String Description, String SalesOrderNo,
                            int OrderId, String mass, int LineNos, String weights, String OrderDate, String Instruction, String Area, String Toinvoice,
                            String toLoad, String reference) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.intAutoPicking, intAutoPicking);
        contentValues.put(DatabaseHelper.Storename, Storename);
        contentValues.put(DatabaseHelper.Quantity, Quantity);
        contentValues.put(DatabaseHelper.ItemCode, ItemCode);
        contentValues.put(DatabaseHelper.Description, Description);
        contentValues.put(DatabaseHelper.SalesOrderNo, SalesOrderNo);
        contentValues.put(DatabaseHelper.OrderId, OrderId);
        contentValues.put(DatabaseHelper.mass, mass);
        contentValues.put(DatabaseHelper.LineNos, LineNos);
        contentValues.put(DatabaseHelper.weights, weights);
        contentValues.put(DatabaseHelper.OrderDate, OrderDate);
        contentValues.put(DatabaseHelper.Instruction, Instruction);
        contentValues.put(DatabaseHelper.Area, Area);
        contentValues.put(DatabaseHelper.Toinvoice, Toinvoice);
        contentValues.put(DatabaseHelper.toLoad, toLoad);
        contentValues.put(DatabaseHelper.reference, reference);

        long id = database.insert(DatabaseHelper.PLAN_TABLE_NAME, null, contentValues);
        return id;
    }


    //get PLAN by reference No:
    public List<PlanModel> getPlansByReference(String reference) {

        planList.clear();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.reference + "=?";
//                " and " + DatabaseHelper.DATE + "=?" +
//                " and " + DatabaseHelper.ROUTE_NAME + "=?" +
//                " and " + DatabaseHelper.ORDER_TYPES + "=?" +
//                " and " + DatabaseHelper.userId + "=?";


        String[] args = {reference};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.intAutoPicking, DatabaseHelper.Storename, DatabaseHelper.Quantity,
                DatabaseHelper.ItemCode, DatabaseHelper.Description, DatabaseHelper.SalesOrderNo, DatabaseHelper.OrderId,
                DatabaseHelper.mass, DatabaseHelper.LineNos, DatabaseHelper.weights, DatabaseHelper.OrderDate,
                DatabaseHelper.Instruction, DatabaseHelper.Area, DatabaseHelper.Toinvoice, DatabaseHelper.toLoad, DatabaseHelper.reference};

        Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            PlanModel model = new PlanModel(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getInt(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getString(15),
                    cursor.getString(16)
            );
            planList.add(model);
        }
        return planList;
    }


    // Update Quantity by name and reference code:
    public long updatePlanToLoad(String itemName, String referenceCode, String quantity, String storeName, int lineNo) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.Description + " LIKE ? AND " + DatabaseHelper.reference + " LIKE ? AND " +
                DatabaseHelper.Storename + " LIKE ? AND " + DatabaseHelper.LineNos + " LIKE ? ";
        String[] args = {itemName, referenceCode, storeName, "" + lineNo};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.toLoad, quantity);

        long ids = database.update(DatabaseHelper.PLAN_TABLE_NAME, contentValues, selection, args);

        return ids;
    }


//    //Getting all the user
//    public List<UserListModel> getUserData() {
//
//        list.clear();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        String[] columns = {DatabaseHelper.UID, DatabaseHelper.uID, DatabaseHelper.strPinCode, DatabaseHelper.strName, DatabaseHelper.intCompanyID};
//        Cursor cursor = database.query(DatabaseHelper.USER_TABLE_NAME, columns, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//
//            UserListModel model = new UserListModel(
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(3),
//                    cursor.getString(4)
//            );
//            list.add(model);
//        }
//        return list;
//
//    }
//
//    //Insert customer data
//    public long insertCustomerData(String strCustName, String strCustDesc, String Uid) {
//
//        SQLiteDatabase database = helper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.strCustName, strCustName);
//        contentValues.put(DatabaseHelper.strCustDesc, strCustDesc);
//        contentValues.put(DatabaseHelper.Uid, Uid);
//
//        long id = database.insert(DatabaseHelper.CUSTOMER_TABLE_NAME, null, contentValues);
//        return id;
//    }
//
//    //Getting all the Customer
//    public List<CustomerModel> getAlLCustomer() {
//
//        customerList.clear();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        String[] columns = {DatabaseHelper.UID, DatabaseHelper.strCustName, DatabaseHelper.strCustDesc, DatabaseHelper.Uid};
//        Cursor cursor = database.query(DatabaseHelper.CUSTOMER_TABLE_NAME, columns, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//
//            CustomerModel model = new CustomerModel(
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(3)
//            );
//            customerList.add(model);
//        }
//        return customerList;
//
//    }
//
//    //getTiming by User Name and Customer Name
//    public List<TimingModel> getTiming(String userName, String customerName) {
//
//        timingList.clear();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        //select * from tableName where name = ? and customerName = ?:
//        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
//        String selection = DatabaseHelper.USER_NAME + "=?" + " and " + DatabaseHelper.CUSTOMER_NAME + "=?";
//
//        Log.d("NAME_TEST", userName + " -> " + customerName);
//
//        String[] args = {userName, customerName};
//        String[] columns = {DatabaseHelper.UID, DatabaseHelper.USER_NAME, DatabaseHelper.CUSTOMER_NAME, DatabaseHelper.START_DATE,
//                DatabaseHelper.BILLABLE_TIME, DatabaseHelper.STATUS, DatabaseHelper.TOTAL_TIME, DatabaseHelper.WORK_TYPE,
//                DatabaseHelper.COMPLETED, DatabaseHelper.DESCRIPTION};
//
//        Cursor cursor = database.query(DatabaseHelper.TIMING_TABLE_NAME, columns, selection, args, null, null, null);
//        while (cursor.moveToNext()) {
//            TimingModel model = new TimingModel(
//                    cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(3),
//                    cursor.getString(4),
//                    cursor.getString(5),
//                    cursor.getString(6),
//                    cursor.getString(7),
//                    cursor.getString(8),
//                    cursor.getString(9)
//            );
//            timingList.add(model);
//        }
//        return timingList;
//
//    }
//
//
//    //Delete timing by User Name , Customer Name, ID
//    public long deleteTiming(String userName, String customerName, int id) {
//        SQLiteDatabase database = helper.getWritableDatabase();
//        String selection = DatabaseHelper.USER_NAME + " LIKE ? AND " + DatabaseHelper.CUSTOMER_NAME + " LIKE ? AND " + DatabaseHelper.UID + " LIKE ?";
//
//        String[] args = {userName, customerName, "" + id};
//        long ids = database.delete(DatabaseHelper.TIMING_TABLE_NAME, selection, args);
//
//        return ids;
//    }
//
//    //Delete timing by id
//    public long deleteJobs(int id) {
//        SQLiteDatabase database = helper.getWritableDatabase();
//        //select * from table_name where id = id
//        String selection = DatabaseHelper.UID + " LIKE ?";
//
//        String[] args = {"" + id};
//        long ids = database.delete(DatabaseHelper.TIMING_TABLE_NAME, selection, args);
//
//        return ids;
//    }

    class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        private static final String DATABASE_NAME = "picking_N_plan.db";
        private static final int VERSION_NUMBER = 5;

        //Header Table:
        private static final String PLAN_TABLE_NAME = "plans";
        private static final String UID = "_id";
        private static final String intAutoPicking = "intAutoPicking";
        private static final String Storename = "Storename";
        private static final String Quantity = "Quantity";
        private static final String ItemCode = "ItemCode";
        private static final String Description = "Description";
        private static final String SalesOrderNo = "SalesOrderNo";
        private static final String OrderId = "OrderId";
        private static final String mass = "mass";
        private static final String LineNos = "LineNos";
        private static final String weights = "weights";
        private static final String OrderDate = "OrderDate";
        private static final String Instruction = "Instruction";
        private static final String Area = "Area";
        private static final String Toinvoice = "Toinvoice";
        private static final String toLoad = "toLoad";
        private static final String reference = "reference";

        //Creating the table:
        private static final String CREATE_PLANS_TABLE = "CREATE TABLE " + PLAN_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + intAutoPicking + " INTEGER,"
                + Storename + " VARCHAR(255),"
                + Quantity + " VARCHAR(255),"
                + ItemCode + " VARCHAR(255),"
                + Description + " VARCHAR(255),"
                + SalesOrderNo + " VARCHAR(255),"
                + OrderId + " INTEGER,"
                + mass + " VARCHAR(255),"
                + LineNos + " INTEGER,"
                + weights + " VARCHAR(255),"
                + OrderDate + " VARCHAR(255),"
                + Instruction + " VARCHAR(255),"
                + Area + " VARCHAR(255),"
                + Toinvoice + " VARCHAR(255),"
                + toLoad + " VARCHAR(255),"
                + reference + " VARCHAR(255));";
        private static final String DROP_PLANS_TABLE = "DROP TABLE IF EXISTS " + PLAN_TABLE_NAME;


        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUMBER);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table:
            try {
                db.execSQL(CREATE_PLANS_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_PLANS_TABLE);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}