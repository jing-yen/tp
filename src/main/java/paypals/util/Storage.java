package paypals.util;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.PaidCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final String SEPARATOR = String.valueOf(Character.toChars(31));
    private static final String MASTER_FILE_STRING = "master-savefile.txt";
    private static final String FILE_EXTENSION = ".txt";
    private static final String STORAGE_FOLDER_PATH = "./data";

    private final File masterFile;
    private File activityFile;
    private Scanner scanner;

    private final ArrayList<String> groupNames;

    public Storage() throws PayPalsException {
        File dir = new File(STORAGE_FOLDER_PATH);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new PayPalsException(ExceptionMessage.STORAGE_DIR_NOT_CREATED);
            }
        }
        assert dir.exists() && dir.isDirectory() : "The storage directory exists and is a directory";

        // Reads the master file, containing the names of Paypals groups
        String masterFilePath = STORAGE_FOLDER_PATH + "/" + MASTER_FILE_STRING;
        this.masterFile = new File(masterFilePath);
        try {
            masterFile.createNewFile();
        } catch (IOException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_CREATED);
        }
        assert masterFile.exists() && masterFile.isFile() : "The master file exists and should be a file";

        try {
            this.scanner = new Scanner(this.masterFile);
        } catch (FileNotFoundException e) {
            throw new PayPalsException(ExceptionMessage.STORAGE_FILE_NOT_FOUND);
        }

        groupNames = new ArrayList<>();
        while (scanner.hasNextLine()) {
            groupNames.add(scanner.nextLine());
        }
        scanner.close();
    }

    /**
     * Saves the list of activities from the provided ActivityManager into a storage file.
     * Each activity is converted to a storage-friendly string format and written to the file.
     *
     * @param activityManager the ActivityManager instance containing the list of activities to save
     * @throws PayPalsException if an I/O error occurs while writing to the storage file,
     *                          with the message {@link ExceptionMessage#SAVE_NOT_WRITTEN}
     */
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

    /**
     * Checks if a given filename is valid by attempting to create a corresponding file in the storage folder.
     * If the file name is valid but does not exist, it is created.
     *
     * @param fileName the name of the file to validate (without the file extension)
     * @return true if the file can be successfully created.
     */
    public boolean checkIfFilenameValid(String fileName) throws PayPalsException {
        if (fileName.isBlank()) {
            throw new PayPalsException(ExceptionMessage.EMPTY_FILENAME);
        }
        if (fileName.contains("/")) {
            throw new PayPalsException(ExceptionMessage.INVALID_FILENAME);
        }
        try {
            int testNumber = Integer.parseInt(fileName);
            if (testNumber <= 0 || testNumber > groupNames.size()) {
                throw new PayPalsException(ExceptionMessage.INVALID_GROUP_NUMBER);
            }
            return true;
        } catch (NumberFormatException e) {
            String testFileName = fileName + ".txt";
            String testFilePath = STORAGE_FOLDER_PATH + "/" + testFileName;
            try {
                File testFile = new File(testFilePath);
                testFile.createNewFile();
            } catch (IOException error) {
                throw new PayPalsException(ExceptionMessage.INVALID_FILENAME);
            }

            // Check if file exists with correct filename. Sometimes invalid names are truncated.
            File folder = new File(STORAGE_FOLDER_PATH);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.getName().equals(testFileName)) {
                    return true;
                }
            }
            throw new PayPalsException(ExceptionMessage.FILENAME_DOES_NOT_EXIST);
        }
    }

    /**
     * Loads activity data for a specific group into the provided ActivityManager.
     * If the group does not exist, it creates a new group with the given name or number.
     *
     * @param groupNumberOrName the name or number of the group to load
     * @param activityManager   the ActivityManager instance where the loaded data will be stored
     * @throws PayPalsException if:
     *                          - the group number cannot be parsed as an integer and is not a valid group name
     *                          - an I/O error occurs while appending to the master file or loading the group file
     *                            with the messages {@link ExceptionMessage#SAVE_NOT_WRITTEN} or
     *                            {@link ExceptionMessage#STORAGE_FILE_NOT_FOUND}
     */
    public void load(String groupNumberOrName, ActivityManager activityManager) throws PayPalsException {
        String groupName;
        try {
            if (groupNames.contains(groupNumberOrName)) {
                groupName = groupNumberOrName;
            } else {
                int groupNumber = Integer.parseInt(groupNumberOrName);
                groupName = groupNames.get(groupNumber - 1);
            }
            activityManager.setGroupDetails(groupName, false);
        } catch (NumberFormatException e) {
            // Create a new group with the group name
            groupName = groupNumberOrName;
            groupNames.add(groupName);
            try {
                FileWriter fw = new FileWriter(this.masterFile, true);
                fw.append(groupName + "\n");
                fw.close();
            } catch (IOException ee) {
                throw new PayPalsException(ExceptionMessage.SAVE_NOT_WRITTEN);
            }
            activityManager.setGroupDetails(groupName, true);
        }
        loadFromGroupName(groupName, activityManager);
    }

    private void loadFromGroupName(String groupName, ActivityManager activityManager) throws PayPalsException {
        // Reads the specific group file selected
        String activityFilePath = STORAGE_FOLDER_PATH + "/" + groupName + FILE_EXTENSION;
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

        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            try {
                processLine(data, activityManager);
            } catch (PayPalsException e) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
            }
            if (activityManager.getSize() >= 1000) {
                throw new PayPalsException(ExceptionMessage.MORE_THAN_1000_ACTIVITIES);
            }
        }
        scanner.close();
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
            } else if (!parts[i + 2].equals("false")) {
                throw new PayPalsException(ExceptionMessage.LOAD_ERROR);
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

    /**
     * Recursively deletes a directory and all its contents.
     *
     * <p>This method traverses the directory structure,
     * deleting all files and subdirectories within the given directory.
     * After all contents are deleted, the directory itself is removed.
     * If the directory is already empty, it is deleted directly.</p>
     *
     * @param dir the directory to be deleted
     */
    public void deleteDir(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDir(file);
            }
        }
        dir.delete();
    }

    public ArrayList<String> getGroupNames() {
        return groupNames;
    }

    /**
     * Deletes a group from the system by removing its file and updating the master file.
     * The group can be identified either by its name or by its position number in the list.
     *
     * @param groupNumberOrName The name of the group or its position number (1-based index) in the list
     * @param activityManager The activity manager instance, used for any cleanup related to the group
     * @throws PayPalsException If an error occurs during file deletion or master file updating,
     *                          or if the group number is invalid
     */
    public void delete(String groupNumberOrName, ActivityManager activityManager) throws PayPalsException {
        String groupName;
        if (groupNames.contains(groupNumberOrName)) {
            groupName = groupNumberOrName;
        } else {
            int groupNumber = Integer.parseInt(groupNumberOrName);
            groupName = groupNames.get(groupNumber - 1);
        }
        deleteFile(groupName);
        groupNames.remove(groupName);
        try {
            removeFromMasterFile(groupName);
        } catch (IOException ee) {
            throw new PayPalsException(ExceptionMessage.SAVE_NOT_WRITTEN);
        }
    }

    /**
     * Removes a group's entry from the master file by reading the entire file,
     * filtering out the line containing the group name, and writing the filtered content back.
     *
     * @param groupName The name of the group to be removed from the master file
     * @throws IOException If an error occurs during file reading or writing operations
     */
    private void removeFromMasterFile(String groupName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.masterFile));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            // Skip the line containing the group name
            if (!line.contains(groupName)) {
                content.append(line).append(System.lineSeparator());
            }
        }
        reader.close();

        // Write the filtered content back to the master file
        FileWriter fw = new FileWriter(this.masterFile, false); // false means overwrite
        fw.write(content.toString());
        fw.close();
    }

    /**
     * Deletes the file associated with a group from the storage location.
     *
     * @param groupName The name of the group whose file should be deleted
     */
    private void deleteFile(String groupName) {
        String groupFilePath = STORAGE_FOLDER_PATH + "/" + groupName + FILE_EXTENSION;
        File file = new File(groupFilePath);
        file.delete();
    }

    /**
     * Checks if a group exists in the system by its name or position number.
     * When a number is provided, this method verifies that it's a valid index
     * within the range of existing groups.
     *
     * @param groupNumberOrName The name of the group or its position number (1-based index) in the list
     * @return true if the group exists or the provided number is valid
     * @throws PayPalsException If the provided string is neither a valid group name nor a valid number,
     *                          or if the number is out of range
     */
    public boolean containsFile(String groupNumberOrName) throws PayPalsException {
        if (groupNames.contains(groupNumberOrName)) {
            return true;
        }
        int groupNumber;
        try {
            groupNumber = Integer.parseInt(groupNumberOrName);
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.FILENAME_DOES_NOT_EXIST);
        }
        if (groupNumber <= 0 || groupNumber > groupNames.size()) {
            throw new PayPalsException(ExceptionMessage.INVALID_GROUP_NUMBER);
        }
        return true;
    }
}
