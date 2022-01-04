package com.example.projectmobilecomputing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EcommerceDB extends SQLiteOpenHelper {
    public static String databaseName="CommerceDBnew";
    public SQLiteDatabase database;
    public EcommerceDB(Context context){
        super(context,databaseName,null,3);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Customers(CustID integer primary key autoincrement,CutName text,Username text" +
                ",Password text,Gender text,birthdate text,job text,ForgetKey text)");

        db.execSQL("create table Orders(OrdID integer primary key autoincrement,OrdDate text,CustID integer," +
                "Address text,foreign key(CustID) references Customers(CustID))");

        db.execSQL("create table Order_details(OrdID integer not null ,ProID integer not null," +
                "Quantity integer)");

        db.execSQL("create table Products(ProID integer primary key autoincrement,ProName text,Price integer,Quantity integer," +
                "CatID integer,foreign key (CatID) references Categories(CatID))");
        db.execSQL("create table Categories(CatID integer primary key autoincrement,CatName text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Customers");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists Order_details");
        db.execSQL("drop table if exists Products");
        db.execSQL("drop table if exists Categories");
        onCreate(db);
    }
    public void Add_Customer(String Name,String UserName,String Password,String Gender,String Birthdate,String job,String forget){
        ContentValues row=new ContentValues();
        row.put("CutName",Name);
        row.put("Username",UserName);
        row.put("Password",Password);
        row.put("Gender",Gender);
        row.put("birthdate",Birthdate);
        row.put("job",job);
        row.put("ForgetKey",forget);
        database=getWritableDatabase();
        database.insert("Customers",null,row);
        database.close();
    }
    public void add_Product(String ProName,int price,int quantity,int categId){
        ContentValues row=new ContentValues();
        row.put("ProName",ProName);
        row.put("Price",price);
        row.put("Quantity",quantity);
        row.put("CatID",categId);
        database=getWritableDatabase();
        database.insert("Products",null,row);
        database.close();
    }
    public void add_categorie(String catname){
        ContentValues row=new ContentValues();
        row.put("CatName",catname);
        database=getWritableDatabase();
        database.insert("Categories",null,row);
        database.close();
    }
    public String login(String toString) {
        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select Password from Customers where Username = ?",new String[]{toString});
        String passw="";
        if (cursor!=null){
            cursor.moveToFirst();
        }
        if(cursor.getCount()>0){
            passw=cursor.getString(0);
        }
        else {
            passw="";
        }
        database.close();
        return passw;

    }
    public Cursor readAllProducts(){
        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from Products",null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public String getCat(int anInt) {
        database=getReadableDatabase();
        String[]rowdet={};
        Cursor cursor=database.rawQuery("select CatName from Categories where CatID ="+String.valueOf(anInt),rowdet);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }
    public int getCatID(String catname){
        database=getReadableDatabase();
        String[]rowdet={catname};
        Cursor cursor=database.rawQuery("select CatID from Categories where CatName = ? ",rowdet);
        if (cursor!=null){
            cursor.moveToFirst();

        }
        database.close();
        if (cursor.getCount()>0){
            return cursor.getInt(0);
        }
        else {
            return -1;
        }

    }
    public int add_Order(String date,int custID,String address){
        ContentValues row=new ContentValues();
        row.put("OrdDate",date);
        row.put("CustID",custID);
        row.put("Address",address);

        database=getWritableDatabase();
        long id=database.insert("Orers",null,row);
        database.close();
        return (int)id;
    }
    public void add_Order_Deatils(int ordID,int productId,int quantity){
        database=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("OrdID",ordID);
        row.put("ProID",productId);
        row.put("Quantity",quantity);
        database.insert("Order_details",null,row);
        database.close();
    }

    public Cursor readProds(String n) {
        database=getReadableDatabase();
        String[]rowdet={n};
        Cursor cursor=database.rawQuery("select * from Products where ProName = ?",rowdet);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor readAllCatgs() {
        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from Categories",null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor readCatProds(int i) {
        database=getReadableDatabase();
        String[]rowdet={};
        Cursor cursor=database.rawQuery("select * from Products where CatID ="+String.valueOf(i),rowdet);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public int getCustID(String toString) {
        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select CustID from Customers where Username like ?",new String[]{toString});
        if (cursor!=null){
            cursor.moveToFirst();

        }
        database.close();
        return cursor.getInt(0);

    }

    public int getproductId(String s) {
        database=getReadableDatabase();
        String []rowdet={s};
        Cursor cursor=database.rawQuery("select ProID from Products where ProName = ?",rowdet);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor.getInt(0);
    }


    public int getPrice(String remo) {
        database=getReadableDatabase();
        String []rowdet={remo};
        Cursor cursor=database.rawQuery("select Price from Products where ProName = ?",rowdet);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor.getInt(0);
    }

    public Cursor getForgetKey(String toString) {

        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from Customers where Username = ?",new String[]{toString});
        if (cursor!=null){
            cursor.moveToFirst();
        }

        database.close();
        return cursor;
    }

    public void updateCustomer(ContentValues row,int id) {
        database=getWritableDatabase();
        database.update("Customers",row,"CustID =?",new String[]{String.valueOf(id)});
        database.close();
    }
}
class Order {
    int ord_ID;
    String ord_Date;
    int cust_ID;
    int[] prod_ID;
    int []quantity;
    public Order(){
        this.ord_ID=0;
        this.ord_Date="";
        this.cust_ID=0;
        this.prod_ID=new int[]{0};
        this.quantity=new int[]{0};
    }

}
class Product{
    int product_ID;
    String product_Name;
    int product_Price;
    int product_quantity;
    String categorie;
    public Product(){
        this.product_ID=0;
        this.product_Name="";
        this.product_Price=0;
        this.product_quantity=0;
        this.categorie="";
    }
}
