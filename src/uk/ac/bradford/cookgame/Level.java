/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.bradford.cookgame;

import java.awt.Point;
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
    private final int currentLevelIndex;
    private final GameEngine gEngine;
    private Player player;
    private final int _difficulty;
    private ArrayList<Point> _spawnLocs;
    private Point _playerSpawnLoc;

    private Tilemap tilemaps;
    private TileType[][] layout;
    
    
    public Level(int width, int height, int levelIndex, GameEngine engine)
    {
        _playWidth = width;
        _playHeight = height;
        currentLevelIndex = levelIndex;
        
        _spawnLocs = new ArrayList<Point>();
        
        gEngine = engine;

        tilemaps = new Tilemap(width, height, levelIndex);
        
        layout = genLevel(currentLevelIndex);
        
        
        
        if (levelIndex <= 100)
        {
            _difficulty = (int)(levelIndex + 1) / 10;
        }
        else
        {
            _difficulty = (int)(levelIndex + 1) / 25;
        }
    }
    
    /**
     * generate a new level of tile types
     * @param levelIndex the current level being played
     * @return A 2D array filled with TileType values representing the current game level
     */
    private TileType[][] genLevel(int levelIndex)
    {

        int mapIndex = levelIndex % tilemaps.getSize();
        int[][] map = tilemaps.getTileMap(mapIndex);
        if (map == null) {map = tilemaps.getTileMap(0);}

        TileType[][] level = new TileType[_playWidth][_playHeight];
        
        int floorSwitch = 0;
        if (levelIndex > 0)
        {
           floorSwitch = getRandomInt(0, 1);
        }
        
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

                            
                            if(floorSwitch == 1)
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
                             
                             if (i <=0)
                             {
                                 _playerSpawnLoc = new Point(i+1, j);
                             }
                             else if (i >= _playWidth - 1 )
                             {
                                 _playerSpawnLoc = new Point(i-1, j);
                             }
                             else if (j <= 0)
                             {
                                 _playerSpawnLoc = new Point(i, j+1);
                             }
                             else if(j >= _playHeight - 1)
                             {
                                 _playerSpawnLoc = new Point(i-1, j-1);
                             }
                             
                             
                             break;
                             
                        case 3:
                            level[i][j] = TileType.FOOD1;
                            break;
                        case 4:
                            level[i][j] = TileType.FOOD2;
                            break;
                        case 5:
                            level[i][j] = TileType.FOOD3;
                    }
                    level = populateLevel(i,j,level);
                }
            }
        }
        
        
        return level;
    }
    /**
     * populate level with obstacles.
     * @param i current i value 
     * @param j current j value
     * @param level current TileType[][]
     * @return updated 2d arr of tile types
     */
    private TileType[][] populateLevel(int i, int j, TileType[][] level)
    {
        if (level[i][j] == TileType.FLOOR1 || level[i][j] == TileType.FLOOR2)
        {
            int r = getRandomInt(0, 100);
        
            if (r <= _difficulty)
            {
                level[i][j] = TileType.TABLE;
            }
            else
            {
                _spawnLocs.add(new Point(i,j));
            }
          
        }
        
        
     
        
        return level;
    }
    
    /**
     * INCLUSIVE!!
     * @param min smallest desired return value
     * @param max largest desired return value
     * @return random int between min-max (inclusive)
     */
    private int getRandomInt(int min, int max)
    {
        return (int) (Math.random() * (max+1))| min;
    }
    
    public TileType[][] getLevelLayout()
    {
        return layout;
    }
    
    public TileType getTileAtLoc(int x, int y)
    {
        return getLevelLayout()[x][y];
    }
    
    
    public ArrayList<Point> getGenericSpawnLocs()
    {
        return _spawnLocs;
    }
    
    public Point getGenericSpawnPoint(int index)
    {
        return getGenericSpawnLocs().get(index);
    }
    
    public int getGenericSpawnPointX(int index)
    {
        return getGenericSpawnPoint(index).x;
    }
    
     public int getGenericSpawnPointY(int index)
    {
        return getGenericSpawnPoint(index).y;
    }
     
     private Point getPlayerSpawnLoc()
     {
         return _playerSpawnLoc;
     }
     
     public int getPlayerSpawnX()
     {
         return getPlayerSpawnLoc().x;
     }
     
     public int getPlayerSpawnY()
     {
         return getPlayerSpawnLoc().y;
     }
     
     
}
