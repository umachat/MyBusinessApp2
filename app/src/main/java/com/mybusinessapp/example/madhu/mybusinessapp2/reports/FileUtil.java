package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Iterator;

/**
 * Creates a file and saves to internal storage as well as external storage Documents folder.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    private String fileName = ReportConstants.FILE_NAME;

    public  void saveExcelFile(Context context) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            Toast.makeText(context, "Storage not available or read only",Toast.LENGTH_SHORT).show();
            return;
        }


        //append current date to fileName
        String prefix = ReportConstants.DATE_FORMAT.format(new Date());
        fileName = prefix.concat("_").concat(fileName);

        //saves to internal storage
        File fileInternal = new File(context.getExternalFilesDir(null), fileName);
        if(!writeFile(context,fileInternal)) {
            Log.e(TAG, "Unable to write file to internal storage!");
            Toast.makeText(context, "Unable to write file to internal storage!",Toast.LENGTH_SHORT).show();
        }

        // get the path to sdcard  external storage
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + ReportConstants.DIR_NAME);
        // create this directory if not already created
        if(!dir.exists()) {
            dir.mkdir();
        }
        // create the file in which we will write the contents
        File fileExternal = new File(dir, fileName);

        if(!writeFile(context,fileExternal)) {
            Log.e(TAG, "Unable to write file to external storage!");
            Toast.makeText(context, "Unable to write file to external storage!",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean writeFile(Context context,File file) {

        boolean success = false;

        try {
            Log.w("FileUtils", "Writing file" + file);
            Toast.makeText(context, "Writing file",Toast.LENGTH_SHORT).show();
            ExcelWriter utils = new ExcelWriter();
            utils.testWriteToExcelInMultiSheets(context,file);

            success = true;

        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            Toast.makeText(context, "Failed to save file",Toast.LENGTH_SHORT).show();
        } finally {

        }
        return success;
    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    public void readExcelFile(Context context) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e(TAG, "Storage not available or read only");
            Toast.makeText(context, "Storage not available or read only",Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            // Creating Input Stream
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.d(TAG, "Cell Value: " +  myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){e.printStackTrace(); }

        return;
    }



}
