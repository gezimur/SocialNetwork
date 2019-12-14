package ru.gena.itmo.SocialNetwork.SocialNetwork.content;

public class Pattern {
    private int id;
    private String patternsName;
    private String siteswap;
    private String description;

    public Pattern(){
        id = 0;
        patternsName = "";
        siteswap = "";
        description = "";
    }
    public Pattern(int newId, String newPatternsName, String newSiteswap, String newDescription){
        id = newId;
        patternsName = newPatternsName;
        siteswap = newSiteswap;
        description = newDescription;
    }

    public int getId() {
        return id;
    }

    public String getPatternsName() {
        return patternsName;
    }

    public String getSiteswap() {
        return siteswap;
    }

    public String getDescription() {
        return description;
    }
}
