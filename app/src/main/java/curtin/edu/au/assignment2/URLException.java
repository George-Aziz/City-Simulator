/********************************************************************************
 * Author: George Aziz
 * Date Created: 09/10/2020
 * Date Last Modified : 09/10/2020
 * Purpose: Exception Class For When Doing API Call To Retrieve Temperature Value
 ********************************************************************************/

package curtin.edu.au.assignment2;

public class URLException extends Exception
{
    public URLException(String message)
    {
        super(message);
    }

    public URLException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
