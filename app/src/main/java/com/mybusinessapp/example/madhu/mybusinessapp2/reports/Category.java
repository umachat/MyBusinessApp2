package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

/**
 * The OrderEntity CategoryEntity VO used in excel report.
 */
public class Category extends ReportVO {

    private String category_Id;
    private String category_Name;

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory_Name() {
        return category_Name;
    }

    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }
}
