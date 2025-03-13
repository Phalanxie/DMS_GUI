package com.swdprojects.romkaicode.dmsgui;

/*
    Roberto Ruiz,CEN3024C, 3/12/2025
    Software Development I
    Film: this class stores the film object attributes and getters, setters, and object constructor
* */
public class Film
{
    private int ID;
    private String name;
    private String description;
    private float price;
    private int releaseDate;
    private int runtime;

    public Film(int ID, String name, String description, float price, int releaseDate, int runtime)
    {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
    }
    public int getID()
    {
        return ID;
    }
    public void setID(int ID)
    {
        this.ID = ID;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price)
    {
        this.price = price;
    }
    public int getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(int releaseDate)
    {
        this.releaseDate = releaseDate;
    }
    public int getRuntime()
    {
        return runtime;
    }
    public void setRuntime(int runtime)
    {
        this.runtime = runtime;
    }

    @Override
    public String toString()
    {
        return "Film ID: " + ID +
                "\nName: " + name +
                "\nDescription: " + description +
                "\nPrice: $" + price +
                "\nRelease Date: " + releaseDate +
                "\nRuntime: " + runtime + " minutes\n";
    }
}