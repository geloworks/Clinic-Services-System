/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package EventDriven;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Angelo Javier
 */
public class Billing extends javax.swing.JDialog {

    ResultSet rs;
    Connection conn;
    PreparedStatement ps;

    private String userId;
    private int patientID;
    private String patientName;
    private int patientAge;
    private int serviceId;

    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/Icon.png"));
    Image iconImage = imageIcon.getImage();
    private TableRowSorter<DefaultTableModel> sorter;
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    DefaultTableModel model;

    AdminForm admin = new AdminForm("");

    /**
     * Creates new form Bill
     */
    public Billing(java.awt.Frame parent, boolean modal, String userId, int patientId, String patientName, int patientAge, int serviceId) {
        super(parent, modal);
        initComponents();
        conn = Database.sqlConnect();
        this.setIconImage(iconImage);
        this.setLocationRelativeTo(null);

        this.userId = userId;
        this.patientID = patientId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.serviceId = serviceId;
        this.model = (DefaultTableModel) supplyTable.getModel();
        center.setHorizontalAlignment(JLabel.CENTER);
        this.setTitle("Billing: " + this.patientID);

        setupDiscountOptions();
        loadPrescriptionData();
        updateSupplyTotal();
        setUpTable();
    }

    private void setupDiscountOptions() {
        discountComboBox.removeAllItems();
        discountComboBox.addItem("-- No Discount --");
        discountComboBox.addItem("PWD (5%)");

        if (this.patientAge >= 60) {
            discountComboBox.addItem("Senior (20%)");
        }
    }

    private void setUpTable() {
        DefaultTableModel dm = (DefaultTableModel) supplyTable.getModel();
        sorter = new TableRowSorter<>(dm);
        supplyTable.setRowSorter(sorter);
        supplyTable.setRowHeight(25);
        supplyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        supplyTable.getTableHeader().setBackground(Color.decode("#3C4753"));
        supplyTable.getTableHeader().setForeground(Color.WHITE);
        for (int i = 0; i < supplyTable.getColumnCount(); i++) {
            supplyTable.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void loadPrescriptionData() {
        double totalSupplies = 0;
        model.setRowCount(0);

        try {
            // 1. Get the latest prescription
            String sql = "SELECT Prescription FROM PatientRecords WHERE PatientID = ? ORDER BY Date DESC, RecordID DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                String prescription = rs.getString("Prescription");

                if (prescription != null && !prescription.isEmpty()) {
                    String[] items = prescription.split(",");

                    for (String item : items) {
                        item = item.trim();
                        int openParen = item.lastIndexOf("(x");
                        int closeParen = item.lastIndexOf(")");

                        if (openParen != -1 && closeParen != -1) {
                            int quantity = Integer.parseInt(item.substring(openParen + 2, closeParen));
                            String fullName = item.substring(0, openParen).trim();
                            double unitCost = 0;
                            String brand = fullName;
                            String generic = "";

                            String supplySql = "SELECT BrandName, GenericName, UnitCost FROM Supply "
                                    + "WHERE ? LIKE CONCAT('%', BrandName, '%') "
                                    + "OR ? LIKE CONCAT('%', GenericName, '%') LIMIT 1";

                            PreparedStatement psSupply = conn.prepareStatement(supplySql);
                            psSupply.setString(1, fullName);
                            psSupply.setString(2, fullName);
                            ResultSet rsSupply = psSupply.executeQuery();

                            if (rsSupply.next()) {
                                brand = rsSupply.getString("BrandName");
                                generic = rsSupply.getString("GenericName");
                                unitCost = rsSupply.getDouble("UnitCost");
                            }
                            model.addRow(new Object[]{brand, generic, quantity, unitCost});
                            totalSupplies += (unitCost * quantity);
                        }
                    }
                }
            }
            updateReceiptLabels(totalSupplies);
            updateSupplyTotal();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e);
        }
    }

