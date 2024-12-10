package Components;

import Enums.OrderStatus;
import Enums.StoreStatus;
import Enums.UserRole;
import Utils.Images;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class Table extends JTable {

    public Table() {
        initTable();
    }

    private void initTable() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        setShowVerticalLines(false);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                TableHeader header = new TableHeader(value + "");
                if (column == 4) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                if (column == 5) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                    getColumnModel().getColumn(5).setPreferredWidth(10);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean selected, boolean focus, int row, int column) {
                if (value instanceof ActionListener) {
                    return new CellActionPanel((ActionListener) value);
                } else if (column == 4) {
                    return new CellStatusPanel(value);
                } else {
                    Component component = super.getTableCellRendererComponent(jTable, value, selected, focus, row, column);
                    setBorder(noFocusBorder);
                    component.setForeground(new Color(0, 0, 0, 0));
                    JLabel label = new JLabel(value + "");
                    label.setForeground(new Color(102, 102, 102));
                    return label;
                }
            }
        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int col) {
        if (col == 5) {
            return new TableCellAction();
        } else {
            return super.getCellEditor(row, col);
        }
    }

    public static class TableCellAction extends DefaultCellEditor {

        private ActionListener action;

        public TableCellAction() {
            super(new JCheckBox());
        }

        @Override
        public Component getTableCellEditorComponent(JTable jTable, Object object, boolean isSelected, int row, int column) {
            action = (ActionListener) object;
            return new CellActionPanel(action);
        }

        //  This method to pass data to cell render when focus lose in cell
        @Override
        public Object getCellEditorValue() {
            return action;
        }
    }

    public static class TableHeader extends JLabel {

        public TableHeader(String text) {
            super(text);
            setOpaque(true);
            setBackground(Color.WHITE);
            setFont(new Font("SanSerif", Font.BOLD, 12));
            setForeground(new Color(102, 102, 102));
            setBorder(new EmptyBorder(10, 5, 10, 5));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(230, 230, 230));
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    }

    public static class CellStatusPanel extends JPanel {

        private StatusLabel label;

        public CellStatusPanel(Object status) {
            if (status instanceof UserRole) {
                label = new UserRoleLabel();
            } else if (status instanceof StoreStatus) {
                label = new StoreStatusLabel();
            } else if (status instanceof OrderStatus){
                label = new OrderStatusLabel();
            } else return;

            label.setStatus(status);
            initComponents();
        }
        
        private void initComponents() {
            setBackground(Color.WHITE);

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(10, Short.MAX_VALUE)
                                    .addComponent(label, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(10, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(label, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                    .addGap(8, 8, 8))
            );
        }
    }

    private static class CellActionPanel extends JPanel {
        Button deleteButton = new Button(Images.getImage("DeleteImg", 15, 15));

        CellActionPanel(ActionListener action) {
            initComponents();
            deleteButton.addActionListener(action);
        }

        private void initComponents() {
            setBackground(Color.WHITE);
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(5, 20, 5, 20));
            deleteButton.setArch(30);
            deleteButton.setPreferredSize(new Dimension(40, 40));
            deleteButton.setFocusable(true);
            deleteButton.setOpaque(false);
            add(deleteButton);
        }
    }
}