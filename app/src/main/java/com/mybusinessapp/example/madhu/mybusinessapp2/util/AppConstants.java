package com.mybusinessapp.example.madhu.mybusinessapp2.util;

import java.text.SimpleDateFormat;

public class AppConstants {

   public static final String EXCEL_PASSWORD = "madshi";

   /******* DATE FORMATS *******/
   public static final SimpleDateFormat DISPLAY_DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
   public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

   /*********** REGEX *************/
   public static final String EMAIL_REGEX ="([A-Za-z0-9-_.]+@[A-Za-z0-9-_]+(?:\\.[A-Za-z0-9]+)+)";
   public static final String PHONE_REGEX ="\\d{10}|(?:\\d{3}-){2}\\d{4}";
           //"^[789]\\d{9}$";
   public static final String NUMERIC_REGEX = "^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";


   public static final int MAXIMUM_CATEGORIES_ALLOWED = 20;
}
