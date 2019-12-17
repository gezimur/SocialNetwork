package ru.gena.itmo.SocialNetwork.SocialNetwork.content;

import java.util.ArrayList;
import java.util.HashMap;

public class PatternsTree {
    private HashMap<Integer, ArrayList<TreesElement>> descendants;
    private int numberOfElements;

    public PatternsTree(){
        numberOfElements = 0;
        descendants = new HashMap<>();
    }
    public void addLine(int pattern){
        ArrayList<TreesElement> newLine = new ArrayList<>();
        newLine.add(new TreesElement(pattern, "", 50, 0));
        descendants.put(pattern, newLine);
    }
    public void addValueInLine(int pattern, int value, String name){
        descendants.get(pattern).add(new TreesElement(value, name,0, 0) );
        numberOfElements = (numberOfElements < value)? value : numberOfElements;
    }
    public boolean isThereThisP(int pattern){ return descendants.containsKey(pattern); }
    public ArrayList<TreesElement> getLine(int pattern){
        return descendants.get(pattern);
    }
    public int getNumberOfElements(){
        return numberOfElements + 1;
    }
    //public int getNumberOfLines(){ return descendants.size(); }
}
