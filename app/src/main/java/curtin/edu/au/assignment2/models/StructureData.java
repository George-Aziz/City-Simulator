/********************************************************************
 * Author: George Aziz
 * Date Created: 06/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for storing Structures of the game
 *******************************************************************/
/* Most of class retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.models;

import java.util.Arrays;
import java.util.List;

import curtin.edu.au.assignment2.R;

/**
 * Stores the list of possible structures. This has a static get() method for retrieving an
 * instance, rather than calling the constructor directly.
 *
 * The remaining methods -- get(int), size(), add(Structure) and remove(int) -- provide
 * minimalistic list functionality.
 *
 * There is a static int array called DRAWABLES, which stores all the drawable integer references,
 * some of which are not actually used (yet) in a Structure object.
 */
public class StructureData
{
    public static final int[] DRAWABLES = {
            0, // No structure
            R.drawable.ic_building1, R.drawable.ic_building2, R.drawable.ic_building3,
            R.drawable.ic_building4, R.drawable.ic_building5, R.drawable.ic_building6,
            R.drawable.ic_building7, R.drawable.ic_building8,
            R.drawable.ic_road_ns, R.drawable.ic_road_ew, R.drawable.ic_road_nsew,
            R.drawable.ic_road_ne, R.drawable.ic_road_nw, R.drawable.ic_road_se, R.drawable.ic_road_sw,
            R.drawable.ic_road_n, R.drawable.ic_road_e, R.drawable.ic_road_s, R.drawable.ic_road_w,
            R.drawable.ic_road_nse, R.drawable.ic_road_nsw, R.drawable.ic_road_new, R.drawable.ic_road_sew};

    private List<Structure> structureList = Arrays.asList(new Structure[] {
            new Residential(R.drawable.ic_building1, "House"),
            new Residential(R.drawable.ic_building2, "House"),
            new Residential(R.drawable.ic_building3, "House"),
            new Residential(R.drawable.ic_building4, "House"),
            new Commercial(R.drawable.ic_building5, "Factory"),
            new Commercial(R.drawable.ic_building6, "Factory"),
            new Commercial(R.drawable.ic_building7, "Factory"),
            new Commercial(R.drawable.ic_building8, "Factory"),
            new Road(R.drawable.ic_road_ns, "Road"),
            new Road(R.drawable.ic_road_ew, "Road"),
            new Road(R.drawable.ic_road_nsew, "Road"),
            new Road(R.drawable.ic_road_ne, "Road"),
            new Road(R.drawable.ic_road_nw, "Road"),
            new Road(R.drawable.ic_road_se, "Road"),
            new Road(R.drawable.ic_road_sw, "Road"),
            new Road(R.drawable.ic_road_n, "Road"),
            new Road(R.drawable.ic_road_e, "Road"),
            new Road(R.drawable.ic_road_s, "Road"),
            new Road(R.drawable.ic_road_w, "Road"),
            new Road(R.drawable.ic_road_nse, "Road"),
            new Road(R.drawable.ic_road_nsw, "Road"),
            new Road(R.drawable.ic_road_new, "Road"),
            new Road(R.drawable.ic_road_sew, "Road"),
    });

    private static StructureData instance = null;

    public static StructureData get()
    {
        if(instance == null)
        {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public Structure get(int i)
    {
        return structureList.get(i);
    }

    public int size()
    {
        return structureList.size();
    }
}
