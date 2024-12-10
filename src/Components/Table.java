package Components;

import Enums.OrderStatus;
import Enums.StoreStatus;
import Enums.UserRole;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (column != 4) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    component.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if (isSelected) {
                        component.setForeground(new Color(15, 89, 140));
                    } else {
                        component.setForeground(new Color(102, 102, 102));
                    }
                    return component;
                } else {
                    return new CellPanel(value);
                }
            }
        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
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

    public static class CellPanel extends JPanel {

        private StatusLabel label;

        public CellPanel(Object status) {
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
            setBackground(new Color(255, 255, 255));

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
}