/****************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating Game Settings Database
 *****************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import curtin.edu.au.assignment2.databases.DBSchemas.SettingsTable;

public class GameSettingsDBHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Settings.db";

    public GameSettingsDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "(" + SettingsTable.Cols.ID + " INTEGER, " +
                SettingsTable.Cols.MAP_WIDTH + " INTEGER, " + SettingsTable.Cols.MAP_HEIGHT + " INTEGER, " +
                SettingsTable.Cols.INITIAL_MONEY + " INTEGER," + SettingsTable.Cols.SERVICE_COST + " INTEGER, " +
                SettingsTable.Cols.HOUSE_BUILDING_COST + " INTEGER, " + SettingsTable.Cols.COMM_BUILDING_COST + " INTEGER, " +
                SettingsTable.Cols.ROAD_BUILDING_COST + " INTEGER, " + SettingsTable.Cols.CITY_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
