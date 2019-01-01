/*
 *Controls the size of the scene in one class so that all of the scenes
 * are easily changed in one central class. 
 */
package controller;

/**
 * @author humanfriendlyfolder
 * this class manages the height and width of windows
 */
public class SceneCntl 
{
    private static int ht = 510;
    private static int wd = 540;
    private static int cellHt = 60;
    private static int dblCellWd = 100;
    private static int buttonWidth = 200;

    /**
     * Constructor
     * @param wd width of the scene
     * @param ht height of the scene
     */
    public SceneCntl(int wd, int ht)
    {
        SceneCntl.wd = wd;
        SceneCntl.ht = ht;
    }
    /**
     * sets the height of the scene
     * @param ht 
     */
    public static void setHt(int ht)
    {
        ht = ht;
    }
    
    /**
     * sets the width of the scene
     * @param wd 
     */
    public static void setWd(int wd)
    {
        wd = wd;
    }

    public static void setButtonWidth( int wd ) { buttonWidth = wd; }
    
    /**
     * returns the height of the scend
     * @return ht
     */
    public static int getHt()
    {
        return ht;
    }

    /**
     * returns width
     * @return width of the scene
     */
    public static int getWd()
    {
        return wd;
    }

    public static int getCellHt() { return cellHt; }

    public static int getDblCellWd() { return dblCellWd; }

    public static int getButtonWidth() { return buttonWidth; }
}
