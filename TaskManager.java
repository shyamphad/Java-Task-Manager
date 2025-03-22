import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TaskManager - A simple console-based task management application
 * This program allows users to manage their tasks through a text interface.
 * Users can add, view, update, delete tasks and mark them as complete.
 * Tasks are saved to a file for persistence between program sessions.
 */
public class TaskManager {
    // Store all tasks in an ArrayList for easy management
    private static ArrayList<Task> taskList = new ArrayList<>();
    
    // File path for saving and loading tasks
    private static final String DATA_FILE_PATH = "tasks.txt";
    
    // Scanner for reading user input
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Main method - entry point of the program
     * Handles initialization and the main program loop
     */
    public static void main(String[] args) {
        // Load any existing tasks when program starts
        loadTasksFromFile();
        
        boolean running = true;
        
        // Main program loop - continue until user chooses to exit
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            
            // Process the user's menu selection
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks("all");
                    break;
                case 3:
                    viewTasks("completed");
                    break;
                case 4:
                    viewTasks("incomplete");
                    break;
                case 5:
                    markTaskComplete();
                    break;
                case 6:
                    deleteTask();
                    break;
                case 7:
                    // Save tasks and exit program
                    saveTasksToFile();
                    running = false;
                    System.out.println("Thank you for using TaskManager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        // Close the scanner when done
        scanner.close();
    }

    /**
     * Display the main menu options to the user
     */
    private static void displayMenu() {
        System.out.println("\n===== Task Manager =====");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. View Completed Tasks");
        System.out.println("4. View Incomplete Tasks");
        System.out.println("5. Mark Task as Complete");
        System.out.println("6. Delete Task");
        System.out.println("7. Save and Exit");
        System.out.println("=======================");
        System.out.print("Enter your choice (1-7): ");
    }

    /**
     * Get and validate the user's menu choice
     * @return validated integer choice
     */
    private static int getUserChoice() {
        int choice = 0;
        boolean validInput = false;
        
        // Loop until user provides valid input
        while (!validInput) {
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number (1-7): ");
            }
        }
        
        return choice;
    }

    /**
     * Add a new task based on user input
     */
    private static void addTask() {
        System.out.println("\n----- Add New Task -----");
        
        // Get task details from user
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        while (title.trim().isEmpty()) {
            System.out.println("Title cannot be empty.");
            System.out.print("Enter task title: ");
            title = scanner.nextLine();
        }
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        
        // Get and validate priority (1-5)
        int priority = 0;
        boolean validPriority = false;
        while (!validPriority) {
            System.out.print("Enter priority (1-5, where 5 is highest): ");
            try {
                priority = Integer.parseInt(scanner.nextLine());
                if (priority < 1 || priority > 5) {
                    System.out.println("Priority must be between 1 and 5.");
                } else {
                    validPriority = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        // Get and validate due date format
        String dueDate = "";
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Enter due date (MM/DD/YYYY): ");
            dueDate = scanner.nextLine();
            
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                dateFormat.setLenient(false);
                dateFormat.parse(dueDate);
                validDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY format.");
            }
        }
        
        // Create new task and add to list
        int id = getNextId();
        Task newTask = new Task(id, title, description, priority, dueDate);
        taskList.add(newTask);
        
        System.out.println("Task added successfully with ID: " + id);
    }

    /**
     * Determine the next available task ID
     * @return next available ID
     */
    private static int getNextId() {
        int maxId = 0;
        for (Task task : taskList) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * View tasks based on filter criteria
     * @param filter "all", "completed", or "incomplete"
     */
    private static void viewTasks(String filter) {
        if (taskList.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        
        System.out.println("\n----- Tasks -----");
        
        // Counter to track if any tasks match the filter
        int count = 0;
        
        // Iterate through all tasks
        for (Task task : taskList) {
            boolean shouldDisplay = false;
            
            // Apply filter criteria
            if (filter.equals("all")) {
                shouldDisplay = true;
            } else if (filter.equals("completed") && task.isCompleted()) {
                shouldDisplay = true;
            } else if (filter.equals("incomplete") && !task.isCompleted()) {
                shouldDisplay = true;
            }
            
            // Display task if it matches filter
            if (shouldDisplay) {
                System.out.println(task);
                System.out.println("-----------------------");
                count++;
            }
        }
        
        // If no tasks matched the filter
        if (count == 0) {
            System.out.println("No " + filter + " tasks found.");
        }
    }

    /**
     * Mark a task as complete based on its ID
     */
    private static void markTaskComplete() {
        System.out.println("\n----- Mark Task as Complete -----");
        System.out.print("Enter task ID to mark as complete: ");
        
        try {
            int taskId = Integer.parseInt(scanner.nextLine());
            boolean taskFound = false;
            
            // Search for the task with the given ID
            for (Task task : taskList) {
                if (task.getId() == taskId) {
                    if (task.isCompleted()) {
                        System.out.println("Task is already marked as complete.");
                    } else {
                        task.markComplete();
                        System.out.println("Task marked as complete successfully.");
                    }
                    taskFound = true;
                    break;
                }
            }
            
            if (!taskFound) {
                System.out.println("Task with ID " + taskId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }

    /**
     * Delete a task based on its ID
     */
    private static void deleteTask() {
        System.out.println("\n----- Delete Task -----");
        System.out.print("Enter task ID to delete: ");
        
        try {
            int taskId = Integer.parseInt(scanner.nextLine());
            boolean taskFound = false;
            
            // Find and remove the task with the given ID
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getId() == taskId) {
                    taskList.remove(i);
                    System.out.println("Task deleted successfully.");
                    taskFound = true;
                    break;
                }
            }
            
            if (!taskFound) {
                System.out.println("Task with ID " + taskId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }

    /**
     * Load tasks from file when program starts
     */
    private static void loadTasksFromFile() {
        File file = new File(DATA_FILE_PATH);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Parse each line using delimiter
                    String[] parts = line.split("\\|");
                    if (parts.length == 6) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            String title = parts[1].trim();
                            String description = parts[2].trim();
                            int priority = Integer.parseInt(parts[3].trim());
                            String dueDate = parts[4].trim();
                            boolean isCompleted = Boolean.parseBoolean(parts[5].trim());
                            
                            Task task = new Task(id, title, description, priority, dueDate);
                            if (isCompleted) {
                                task.markComplete();
                            }
                            taskList.add(task);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing task: " + line);
                        }
                    }
                }
                System.out.println("Loaded " + taskList.size() + " tasks from file.");
            } catch (IOException e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }
        }
    }

    /**
     * Save tasks to file when program exits
     */
    private static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
            for (Task task : taskList) {
                // Format each task as a line in the file
                String line = task.getId() + "|" + 
                              task.getTitle() + "|" + 
                              task.getDescription() + "|" + 
                              task.getPriority() + "|" + 
                              task.getDueDate() + "|" + 
                              task.isCompleted();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Tasks saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}
