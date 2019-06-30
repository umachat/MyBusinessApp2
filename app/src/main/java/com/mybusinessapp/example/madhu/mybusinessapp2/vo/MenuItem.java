package com.mybusinessapp.example.madhu.mybusinessapp2.vo;

public class MenuItem {
    private int Id;
    private String name;

    public MenuItem(int Id, String name) {
        this.name = name;
        this.Id= Id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return Id;
    }

}