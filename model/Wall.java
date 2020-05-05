package model;

import utility.Vector2D;

public class Wall extends Obstacle{
    private static Wall instance = null;

    private Wall(){
        //private singleton instance
    }
    public static Wall getInstance(){
        if(instance == null){
            instance = new Wall();
        }
        return instance;
    }
}
