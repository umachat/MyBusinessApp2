package com.mybusinessapp.example.madhu.mybusinessapp2.vo;

public class CategoryEntity {

    private int id;
    private String name;

    public CategoryEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//NOTE do not change toString of CategoryEntity as it is usd in autocomplete!!!
    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CategoryEntity){
            CategoryEntity c = (CategoryEntity)obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }
}
