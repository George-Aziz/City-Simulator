/************************************************************************
 * Author: George Aziz
 * Date Created: 06/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creation and storage of structure data
 ***********************************************************************/
/* Class retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.models;

public abstract class Structure
{
    private final int drawableId;
    private String label;

    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }
}
