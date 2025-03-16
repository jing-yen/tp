package paypals.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.SerializablePermission;
import java.util.ArrayList;
import java.util.Scanner;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPals;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class Storage {
    private static final String SEPARATOR = String.valueOf(Character.toChars(31));
    private static final String SAVE_FILE_STRING = "savefile.txt";
    private static final String storageFolderPath = "./data";

    private String activityFilePath;
    private File activityFile;
    private ActivityManager activityManager;
    private Scanner scanner;

    public Storage(ActivityManager activityManager) throws PayPalsException{
        File dir = new File(storageFolderPath);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new PayPalsException(ExceptionMessage.STORAGE_DIR_NOT_CREATED);
            }
        }
        this.activityFilePath = storageFolderPath + "/" + SAVE_FILE_STRING;
        this.activityFile = new File(activityFilePath);
        try {
            activityFile.createNewFile();
        } catch (IOException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_CREATED);
        }
        this.activityManager = activityManager;
        try {
            this.scanner = new Scanner(this.activityFile);
        } catch (FileNotFoundException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_FOUND);
        }
    }

    public void save() throws PayPalsException {
        ArrayList<Activity> activities = activityManager.getActivityList();
        try {
            FileWriter fw = new FileWriter(this.activityFile);
            for (Activity activity : activities) {
                String data = activity.toStorageString(SEPARATOR);
                fw.write(data + "\n");
            }
        } catch (IOException e) {
            throw new PayPalsException(ExceptionMessage.SAVE_NOT_WRITTEN);
        }
    }

    public void load() throws PayPalsException {
        while (scanner.hasNextLine()) {

        }
    }
}
