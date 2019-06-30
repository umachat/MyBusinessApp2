package com.mybusinessapp.example.madhu.mybusinessapp2.reports;


import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Reads .xslx file and converts into vo objects using reflection.
 */
public class ExcelRead {

    public static final String TAG = "ExcelRead";
    private String fileName= "D:/2019-04-25_PADump.xlsx";
    private final String packagePrefix = "com.mybusinessapp.example.madhu.mybusinessapp2.reports.";

    private List<Object> clients;
    private List<Object> categories;
    private List<Object> orders;
    private List<Object> payments;

    public List<Object> getClients() {
        return clients;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public List<Object> getPayments() {
        return payments;
    }


    public void startReading() {
        FileInputStream input= null;
        try{
            // Creating Input Stream
            File file = new File(fileName);
            input = new FileInputStream(file);

            XSSFWorkbook workBook = null;
            // Create a workbook using the File System
            workBook = (XSSFWorkbook) WorkbookFactory.create(input);

            clients = readData(workBook,0);
            categories = readData(workBook,1);
            orders = readData(workBook,2);
            payments = readData(workBook,3);

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Iterates through worksheets of an excel workbook. Also further iterates through rows and columns of the worksheet.
     * @param workBook
     * @param index
     * @return
     */
    private List<Object> readData(XSSFWorkbook workBook, int index) {

        List<Object> list = new ArrayList<>();

        // Get the first sheet from workbook
        XSSFSheet mySheet = workBook.getSheetAt(index);
        if(!mySheet.getProtect()) {
            System.out.println("Read failed as data may be corrupt");
            return null;
        }
        String clsName = mySheet.getSheetName();
        clsName = getCamelCase(clsName);
        System.out.println(clsName);
        Class cls = null;
        Object obj = null;
        try {
            cls = Class.forName(packagePrefix + clsName);
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(obj);


        /** We now need something to iterate through the cells.**/
        Iterator rowIter = mySheet.rowIterator();
        List<String> headers = new ArrayList<String>();

        //get the header
        if(rowIter.hasNext()) {
            XSSFRow myRow = (XSSFRow) rowIter.next();
            Iterator cellIter = myRow.cellIterator();

            while(cellIter.hasNext()){
                XSSFCell myCell = (XSSFCell) cellIter.next();
//                    Log.d(TAG, "Cell Value: " +  myCell.toString());
                String value = myCell.toString();
                headers.add(getFieldName(value));
            }
        }
        System.out.println(headers);

        //get the values
        while(rowIter.hasNext()){
            XSSFRow myRow = (XSSFRow) rowIter.next();
            Iterator cellIter = myRow.cellIterator();
            Map<String,String> mapOfValues = new HashMap<>();
            int i = 0;
            while(cellIter.hasNext()){
                XSSFCell myCell = (XSSFCell) cellIter.next();
                //System.out.println("Cell Value: " +  myCell.toString());
                String value = myCell.toString();
                mapOfValues.put(headers.get(i),value);
                i++;
            }
            try {
                buildObject(obj,mapOfValues);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            //System.out.println(((PaymentEntity)obj).getPayment_Date());
            list.add(obj);
        }
        return list;
    }


    /**
     * Coverts given string into camel case.
     * @param clsName
     * @return
     */
    private  String getCamelCase(String clsName) {
        String c = clsName.substring(0,1).toUpperCase();
        String d = clsName.substring(1,clsName.length()).toLowerCase();
        clsName = c.concat(d);
        System.out.println(clsName);
        return clsName;
    }

    /**
     * accepts _ concatenated string and transfoms into camel case string.
     * @param name
     * @return
     */
    private  String getFieldName(String name) {
        String[] arr =  name.split("_");
        String fieldName = "";
        for(int i=0; i<arr.length; i++) {
            if(i==0) {
                fieldName = fieldName.concat(arr[i].toLowerCase());
            } else {
                fieldName = fieldName.concat("_").concat(getCamelCase(arr[i]));
            }
        }
        System.out.println("fieldName is "+fieldName);
        return fieldName;
    }

    /**
     * accepts values read in from excel and prepares a vo object.
     * @param obj
     * @param mapOfValues
     * @throws IllegalAccessException
     */
    private  void buildObject(Object obj, Map<String,String> mapOfValues) throws IllegalAccessException{
       // System.out.println("------" +mapOfValues);
        //Get all the fields of the Class of object
        Field fields[] = obj.getClass().getDeclaredFields();

        //Iterate through each field
        for(Field field: fields){

            //Check is the filed is in map as a key
            if(mapOfValues.containsKey(field.getName())){

                //Get the field name
                String fieldValue = mapOfValues.get(field.getName());

                //Get the type
                String type = field.getType().getSimpleName();

                //Making it accessible allows to see private stuff. By default
                //java compile time rules are applied.
                field.setAccessible(true);

                //Base on type set the field value
                if(type.equalsIgnoreCase("String")){
                    field.set(obj, fieldValue);
                }else if(type.equalsIgnoreCase("Long") ||
                        type.equalsIgnoreCase("long")){
                    field.set(obj, Long.valueOf(fieldValue));
                }else if(type.equalsIgnoreCase("Integer") ||
                        type.equalsIgnoreCase("int")){
                    field.set(obj, Integer.valueOf(fieldValue));
                }else if(type.equalsIgnoreCase("Double") ||
                        type.equalsIgnoreCase("double")){
                    field.set(obj, Double.valueOf(fieldValue));
                }else if(type.equalsIgnoreCase("Float") ||
                        type.equalsIgnoreCase("float")){
                    field.set(obj, Float.valueOf(fieldValue));
                }else if(type.equalsIgnoreCase("Boolean") ||
                        type.equalsIgnoreCase("boolean")){
                    field.set(obj, Boolean.valueOf(fieldValue));
                }else if(type.equalsIgnoreCase("Date") ||
                        type.equalsIgnoreCase("date")){
                    try {
                        //System.out.println("++++++++++ "+fieldValue);
                        field.set(obj, AppConstants.DISPLAY_DATE_FORMATTER.parse(fieldValue));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ExcelRead util = new ExcelRead();
        util.startReading();
    }

}
