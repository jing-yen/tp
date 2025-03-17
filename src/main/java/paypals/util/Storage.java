package paypals.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class Storage {
    private static final String SEPARATOR = String.valueOf(Character.toChars(31));
    private static final String SAVE_FILE_STRING = "savefile.txt";
    private static final String storageFolderPath = "./data";

    private final File activityFile;
    private final Scanner scanner;

    public Storage() throws PayPalsException{
        File dir = new File(storageFolderPath);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new PayPalsException(ExceptionMessage.STORAGE_DIR_NOT_CREATED);
            }
        }
        assert dir.exists() && dir.isDirectory() : "The storage directory exists and is a directory";

        String activityFilePath = storageFolderPath + "/" + SAVE_FILE_STRING;
        this.activityFile = new File(activityFilePath);
        try {
            activityFile.createNewFile();
        } catch (IOException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_CREATED);
        }
        assert activityFile.exists() && activityFile.isFile() : "The activity file exists and should be a file";

        try {
            this.scanner = new Scanner(this.activityFile);
        } catch (FileNotFoundException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_FOUND);
        }
    }

    public void save(ActivityManager activityManager) throws PayPalsException {
        ArrayList<Activity> activities = activityManager.getActivityList();
        try {
            FileWriter fw = new FileWriter(this.activityFile);
            for (Activity activity : activities) {
                String data = activity.toStorageString(SEPARATOR);
                fw.write(data + "\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new PayPalsException(ExceptionMessage.SAVE_NOT_WRITTEN);
        }
    }

    public void load(ActivityManager activityManager) throws PayPalsException {
        while (scanner.hasNextLine()) {
            try {
                String data = scanner.nextLine();
                String[] parts = data.split(SEPARATOR);
                if (parts.length < 4) {
                    throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
                }
                assert ((parts.length - 1) % 3) == 0 : "Parts should contain full data of each Person";
                String description = parts[0];
                Person payer = new Person(parts[1], Double.parseDouble(parts[2]), Boolean.parseBoolean(parts[3]));
                HashMap<String, Person> owed = new HashMap<>();
                for (int i = 4; i < parts.length; i += 3) {
                    if (i + 2 >= parts.length) {
                        throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
                    }
                    String owedName = parts[i];
                    double owedAmount = Double.parseDouble(parts[i + 1]);
                    boolean owedHasPaid = Boolean.parseBoolean(parts[i + 2]);
                    Person owedPerson = new Person(owedName, owedAmount, owedHasPaid);
                    owed.put(owedName, owedPerson);
                }
                Activity activity = new Activity(description, payer, new HashMap<>());
                activity.setOwed(owed);
                activityManager.addActivity(activity);
            } catch (PayPalsException e) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
            }
        }
    }

    public void deleteDir(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDir(file);
            }
        }
        dir.delete();
    }
}
