/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.bradford.cookgame;

import java.awt.Point;
import java.util.ArrayList;
import uk.ac.bradford.cookgame.GameEngine.TileType;

/**
 *
 * @author Jfarrow
 */
public class Level 
{
    private final int _playWidth;
    private final int _playHeight;
    private int currentLevelIndex;
    private int currentLevelNum;
    private final GameEngine gEngine;
    private Player player;
    private final double _difficulty;
    private ArrayList<Point> _spawnLocs;
    private Point _playerSpawnLoc;
    private int _mapIndex;
    private Tilemap tilemaps;
    private TileType[][] layout;
    private int customerCount;
    

   
    public Level(int w, int h, int levelNum, GameEngine e)
    {
        _difficulty = calculateDiff(levelNum);
        _playWidth = w;
        _playHeight = h;
        currentLevelNum = levelNum;
        gEngine = e;
        
        
        tilemaps = new Tilemap(w, h, levelNum);
        layout = genMap(tilemaps);
        
        
        
        
    }
    /**
     * calculates a difficulty modifier f(x) = log10(x+1) 
     * @param levelNum the current level number
     * @return the difficulty modifier
     */
    private double calculateDiff(int levelNum)
    {
        return Math.log10(levelNum+1);      
    }
    
     /**
     * INCLUSIVE!!
     * @param min smallest desired return value
     * @param max largest desired return value
     * @return random int between min-max (inclusive)
     */
    public int getRandomInt(int min, int max)
    {
        return (int) (Math.random() * (max+1))| min;
    }
    
    
    /**
     * Generates new 2d tile array from tile map
     * @param tm tile map seed
     * @return 2d tile array ~ layout
     */
    private TileType[][] genMap(Tilemap tm)
    {
        currentLevelIndex = getRandomInt(0, tilemaps.getSize());
        TileType[][] l = new TileType[_playWidth][_playHeight];
        int floorSwitch = 0;
        
        if (currentLevelIndex > 0)
        {
           floorSwitch = getRandomInt(0, 1);
        }
        
        for (int i = 0; i < _playWidth; i++)
        {
            for (int j = 0; j < _playHeight; j++)
            {
                if (l[i][j] == null)
                {
                    switch(tm.getTile(j, i))
                    {
                        case 0:
                            l[i][j] = TileType.WALL;
                            break;
                            
                        case 1:
                        default:
                            if(floorSwitch < 1)
                            {
                                l[i][j] = TileType.FLOOR1;
                            }
                            else
                            {
                                l[i][j] = TileType.FLOOR2;
                            }
                            break;
                        
                        case 2:
                            l[i][j] = TileType.DOOR;
                            
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
                                 _playerSpawnLoc = new Point(i, j-1);
                             }
                             break;
                        case 3:
                            l[i][j] = TileType.FOOD1;
                            break;
                        case 4:
                            l[i][j] = TileType.FOOD2;
                            break;
                        case 5:
                            l[i][j] = TileType.FOOD3;  
                            
                            
                        
                    }
                }
            }
        }
        l = spawnObstacles(l);
            
        
        return l;
    }
    
    public TileType[][] getLayout()
    {
        return layout;
    }
    
    /**
     * populates the layout with table tiles
     * @param L the 2d tile array ~ layout
     * @return the updated 2d array
     */
    private TileType[][] spawnObstacles(TileType[][] L)
    {
        _spawnLocs = new ArrayList<Point>();
        customerCount = calculateCustomerCount();
        int tableCount = (int) (customerCount * 4);
       for (int i = 1; i < _playWidth-1; i++)
       {
           for(int j = 1; j< _playHeight-1; j++)
           {
               if(L[i][j] == TileType.FLOOR1 || L[i][j] == TileType.FLOOR2)
               {
                   if(checkSpaceForTable(L,i,j) == true && tableCount > 0)
                   {
                       int r = getRandomInt(0, 100);
                       
                       if(r <= 10)
                       {
                           if(L[i-1][j] == TileType.WALL || L[i+1][j] == TileType.WALL)
                           {
                               continue;
                           }
                            L[i][j] = TileType.TABLE;
                            tableCount--;
                       }
                       else
                       {
                           generateGenericSpawnPoints(L, customerCount, i,j);

                       }
                   }
                   //move this into customer spawning func
                   else
                   {
                       
                       generateGenericSpawnPoints(L, customerCount, i,j);

                   }
               }
               
               
           }
       }
       return L;
    }
    
    private void generateGenericSpawnPoints(TileType[][] L, int count, int i, int j)
    {
        if(count > 0)
        {
            Point p = new Point(i,j);
            if(p.getX() != _playerSpawnLoc.getLocation().x && p.getY() != _playerSpawnLoc.y)
            {
                if(L[i-1][j] == TileType.DOOR || L[i-1][j] == TileType.FOOD1 || L[i-1][j] == TileType.FOOD2 ||L[i-1][j] == TileType.FOOD3)
                {
                    return;
                }
                if(L[i+1][j] == TileType.DOOR || L[i+1][j] == TileType.FOOD1 || L[i+1][j] == TileType.FOOD2 ||L[i+1][j] == TileType.FOOD3)
                {
                    return;
                }
                if(L[i][j-1] == TileType.DOOR || L[i][j-1] == TileType.FOOD1 || L[i][j-1] == TileType.FOOD2 ||L[i][j-1] == TileType.FOOD3)
                {
                    return;
                }
                if(L[i][j+1] == TileType.DOOR || L[i][j+1] == TileType.FOOD1 || L[i][j+1] == TileType.FOOD2 ||L[i][j+1] == TileType.FOOD3)
                {
                    return;
                }
                _spawnLocs.add(new Point(i,j));
            }
        }
    }
    
    /**
     * checks adjacent tiles to ensure a table spawns without blocking other tiles (like the door)
     * @param L the 2d array of tiles ~ layout
     * @param i the current x position
     * @param j the current y position
     * @return defaults true, returns false on FOOD, DOOR and, TABLE(X-axis only)
     */
    private boolean checkSpaceForTable(TileType[][] L, int i, int j)
    {
        if(i > 0)
        {
            if(L[i-1][j] == TileType.DOOR || (L[i-1][j] == TileType.FOOD1 
                    || L[i-1][j] == TileType.FOOD2 || L[i-1][j] == TileType.FOOD3) 
                    || L[i-1][j] == TileType.TABLE)
            {
                return false;
            }
        }
        if (i < _playWidth)
        {
            if(L[i+1][j] == TileType.DOOR || (L[i+1][j] == TileType.FOOD1 
                    || L[i+1][j] == TileType.FOOD2 || L[i+1][j] == TileType.FOOD3)
                    || L[i+1][j] == TileType.TABLE)
            {
                return false;
            }
        }
        if(j > 0)
        {
            if(L[i][j-1] == TileType.DOOR || (L[i][j-1] == TileType.FOOD1 
                    || L[i][j-1] == TileType.FOOD2 || L[i][j-1] == TileType.FOOD3))
            {
                return false;
            }
        }
        if(j < _playHeight)
        {
            if(L[i][j+1] == TileType.DOOR || (L[i][j+1] == TileType.FOOD1 
                    || L[i][j+1] == TileType.FOOD2 || L[i][j+1] == TileType.FOOD3))
            {
                return false;
            }
        }
        return true;
    }
    
    
    
    private int calculateCustomerCount()
    {   //Wtf am i doing?
        //Trying to calculate a customer count from the level number
        // 0 < customer count <= N 
        // where N = f(10), f(x) = (x+1)^1.25
        
        // Using the modulus means x = 0 values on multiples of 10, etc.
        // want mod == 0 == f(10) && !mod==0 == f(0)
        // want mod == 9 == f(1< x>= 3) after level 0
        // 
 
        int mod = currentLevelNum;
        if(currentLevelNum > 10)
        {
            mod = currentLevelNum % 10;
            if(mod == 0)
            {
                mod = 10;
            }
        }
        return (int) Math.pow(mod+1, 1.25);
    }
    
    
    public ArrayList<Point> getGenericSpawnLocs()
    {
        return _spawnLocs;
    }
    
    
    private Point GetPlayerSpawnLoc()
    {
        return _playerSpawnLoc;
    }
    
    public int getPlayerSpawnX()
    {
        return GetPlayerSpawnLoc().x;
    }
    
    public int getPlayerSpawnY()
    {
        return GetPlayerSpawnLoc().y;
    }
    
    public double getDiff()
    {
      return _difficulty;          
    }
    
    public int getCustomerCount()
    {
        return customerCount;
    }
    
    public void decrementCustomerCount()
    {
        customerCount--;
    }
}
