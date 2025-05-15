import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ToDoListGUI extends JFrame {
    private TaskManager manager = new TaskManager();
    private JTable table;
    private DefaultTableModel model;

    private JTextField titleField = new JTextField(15);
    private JTextField descField = new JTextField(15);
    private JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "Done"});
    private JTextField searchField = new JTextField(15);

    public ToDoListGUI() {
        setTitle("ToDo List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==== Form Panel ====
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);
        JButton addBtn = new JButton("➕ Add");
        formPanel.add(addBtn);
        add(formPanel, BorderLayout.NORTH);

        // ==== Table ====
        model = new DefaultTableModel(new String[]{"ID", "Title", "Description", "Status", "Created At"}, 0);
        table = new JTable(model) {
            // Set row color based on status
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String status = getValueAt(row, 3).toString();
                c.setBackground(status.equals("Done") ? new Color(200, 255, 200) : Color.WHITE);
                return c;
            }
        };
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ==== Action Panel ====
        JPanel actionPanel = new JPanel();
        JButton updateBtn = new JButton("✅ Mark as Done");
        JButton deleteBtn = new JButton("❌ Delete");
        actionPanel.add(updateBtn);
        actionPanel.add(deleteBtn);

        // Search field
        actionPanel.add(new JLabel("🔍 Search:"));
        actionPanel.add(searchField);
        JButton searchBtn = new JButton("Go");
        actionPanel.add(searchBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // ==== Button Actions ====
        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            String status = (String) statusBox.getSelectedItem();
            if (!title.isEmpty()) {
                manager.addTask(title, desc);
                refreshTable();
                titleField.setText("");
                descField.setText("");
                statusBox.setSelectedIndex(0);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                manager.deleteTask(id);
                refreshTable();
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                manager.updateTaskStatus(id, "Done");
                refreshTable();
            }
        });

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            filterTable(keyword);
        });

        refreshTable();
        setVisible(true);
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Task> tasks = manager.getAllTasks();
        for (Task t : tasks) {
            model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt()});
        }
    }

    private void filterTable(String keyword) {
        model.setRowCount(0);
        List<Task> tasks = manager.getAllTasks();
        for (Task t : tasks) {
            if (t.getTitle().toLowerCase().contains(keyword) ||
                    t.getStatus().toLowerCase().contains(keyword)) {
                model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt()});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListGUI::new);
    }
}
