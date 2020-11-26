/****************************************************************************
 * Author: George Aziz
 * Date Created: 08/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating Game Data database
 *****************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import curtin.edu.au.assignment2.databases.DBSchemas.GameDataTable;

public class GameDataDBHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "GameData.db";

    public GameDataDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + GameDataTable.NAME + "(" + GameDataTable.Cols.ID + " INTEGER, " +
                GameDataTable.Cols.MONEY + " INTEGER, " + GameDataTable.Cols.GAME_TIME + " INTEGER, " +
                GameDataTable.Cols.NUM_RESIDENTIAL + " INTEGER," + GameDataTable.Cols.NUM_COMMERCIAL +
                " INTEGER, " + GameDataTable.Cols.POPULATION + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