    private void updateReceiptLabels(double totalSupplies) {
        try {
            ps = conn.prepareStatement("SELECT Price, ServiceName FROM Service WHERE ServiceID = ?");
            ps.setInt(1, this.serviceId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String serviceName = rs.getString("ServiceName");
                double servicePrice = rs.getDouble("Price");
                double baseCost = servicePrice + totalSupplies;
                double tax = baseCost * 0.12;
                double subTotal = baseCost + tax;
                double discountPercent = 0.0;

                Object selection = discountComboBox.getSelectedItem();
                if (selection != null) {
                    String selectedDiscount = selection.toString();

                    if (selectedDiscount.contains("PWD")) {
                        discountPercent = 0.05;
                    } else if (selectedDiscount.contains("Senior")) {
                        discountPercent = 0.20;
                    }
                }
                double discountAmount = subTotal * discountPercent;
                double totalDue = subTotal - discountAmount;

                serviceCostReceipt.setText("P " + String.format("%,.2f", servicePrice));
                supplyCostReceipt.setText("P " + String.format("%,.2f", totalSupplies));
                baseCostReceipt.setText("P " + String.format("%,.2f", baseCost));
                discountReceipt.setText("P " + String.format("%,.2f", discountAmount));
                subTotalReceipt.setText("P " + String.format("%,.2f", subTotal));
                totalReceipt.setText("P " + String.format("%,.2f", totalDue));
                nameReceipt.setText(this.patientName);
                ageReceipt.setText(String.valueOf(this.patientAge));
                serviceReceipt.setText(serviceName);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e);
        }
    }

