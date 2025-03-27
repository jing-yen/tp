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
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            try {
                processLine(data, activityManager);
            } catch (PayPalsException e) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
            }
        }
        Logging.logInfo("Data has been loaded from save file");
    }

    /**
     * Processes a single line of input, constructs the necessary command input,
     * and executes the commands.
     *
     * @param data the line of data read from the file
     * @param activityManager the activity manager to operate on
     * @throws PayPalsException if the data format is invalid
     */
    private void processLine(String data, ActivityManager activityManager)
            throws PayPalsException {
        ArrayList<String> hasPaidNames = new ArrayList<>();
        String[] parts = data.split(SEPARATOR);
        if (parts.length < 4) {
            throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
        }
        assert ((parts.length - 1) % 3) == 0 : "Parts should contain full data of each Person";

        // Build the command input string and collect names with hasPaid true
        String input = buildInput(parts, hasPaidNames);

        // Execute the add command
        executeAddCommand(input, activityManager);

        // Execute additional commands for persons who have paid
        executePaidCommands(hasPaidNames, activityManager);
    }

    /**
     * Builds the command input string from the parsed parts and populates a list
     * of names for which the "hasPaid" flag is true.
     *
     * @param parts the array of strings obtained by splitting the line
     * @param hasPaidNames a list to be populated with names that have paid
     * @return the combined command input string
     * @throws PayPalsException if the parts array does not contain the expected data
     */
    private String buildInput(String[] parts, ArrayList<String> hasPaidNames) throws PayPalsException {
        String description = "d/" + parts[0] + " ";
        String payer = "n/" + parts[1] + " ";
        String input = description + payer;
        for (int i = 4; i < parts.length; i += 3) {
            if (i + 2 >= parts.length) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
            }
            String owedName = "f/" + parts[i] + " ";
            String owedAmount = "a/" + parts[i + 1] + " ";
            boolean hasPaid = Boolean.parseBoolean(parts[i + 2]);
            if (hasPaid) {
                hasPaidNames.add(parts[i]);
            }
            input += owedName + owedAmount;
        }
        return input;
    }

    /**
     * Creates and executes the add command based on the input string.
     *
     * @param input the command input string
     * @param activityManager the activity manager to operate on
     */
    private void executeAddCommand(String input, ActivityManager activityManager) {
        try {
            Command c = new AddCommand(input);
            c.execute(activityManager, false);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates and executes paid commands for each name in the list.
     *
     * @param hasPaidNames the list of names for which the hasPaid flag is true
     * @param activityManager the activity manager to operate on
     */
    private void executePaidCommands(ArrayList<String> hasPaidNames, ActivityManager activityManager) {
        try {
            for (String name : hasPaidNames) {
                Activity activity = activityManager.getActivity(activityManager.getSize()-1);
                int activityIdentifier = activityManager.getIdentifierFromUnpaidList(activity,name);
                String paidInput = "n/" + name + " i/" + activityIdentifier;
                Command paidCommand = new PaidCommand(paidInput);
                paidCommand.execute(activityManager, false);
            }
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
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
