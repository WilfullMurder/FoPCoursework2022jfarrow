/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.bradford.cookgame;

import java.util.ArrayList;
import java.util.Random;
import uk.ac.bradford.cookgame.GameEngine.TileType;


/**
 *
 * @author Wilfu
 */
public class Level 
{
    private final int _playWidth;
    private final int _playHeight;
    private int currentLevelIndex;
    

    Tilemap tilemaps; 
    
    
    public Level(int width, int height, int levelIndex)
    {
        _playWidth = width;
        _playHeight = height;
        currentLevelIndex = levelIndex;

        tilemaps = new Tilemap(width, height, levelIndex);
        
        
        

        
    }
    
    public TileType[][] genLevel(int levelIndex)
    {
        int mapIndex = levelIndex;
        int[][] map = tilemaps.getTileMap(mapIndex);
        if (map == null) {map = tilemaps.getTileMap(0);}

        TileType[][] level = new TileType[_playWidth][_playHeight];
      
        int floorSwitch = getRandomInt(0, 1);
        for(int i =0; i < _playWidth; i++)
        {
            for (int j = 0; j<_playHeight; j++)
            {
                if (level[i][j] == null)
                {
                    switch(tilemaps.getTile(j, i))
                    {
                        case 0:
                        default:
                            level[i][j] = TileType.WALL;
                            break;
                        case 1:

                            
                            if( floorSwitch == 0)
                            {
                                level[i][j] = TileType.FLOOR1;
                            }
                            else
                            {
                                level[i][j] = TileType.FLOOR2;
                            }
                            break;
                        
                        case 2:
                            level[i][j] = TileType.DOOR;
                            
                            
                    }
                }
            }
        }
        
        return level;
    }
    
    private ArrayList<String> initLevels(ArrayList<String> level)
    {
        //here we need to create level tilemaps
                level.add("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        for (int i = 0; i < 10; i++)
        {
            level.add("W..............................W");
        }
        level.add("WWWWWWWWWWWWWWWWWWWWWWWWWWWWDWWW");
        
        return level;
    }
    
    private void populateLevel(int numObstacles, int numCustomers)
    {
        //this needs to add obstacles and customers to the level
    }
    
  
    private int getRandomInt(int min, int max)
    {
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }
    
}
