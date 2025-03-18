package paypals.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.PaidCommand;
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
        int activityIdentifier = 1;
        while (scanner.hasNextLine()) {
            try {
                ArrayList<String> hasPaidNames = new ArrayList<>();
                String data = scanner.nextLine();
                String[] parts = data.split(SEPARATOR);
                if (parts.length < 4) {
                    throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
                }
                assert ((parts.length - 1) % 3) == 0 : "Parts should contain full data of each Person";
                String description = "d/" + parts[0] + " ";
                String payer = "n/" + parts[1] + " ";
                String input = description + payer;
                for (int i = 4; i < parts.length; i += 3) {
                    if (i + 2 >= parts.length) {
                        throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
                    }
                    String owedName = "f/ " + parts[i] + " ";
                    String owedAmount = "a/ " + parts[i + 1] + " ";
                    boolean hasPaid = Boolean.parseBoolean(parts[i + 2]);
                    if (hasPaid) {
                        hasPaidNames.add(parts[i]);
                    }
                    input += owedName + owedAmount;
                }
                Command c = new AddCommand(input);
                c.execute(activityManager, false);
                for (String name : hasPaidNames) {
                    String paidInput = "n/" + name + " i/" + activityIdentifier;
                    Command paidCommand = new PaidCommand(paidInput);
                    paidCommand.execute(activityManager, false);
                }
                activityIdentifier++;
            } catch (PayPalsException e) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
            }
        }
        Logging.logInfo("Data has been loaded from save file");
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