    private void updateSupplyTotal() {
        double totalSupplies = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                Object qtyObj = model.getValueAt(i, 2);
                Object costObj = model.getValueAt(i, 3);

                if (qtyObj != null && costObj != null) {
                    int quantity = Integer.parseInt(qtyObj.toString());
                    double unitCost = Double.parseDouble(costObj.toString());

                    totalSupplies += (quantity * unitCost);
                }
            } catch (NumberFormatException e) {
                System.err.println("Row " + i + " calculation error: " + e.getMessage());
            }
        }

        supplyCostReceipt.setText("P " + String.format("%.2f", totalSupplies));

        updateReceiptLabels(totalSupplies);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        patientAgeLabel = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        discountLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        serviceNameLabel = new javax.swing.JLabel();
        serviceCostLabel = new javax.swing.JLabel();
        supplyCostLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        subtotalLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        baseCostLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        payBtn = new javax.swing.JButton();
        idMoneyField = new javax.swing.JTextField();
        cashField = new javax.swing.JTextField();
        nameReceipt = new javax.swing.JLabel();
        serviceCostReceipt = new javax.swing.JLabel();
        totalReceipt = new javax.swing.JLabel();
        ageReceipt = new javax.swing.JLabel();
        supplyCostReceipt = new javax.swing.JLabel();
        serviceReceipt = new javax.swing.JLabel();
        baseCostReceipt = new javax.swing.JLabel();
        subTotalReceipt = new javax.swing.JLabel();
        discountReceipt = new javax.swing.JLabel();
        idNumberField = new javax.swing.JTextField();
        checkButton = new javax.swing.JButton();
        paymentValidation = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplyTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        discountComboBox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        patientAgeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        patientAgeLabel.setText("Age:");
        jPanel1.add(patientAgeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        patientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        patientNameLabel.setText("Patient Receipt:");
        jPanel1.add(patientNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        discountLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        discountLabel.setText("Discount");
        jPanel1.add(discountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("-------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 580, 20));

        serviceNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceNameLabel.setText("Service: ");
        jPanel1.add(serviceNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        serviceCostLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceCostLabel.setText("Cost of Service: ");
        jPanel1.add(serviceCostLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        supplyCostLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        supplyCostLabel.setText("Cost of Supply: ");
        jPanel1.add(supplyCostLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Choose Payment Option");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, 20));

        subtotalLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subtotalLabel.setText("Sub-total");
        jPanel1.add(subtotalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("-------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 580, 20));

        baseCostLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        baseCostLabel.setText("Total Base Cost:");
        jPanel1.add(baseCostLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, 20));

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalLabel.setText("Total Amount Due:");
        jPanel1.add(totalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, 20));

        cancelBtn.setBackground(new java.awt.Color(255, 0, 0));
        cancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);
        jPanel1.add(cancelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, 90, 30));

        payBtn.setBackground(new java.awt.Color(0, 153, 255));
        payBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        payBtn.setForeground(new java.awt.Color(255, 255, 255));
        payBtn.setText("Pay");
        payBtn.addActionListener(this::payBtnActionPerformed);
        jPanel1.add(payBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 90, 30));

        idMoneyField.setEnabled(false);
        jPanel1.add(idMoneyField, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, 140, -1));
        jPanel1.add(cashField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 320, -1));

        nameReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nameReceipt.setText("name");
        jPanel1.add(nameReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 310, 20));

        serviceCostReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceCostReceipt.setText("service cost");
        jPanel1.add(serviceCostReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 310, 20));

        totalReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalReceipt.setText("total");
        jPanel1.add(totalReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 310, 20));

        ageReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ageReceipt.setText("age");
        jPanel1.add(ageReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 310, 20));

        supplyCostReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        supplyCostReceipt.setText("supply cost");
        jPanel1.add(supplyCostReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 310, -1));

        serviceReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceReceipt.setText("service");
        jPanel1.add(serviceReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 310, 20));

        baseCostReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        baseCostReceipt.setText("base cost");
        jPanel1.add(baseCostReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 310, -1));

        subTotalReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subTotalReceipt.setText("subtotal");
        jPanel1.add(subTotalReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, 310, -1));

        discountReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        discountReceipt.setText("discount");
        jPanel1.add(discountReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 310, -1));
        jPanel1.add(idNumberField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, 170, -1));

        checkButton.setBackground(new java.awt.Color(0, 204, 51));
        checkButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        checkButton.setForeground(new java.awt.Color(255, 255, 255));
        checkButton.setText("Check");
        checkButton.addActionListener(this::checkButtonActionPerformed);
        jPanel1.add(checkButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, 90, 30));

        paymentValidation.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(paymentValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 380, 20));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("PhilHealth:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Cash:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, -1, -1));

        supplyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Brand Name", "Generic Name", "Quantity", "Unit Cost"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        supplyTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(supplyTable);
        if (supplyTable.getColumnModel().getColumnCount() > 0) {
            supplyTable.getColumnModel().getColumn(0).setResizable(false);
            supplyTable.getColumnModel().getColumn(1).setResizable(false);
            supplyTable.getColumnModel().getColumn(2).setResizable(false);
            supplyTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 560, 130));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("-------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 580, 20));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("-------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 580, 20));

        discountComboBox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        discountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Discount --", "PWD" }));
        discountComboBox.addActionListener(this::discountComboBoxActionPerformed);
        jPanel1.add(discountComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 580, 550));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PATIENT BILLING & RECEIPT");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 330, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 80));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void payBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payBtnActionPerformed
        String cashMoney = cashField.getText().trim();
        String philHealthMoney = idMoneyField.getText().trim();

        if (cashMoney.isEmpty() && philHealthMoney.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an amount.");
            return; 
        }

        try {
            double cash = cashMoney.isEmpty() ? 0 : Double.parseDouble(cashMoney);
            double philHealth = philHealthMoney.isEmpty() ? 0 : Double.parseDouble(philHealthMoney);
            double totalPaid = cash + philHealth;

            double totalDue = Double.parseDouble(totalReceipt.getText().replace("P ", "").replace(",", ""));

            if (totalPaid >= totalDue) {
                double change = totalPaid - totalDue;

                String updateStockSql = "UPDATE Supply SET Quantity = Quantity - ? WHERE BrandName = ?";
                PreparedStatement psStock = conn.prepareStatement(updateStockSql);

                for (int i = 0; i < model.getRowCount(); i++) {
                    String brandName = model.getValueAt(i, 0).toString();
                    int qtyBought = Integer.parseInt(model.getValueAt(i, 2).toString());

                    psStock.setInt(1, qtyBought);
                    psStock.setString(2, brandName);
                    psStock.executeUpdate();

                    ps = conn.prepareStatement("UPDATE Dashboard SET SuppliesSold = SuppliesSold + ?");
                    ps.setInt(1, qtyBought);
                    ps.executeUpdate();
                }

                ps = conn.prepareStatement("UPDATE QueueBoard SET Status = 'Completed' WHERE PatientID = ?");
                ps.setInt(1, this.patientID);
                ps.executeUpdate();

                conn.createStatement().execute("DELETE FROM QueueBoard WHERE Status = 'Completed'");

                ps = conn.prepareStatement("UPDATE Dashboard SET Earnings = Earnings + ?");
                ps.setDouble(1, totalDue);
                ps.executeUpdate();
                
                String insertReceipt = "INSERT INTO Receipts (PatientID, PatientName, PatientAge, Service, "
                        + "ServiceCost, SupplyCost, BaseCost, Discount, SubTotal, TotalAmountDue, "
                        + "PhilHealthID, PhilHealthAmount, CashPaid, Changes,TransactionDate) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

                try (PreparedStatement psReceipt = conn.prepareStatement(insertReceipt)) {
                psReceipt.setInt(1, this.patientID);
                psReceipt.setString(2, nameReceipt.getText());
                psReceipt.setInt(3, Integer.parseInt(ageReceipt.getText()));
                psReceipt.setString(4, serviceReceipt.getText());
                psReceipt.setDouble(5, parseLabelToDouble(serviceCostReceipt.getText()));
                psReceipt.setDouble(6, parseLabelToDouble(supplyCostReceipt.getText()));
                psReceipt.setDouble(7, parseLabelToDouble(baseCostReceipt.getText()));
                psReceipt.setDouble(8, parseLabelToDouble(discountReceipt.getText()));
                psReceipt.setDouble(9, parseLabelToDouble(subTotalReceipt.getText()));
                psReceipt.setDouble(10, totalDue);
                psReceipt.setString(11, idNumberField.getText().isEmpty() ? null : idNumberField.getText());
                psReceipt.setDouble(12, philHealth);
                psReceipt.setDouble(13, cash);
                psReceipt.setDouble(14, change);
                psReceipt.executeUpdate();
                }
             
                admin.addReport(userId, "Billing", "Patient " + patientID + " paid P " + String.format("%,.2f", totalDue));
                
                JOptionPane.showMessageDialog(this, "Payment Successful!\nChange: P " + String.format("%,.2f", change));
                
                printReceipt();
                
                this.dispose();
                
            } else {
                double lacking = totalDue - totalPaid;
                JOptionPane.showMessageDialog(this, "Insufficient funds. Lacking: P " + String.format("%.2f", lacking),
                        "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("Payment DB Error: " + e);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format in payment fields.");
        }
    }//GEN-LAST:event_payBtnActionPerformed

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkButtonActionPerformed
        if (idNumberField.getText().length() > 10) {
            Random random = new Random();
            int randomNumber = random.nextInt(9001) + 1000;
            idMoneyField.setText(String.valueOf(randomNumber));
            checkButton.setEnabled(false);
        } else {
            idMoneyField.setText("0");
        }
    }//GEN-LAST:event_checkButtonActionPerformed

    private void discountComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountComboBoxActionPerformed
        // TODO add your handling code here:
        if (discountComboBox.getSelectedItem() != null) {
            updateSupplyTotal();
        }
    }//GEN-LAST:event_discountComboBoxActionPerformed

    public void printReceipt() {
    try {
        JasperReport patientReport = (JasperReport) JRLoader.loadObjectFromFile( "C:\\Users\\Angelo Javier\\Downloads\\JaspersoftWorkspace\\MyReports\\BillingReciept.jasper");       
        JasperPrint reportPrint = JasperFillManager.fillReport(patientReport, null, conn);
        JasperViewer viewer = new JasperViewer(reportPrint, false);
        javax.swing.JDialog reportDialog = new javax.swing.JDialog(this, "Print Preview", true);
        reportDialog.setContentPane(viewer.getContentPane()); 
        reportDialog.setSize(1000, 900);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setVisible(true);
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Print Error: " + ex.getMessage());
        ex.printStackTrace();
    }
}
    
    private double parseLabelToDouble(String labelText) {
       try {
           String cleanValue = labelText.replace("P ", "").replace(",", "").trim();
           return Double.parseDouble(cleanValue);
       } catch (Exception e) {
           return 0.0;
       }
   }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ageReceipt;
    private javax.swing.JLabel baseCostLabel;
    private javax.swing.JLabel baseCostReceipt;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField cashField;
    private javax.swing.JButton checkButton;
    private javax.swing.JComboBox<String> discountComboBox;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JLabel discountReceipt;
    private javax.swing.JTextField idMoneyField;
    private javax.swing.JTextField idNumberField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameReceipt;
    private javax.swing.JLabel patientAgeLabel;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JButton payBtn;
    private javax.swing.JLabel paymentValidation;
    private javax.swing.JLabel serviceCostLabel;
    private javax.swing.JLabel serviceCostReceipt;
    private javax.swing.JLabel serviceNameLabel;
    private javax.swing.JLabel serviceReceipt;
    private javax.swing.JLabel subTotalReceipt;
    private javax.swing.JLabel subtotalLabel;
    private javax.swing.JLabel supplyCostLabel;
    private javax.swing.JLabel supplyCostReceipt;
    private javax.swing.JTable supplyTable;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel totalReceipt;
    // End of variables declaration//GEN-END:variables
}
