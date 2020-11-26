/************************************************************************************************
 * Author: George Aziz
 * Date Created: 08/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class allows game to be able to get game data contents when loading at start of game
 ***********************************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import curtin.edu.au.assignment2.models.GameData;
import curtin.edu.au.assignment2.databases.DBSchemas.GameDataTable;

public class GameDataCursor extends CursorWrapper
{
    public GameDataCursor(Cursor cursor) { super(cursor); }

    public GameData getGameData()
    {
        //Gets All the Game's Data From Database
        int money = getInt(getColumnIndex(GameDataTable.Cols.MONEY));
        int gameTime = getInt(getColumnIndex(GameDataTable.Cols.GAME_TIME));
        int nCommercial = getInt(getColumnIndex(GameDataTable.Cols.NUM_COMMERCIAL));
        int nResidential = getInt(getColumnIndex(GameDataTable.Cols.NUM_RESIDENTIAL));
        int population = getInt(getColumnIndex(GameDataTable.Cols.POPULATION));

        return new GameData(money, gameTime, nCommercial, nResidential, population);

    }
}
