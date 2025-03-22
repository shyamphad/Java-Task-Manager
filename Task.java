/**
 * Task class - represents an individual task with all its properties
 */
class Task {
    private int id;
    private String title;
    private String description;
    private int priority;
    private String dueDate;
    private boolean completed;
    
    /**
     * Constructor to create a new Task
     */
    public Task(int id, String title, String description, int priority, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
    }
    
    // Getters for all properties
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public String getDueDate() {
        return dueDate;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Mark this task as complete
     */
    public void markComplete() {
        this.completed = true;
    }
    
    /**
     * Convert priority number to text description
     */
    private String getPriorityText() {
        switch (priority) {
            case 1: return "Very Low";
            case 2: return "Low";
            case 3: return "Medium";
            case 4: return "High";
            case 5: return "Very High";
            default: return "Unknown";
        }
    }
    
    /**
     * Format task as a string for display
     */
    @Override
    public String toString() {
        return "ID: " + id + 
               "\nTitle: " + title + 
               "\nDescription: " + description + 
               "\nPriority: " + priority + " (" + getPriorityText() + ")" +
               "\nDue Date: " + dueDate + 
               "\nStatus: " + (completed ? "Completed" : "Not Completed");
    }
}
