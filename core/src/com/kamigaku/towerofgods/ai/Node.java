package com.kamigaku.towerofgods.ai;

public class Node {

    public int x;
    public int y;
    public Node parent;
    public boolean racine;
    
    public Node(int x, int y, Node parent, boolean racine) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.racine = racine;
    }
    
    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.racine = false;        
    }   
}