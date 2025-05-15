import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n== ToDo List ==");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Update Task Status");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");

            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Description: ");
                    String desc = scanner.nextLine();
                    manager.addTask(title, desc);
                    break;
                case 2:
                    for (Task task : manager.getAllTasks()) {
                        System.out.println(task.getId() + ": " + task.getTitle() + " - " + task.getStatus());
                    }
                    break;
                case 3:
                    System.out.print("Enter Task ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("New Status (Pending/Done): ");
                    String status = scanner.nextLine();
                    manager.updateTaskStatus(id, status);
                    break;
                case 4:
                    System.out.print("Enter Task ID to delete: ");
                    int delId = scanner.nextInt();
                    manager.deleteTask(delId);
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
