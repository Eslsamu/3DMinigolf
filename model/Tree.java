package model;

public class Tree extends Obstacle {
    private static Tree instance = null;

    private Tree(){
        //private singleton instance
    }
    public static Tree getInstance(){
        if(instance == null){
            instance = new Tree();
        }
        return instance;
    }
}
