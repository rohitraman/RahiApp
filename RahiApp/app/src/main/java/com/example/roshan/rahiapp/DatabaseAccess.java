package com.example.roshan.rahiapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DatabaseAccess {
    DatabaseHelper helper;
    SQLiteDatabase database;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context)
    {
        this.helper = new DatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if(instance == null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void  open()
    {
        this.database = helper.getWritableDatabase();
    }

    public void close()
    {
        if(database!=null)
        {
            this.database.close();
        }
    }

    public Map<Integer,String> getTrains(String from,String to)
    {
        String sql1 = "select _id from station_table where stationCode = ? or stationCode = ? order by _id";

        String sql = "with train_values(value) as (select distinct s.trainId from route_table s join route_table e on s.trainId = e.trainId where s.stationId = ? and e.stationId = ? and s._id<e._id)\n" +
                "select train_table.trainNO,train_table.trainName from train_table,train_values where train_table._id = train_values.value";
        Map<Integer,String> trains = new HashMap<>();
        Cursor cursor = database.rawQuery(sql1,new String[]{from,to});
        cursor.moveToFirst();
        List<String> arrayList = new ArrayList<>();
        while (!cursor.isAfterLast())
        {
            arrayList.add(Integer.toString(cursor.getInt(0)));
            Log.i("Station",arrayList.toString());
            cursor.moveToNext();

        }
        cursor.close();
        String[] stationCodes =  new String[arrayList.size()];
        stationCodes = arrayList.toArray(stationCodes);
        for (String e:stationCodes)
        {
            Log.i("Helooo",e);
        }
        cursor = database.rawQuery(sql,stationCodes);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            trains.put(cursor.getInt(0),cursor.getString(1));
            cursor.moveToNext();
        }

        Log.i("Size", String.valueOf(trains.size()));

        return trains;
    }

    public ListItems getStaionAndArrival(int trainno)
    {
        ListItems items = new ListItems();
        String sql = "select station_table.stationName,route_table.arrival,route_table.halt,route_table.dist from route_table,station_table,train_table where route_table.trainId=train_table._id and route_table.stationId=station_table._id and train_table.trainNO=?";
        Cursor cursor = database.rawQuery(sql,new String[]{Integer.toString(trainno)});
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            items.stationNames.add(cursor.getString(0));
            items.arrivals.add(cursor.getString(1));
            items.halts.add(cursor.getInt(2));
            items.distances.add(cursor.getString(3));
            Log.i("String",cursor.getString(0));
            cursor.moveToNext();
        }

        return items;
    }

    public List<String> getStations()
    {
        String sql = "select stationName,stationCode from station_table";
        List<String> stations = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String code = cursor.getString(1);
            String name = cursor.getString(0);
            stations.add(name+" - "+code);
            Log.i("Lol","lol");
            cursor.moveToNext();
        }
        Log.i("Stations",stations.toString());

        return stations;
    }

    public List<String> getTrains1()
    {
        String sql = "select trainNO,trainName from train_table";
        List<String> trains = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String number = cursor.getString(0);
            String name = cursor.getString(1);
            trains.add(number+" - "+name);
            Log.i("Lol","lol");
            cursor.moveToNext();
        }
        Log.i("Stations",trains.toString());

        return trains;
    }
}

class ListItems
{
    List<String> stationNames = new ArrayList<>();
    List<String> arrivals = new ArrayList<>();
    List<Integer> halts = new ArrayList<>();
    List<String> distances = new ArrayList<>();
}
