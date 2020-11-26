/****************************************************************************
 * Author: George Aziz
 * Date Created: 07/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating Game Map Database
 *****************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import curtin.edu.au.assignment2.databases.DBSchemas.MapTable;

public class GameMapDBHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Map.db";

    public GameMapDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + MapTable.NAME + "(" + MapTable.Cols.ID + " INTEGER, "+
                MapTable.Cols.BUILDABLE + " INTEGER, " + MapTable.Cols.TERRAIN_NW + " INTEGER, "
                + MapTable.Cols.TERRAIN_NE + " INTEGER, " + MapTable.Cols.TERRAIN_SE + " INTEGER, "
                + MapTable.Cols.TERRAIN_SW + " INTEGER, " + MapTable.Cols.STRUCTURE_ID + " INTEGER, "
                + MapTable.Cols.STRUCTURE_LABEL + " TEXT, " + MapTable.Cols.EDIT_NAME + " TEXT, "
                + MapTable.Cols.IMAGE + " BLOB )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
