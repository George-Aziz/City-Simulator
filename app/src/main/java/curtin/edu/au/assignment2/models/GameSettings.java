/************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating and storing the game settings
 ***********************************************************************/

package curtin.edu.au.assignment2.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import curtin.edu.au.assignment2.databases.DBSchemas.SettingsTable;
import curtin.edu.au.assignment2.databases.GameSettingsCursor;
import curtin.edu.au.assignment2.databases.GameSettingsDBHelper;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class GameSettings
{
    private static GameSettings instance;

    private final int id = 1;
    private int mapWidth = 50;
    private int mapHeight = 10;
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.3;
    private int serviceCost = 2;
    private int houseBuildingCost = 100;
    private int commBuildingCost = 500;
    private int roadBuildingCost = 20;
    private String cityName = "Perth";

    private SQLiteDatabase db;
    private GameSettingsCursor cursor;

    private GameSettings() { }

    //Alternate Constructor for GameSettingsCursor
    public GameSettings(int mapWidth, int mapHeight, int initialMoney, int serviceCost,
                        int houseBuildingCost, int commBuildingCost, int roadBuildingCost, String cityName)
    {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.initialMoney = initialMoney;
        this.serviceCost = serviceCost;
        this.houseBuildingCost = houseBuildingCost;
        this.commBuildingCost = commBuildingCost;
        this.roadBuildingCost = roadBuildingCost;
        this.cityName = cityName;
    }

    public static GameSettings get()
    {
        if (instance == null)
        {
            instance = new GameSettings();
        }
        return instance;
    }

    //ACCESSORS
    public int getId()
    {
        return id;
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public int getInitialMoney()
    {
        return initialMoney;
    }

    public int getFamilySize()
    {
        return familySize;
    }

    public int getShopSize()
    {
        return shopSize;
    }

    public int getSalary()
    {
        return salary;
    }

    public double getTaxRate()
    {
        return taxRate;
    }

    public int getServiceCost()
    {
        return serviceCost;
    }

    public int getHouseBuildingCost()
    {
        return houseBuildingCost;
    }

    public int getCommBuildingCost()
    {
        return commBuildingCost;
    }

    public int getRoadBuildingCost()
    {
        return roadBuildingCost;
    }

    public String getCityName()
    {
        return cityName;
    }

    //MUTATORS
    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    public void setInitialMoney(int money)
    {
        this.initialMoney = money;
    }
    public void setServiceCost(int serviceCost)
    {
        this.serviceCost = serviceCost;
    }

    public void setHouseBuildingCost(int houseBuildingCost)
    {
        this.houseBuildingCost = houseBuildingCost;
    }

    public void setCommBuildingCost(int commBuildingCost)
    {
        this.commBuildingCost = commBuildingCost;
    }

    public void setRoadBuildingCost(int roadBuildingCost)
    {
        this.roadBuildingCost = roadBuildingCost;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }


    //Database Section
    public void load(Context context)
    {
        this.db = new GameSettingsDBHelper(context.getApplicationContext()).getWritableDatabase();

        cursor = new GameSettingsCursor(
                db.query(SettingsTable.NAME, // FROM Settings Table
                        null, // SELECT ALL COLUMNS
                        null, // WHERE CLAUSE (null = all rows)
                        null, // WHERE ARGUMENTS
                        null, // GROUP BY ARGUMENTS
                        null, // HAVING clause
                        null // ORDER BY clause
                ));

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                //Sets database settings into current game's settings
                instance = cursor.getSettings(); //Gets Settings also updates all settings inside
                instance.setDb(this.db);
                this.mapWidth = instance.getMapWidth();
                this.mapHeight = instance.getMapHeight();
                this.initialMoney = instance.getInitialMoney();
                this.serviceCost = instance.getServiceCost();
                this.houseBuildingCost = instance.getHouseBuildingCost();
                this.commBuildingCost = instance.getCommBuildingCost();
                this.roadBuildingCost = instance.getRoadBuildingCost();
                this.cityName = instance.getCityName();

                cursor.moveToNext();
            }
        } finally
        {
            cursor.close();
        }
    }

    public void saveSettings()
    {
        //Only add if there is no other settings since we do not want to have multiple
        if (cursor.getCount() < 1)
        {
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.ID, getId());
            cv.put(SettingsTable.Cols.MAP_WIDTH, getMapWidth());
            cv.put(SettingsTable.Cols.MAP_HEIGHT, getMapHeight());
            cv.put(SettingsTable.Cols.INITIAL_MONEY, getInitialMoney());
            cv.put(SettingsTable.Cols.SERVICE_COST, getServiceCost());
            cv.put(SettingsTable.Cols.HOUSE_BUILDING_COST, getHouseBuildingCost());
            cv.put(SettingsTable.Cols.COMM_BUILDING_COST, getCommBuildingCost());
            cv.put(SettingsTable.Cols.ROAD_BUILDING_COST, getRoadBuildingCost());
            cv.put(SettingsTable.Cols.CITY_NAME, getCityName());

            db.insert(SettingsTable.NAME, null, cv);
        }
    }

    public void updateSettings()
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID, getId());
        cv.put(SettingsTable.Cols.MAP_WIDTH, getMapWidth());
        cv.put(SettingsTable.Cols.MAP_HEIGHT, getMapHeight());
        cv.put(SettingsTable.Cols.INITIAL_MONEY, getInitialMoney());
        cv.put(SettingsTable.Cols.SERVICE_COST, getServiceCost());
        cv.put(SettingsTable.Cols.HOUSE_BUILDING_COST, getHouseBuildingCost());
        cv.put(SettingsTable.Cols.COMM_BUILDING_COST, getCommBuildingCost());
        cv.put(SettingsTable.Cols.ROAD_BUILDING_COST, getRoadBuildingCost());
        cv.put(SettingsTable.Cols.CITY_NAME, getCityName());

        String[] whereValue = {String.valueOf(getId())};

        db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " =?", whereValue);
    }
}
