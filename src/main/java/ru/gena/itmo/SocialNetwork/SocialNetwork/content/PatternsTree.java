package ru.gena.itmo.SocialNetwork.SocialNetwork.content;

import java.util.ArrayList;

public class PatternsTree {
    private ArrayList<ArrayList<TreesElement>> descendants;
    private int numberOfElements;

    public PatternsTree(){
        numberOfElements = 0;
        descendants = new ArrayList<>();
    }
    public void addLine(){
        descendants.add(new ArrayList<>());
        int nomEle = descendants.size() - 1;
        descendants.get(nomEle)
                .add(new TreesElement(nomEle, "", 50, 0) );
    }
    public void addValueInLine(int numberLine, int value, String name){
        descendants.get(numberLine).add(new TreesElement(value, name,0, 0) );
        numberOfElements = (numberOfElements < value)? value: numberOfElements;
    }
    public ArrayList<TreesElement> getLine(int numberLine){
        return descendants.get(numberLine);
    }
    public int getNumberOfElements(){
        return numberOfElements + 1;
    }
    public int getNumberOfLines(){ return descendants.size(); }
}
