/****************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class Responsible for each database table names and column names
 ***************************************************************************/

package curtin.edu.au.assignment2.databases;

public class DBSchemas
{
    //Database Table for Game Settings
    public static class SettingsTable
    {
        public static final String NAME = "Settings";

        public static class Cols
        {
            public static final String ID = "ID"; //Primary Key
            public static final String MAP_WIDTH = "Map_Width";
            public static final String MAP_HEIGHT = "Map_Height";
            public static final String INITIAL_MONEY = "Initial_Money";
            public static final String SERVICE_COST = "Service_Cost";
            public static final String HOUSE_BUILDING_COST = "House_Building_Cost";
            public static final String COMM_BUILDING_COST = "Comm_Building_Cost";
            public static final String ROAD_BUILDING_COST = "Road_Building_Cost";
            public static final String CITY_NAME = "City_Name";
        }
    }

    //Database Table for Game Data
    public static class GameDataTable
    {
        public static final String NAME = "Game_Data";

        public static class Cols
        {
            public static final String ID = "ID"; //Primary Key
            public static final String MONEY = "Money";
            public static final String GAME_TIME = "Game_Time";
            public static final String NUM_COMMERCIAL = "Num_Commercial";
            public static final String NUM_RESIDENTIAL = "Num_Residential";
            public static final String POPULATION = "Population";
        }
    }

    //Database Table for Game Map
    public static class MapTable
    {
        public static final String NAME = "Map";

        public static class Cols
        {
            public static final String ID = "ID"; // Primary Key
            public static final String BUILDABLE = "Buildable";
            public static final String TERRAIN_NW = "Terrain_NorthWest";
            public static final String TERRAIN_NE = "Terrain_NorthEast";
            public static final String TERRAIN_SE = "Terrain_SouthEast";
            public static final String TERRAIN_SW = "Terrain_SouthWest";
            public static final String STRUCTURE_ID = "Structure_ID";
            public static final String STRUCTURE_LABEL = "Structure_Label";
            public static final String EDIT_NAME = "Edit_Name";
            public static final String IMAGE = "Image";
        }
    }
}
