/***************************************************************************************************
 * Author: George Aziz
 * Date Created: 05/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class allows game to be able to get game settings contents when loading at start of game
 ***************************************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import curtin.edu.au.assignment2.models.GameSettings;
import curtin.edu.au.assignment2.databases.DBSchemas.SettingsTable;

public class GameSettingsCursor extends CursorWrapper
{
    public GameSettingsCursor(Cursor cursor) { super(cursor); }

    public GameSettings getSettings()
    {
        //Gets All the Game's Settings From Database
        int mapWidth = getInt(getColumnIndex(SettingsTable.Cols.MAP_WIDTH));
        int mapHeight =  getInt(getColumnIndex(SettingsTable.Cols.MAP_HEIGHT));
        int initialMoney =  getInt(getColumnIndex(SettingsTable.Cols.INITIAL_MONEY));
        int serviceCost =  getInt(getColumnIndex(SettingsTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(SettingsTable.Cols.HOUSE_BUILDING_COST)) ;
        int commBuldingCost =  getInt(getColumnIndex(SettingsTable.Cols.COMM_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(SettingsTable.Cols.ROAD_BUILDING_COST));
        String cityName = getString(getColumnIndex(SettingsTable.Cols.CITY_NAME));

        return new GameSettings(mapWidth, mapHeight, initialMoney, serviceCost, houseBuildingCost, commBuldingCost, roadBuildingCost, cityName);
    }
}
