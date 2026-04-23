/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package EventDriven;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Angelo Javier
 */
public class StaffForm extends javax.swing.JFrame {

    ResultSet rs;
    Connection conn;
    PreparedStatement ps;
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();

    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/Icon.png"));
    Image iconImage = imageIcon.getImage();
    private TableRowSorter<DefaultTableModel> sorter;

    String userId;
    int row;
    AdminForm admin = new AdminForm("");

    /**
     * Creates new form AdminForm
     */
    public StaffForm(String staffID) {
        initComponents();
        this.userId = staffID;
        this.setTitle("Staff - " + userId);
        this.setIconImage(iconImage);
        this.setLocationRelativeTo(null);

        conn = Database.sqlConnect();
        center.setHorizontalAlignment(JLabel.CENTER);

        // show data
        showQueueData();
        showServiceData();
        showSupplyData();
        showPatientData();

        // table setup
        setupQueueTable(queueTable);
        setupTable(serviceTable);
        setupSuppliesTable(suppliesTable);
        setupTable(patientTable);

        // button icons
        setButtonIcon(homeButton, "src\\image\\Home.png");
        setButtonIcon(servicesButton, "src\\image\\Service.png");
        setButtonIcon(suppliesButton, "src\\image\\Supplies.png");
        setButtonIcon(recordsButton, "src\\image\\Records.png");

        // label icons
        setLabelIcon(jLabel20, "src\\image\\Records.png");
        setLabelIcon(jLabel14, "src\\image\\Supplies.png");
        setLabelIcon(jLabel10, "src\\image\\Service.png");

        validators();
    }

    private void validators() {
        searchService.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchServiceData();
            }

            public void removeUpdate(DocumentEvent e) {
                searchServiceData();
            }

            public void changedUpdate(DocumentEvent e) {
                searchServiceData();
            }
        });

        searchSupply.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchSupplyData();
            }

            public void removeUpdate(DocumentEvent e) {
                searchSupplyData();
            }

            public void changedUpdate(DocumentEvent e) {
                searchSupplyData();
            }
        });

        searchPatient.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchPatientData();
            }

            public void removeUpdate(DocumentEvent e) {
                searchPatientData();
            }

            public void changedUpdate(DocumentEvent e) {
                searchPatientData();
            }
        });
    }

    private void searchServiceData() {
        try {
            if (searchService.getText().trim().isEmpty()) {
                showServiceData();
                searchServiceValidation.setText("");
                return;
            }

            ps = conn.prepareStatement("SELECT * FROM Service WHERE "
                    + "CONCAT('SR-', LPAD(ServiceID, 3, '0')) LIKE ? OR "
                    + "ServiceName LIKE ? OR "
                    + "CAST(Price AS CHAR) LIKE ?");

            String keyword = "%" + searchService.getText().trim() + "%";

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            rs = ps.executeQuery();

            DefaultTableModel dm = (DefaultTableModel) serviceTable.getModel();
            dm.setRowCount(0);

            boolean found = false;

            while (rs.next()) {
                found = true;

                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("SR-%03d", rs.getInt("ServiceID")));
                v1.add(rs.getString("ServiceName"));
                v1.add(rs.getDouble("Price"));

                dm.addRow(v1);
            }

            if (!found) {
                searchServiceValidation.setForeground(Color.red);
                searchServiceValidation.setText("No result found");
            } else {
                searchServiceValidation.setText("");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void searchSupplyData() {
        try {
            if (searchSupply.getText().trim().isEmpty()) {
                showSupplyData();
                searchSupplyValidation.setText("");
                return;
            }

            ps = conn.prepareStatement("SELECT * FROM Supply WHERE "
                    + "CONCAT('SP-', LPAD(SupplyID, 3, '0')) LIKE ? OR "
                    + "BrandName LIKE ? OR "
                    + "GenericName LIKE ? OR "
                    + "Measurement LIKE ? OR "
                    + "Category LIKE ? OR "
                    + "CAST(Quantity AS CHAR) LIKE ? OR "
                    + "CAST(UnitCost AS CHAR) LIKE ?"
            );

            String keyword = "%" + searchSupply.getText().trim() + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setString(5, keyword);
            ps.setString(6, keyword);
            ps.setString(7, keyword);

            rs = ps.executeQuery();

            DefaultTableModel dm = (DefaultTableModel) suppliesTable.getModel();
            dm.setRowCount(0);

            boolean found = false;

            while (rs.next()) {
                found = true;

                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("SP-%03d", rs.getInt("SupplyID")));
                v1.add(rs.getString("BrandName"));
                v1.add(rs.getString("GenericName"));
                v1.add(rs.getString("Measurement"));
                v1.add(rs.getString("Category"));
                v1.add(rs.getInt("Quantity"));
                v1.add(rs.getDouble("UnitCost"));
                dm.addRow(v1);
            }

            if (!found) {
                searchSupplyValidation.setForeground(Color.red);
                searchSupplyValidation.setText("No result found");
            } else {
                searchSupplyValidation.setText("");
            }

        } catch (SQLException ex) {
            System.out.println("Supply" + ex);
        }
    }

    private void searchPatientData() {
        try {
            String keywordInput = searchPatient.getText().trim();
            if (keywordInput.isEmpty()) {
                showPatientData();
                searchPatientValidation.setText("");
                return;
            }

            String keyword = "%" + keywordInput + "%";
            ps = conn.prepareStatement(
                    "SELECT * FROM Patient WHERE "
                    + "CONCAT('P-', LPAD(PatientID, 3, '0')) LIKE ? OR "
                    + "FirstName LIKE ? OR "
                    + "LastName LIKE ? OR "
                    + "Sex LIKE ? OR "
                    + "CAST(Age AS CHAR) LIKE ? OR "
                    + "CAST(Weight AS CHAR) LIKE ? OR "
                    + "CAST(Height AS CHAR) LIKE ? OR "
                    + "ContactNumber LIKE ?"
            );

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setString(5, keyword);
            ps.setString(6, keyword);
            ps.setString(7, keyword);
            ps.setString(8, keyword);
            rs = ps.executeQuery();

            DefaultTableModel dm = (DefaultTableModel) patientTable.getModel();
            dm.setRowCount(0);

            boolean found = false;
            while (rs.next()) {
                found = true;
                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("P-%03d", rs.getInt("PatientID")));
                v1.add(rs.getString("FirstName"));
                v1.add(rs.getString("LastName"));
                v1.add(rs.getString("Sex"));
                v1.add(rs.getInt("Age"));
                v1.add(rs.getString("Weight"));
                v1.add(rs.getString("Height"));
                v1.add(rs.getString("ContactNumber"));
                dm.addRow(v1);
            }

            if (!found) {
                searchPatientValidation.setForeground(Color.red);
                searchPatientValidation.setText("No result found");
            } else {
                searchPatientValidation.setText("");
            }
        } catch (SQLException ex) {
            System.out.println("E: " + ex);
        }
    }

    private void showServiceData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM Service");
            rs = ps.executeQuery();
            DefaultTableModel dm = (DefaultTableModel) serviceTable.getModel();
            dm.setRowCount(0);
            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("SR-%03d", rs.getInt("ServiceID")));
                v1.add(rs.getString("ServiceName"));
                v1.add(rs.getDouble("Price"));
                dm.addRow(v1);
            }
        } catch (SQLException ex) {
//            System.getLogger(NewJFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private void showSupplyData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM Supply");
            rs = ps.executeQuery();
            DefaultTableModel dm = (DefaultTableModel) suppliesTable.getModel();
            dm.setRowCount(0);
            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("SP-%03d", rs.getInt("SupplyID")));
                v1.add(rs.getString("BrandName"));
                v1.add(rs.getString("GenericName"));
                v1.add(rs.getString("Measurement"));
                v1.add(rs.getString("Category"));
                v1.add(rs.getInt("Quantity"));
                v1.add(rs.getDouble("UnitCost"));
                dm.addRow(v1);
            }
        } catch (SQLException ex) {
            System.out.println("" + ex);
        }

    }

    private void showPatientData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM Patient");
            rs = ps.executeQuery();
            DefaultTableModel dm = (DefaultTableModel) patientTable.getModel();
            dm.setRowCount(0);
            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("P-%03d", rs.getInt("PatientID")));
                v1.add(rs.getString("FirstName"));
                v1.add(rs.getString("LastName"));
                v1.add(rs.getString("Sex"));
                v1.add(rs.getInt("Age"));
                v1.add(rs.getString("Weight") + " kg");
                v1.add(rs.getString("Height") + " cm");
                v1.add(rs.getString("ContactNumber"));
                dm.addRow(v1);
            }
        } catch (SQLException ex) {
            System.out.println("E" + ex);
        }
    }

    private void showQueueData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM QueueBoard ORDER BY QueueNumber ASC");
            rs = ps.executeQuery();

            DefaultTableModel dm = (DefaultTableModel) queueTable.getModel();
            dm.setRowCount(0);

            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(rs.getInt("QueueNumber"));
                v1.add(String.format("P-%03d", rs.getInt("PatientID")));
                v1.add(rs.getString("PatientName"));
                v1.add(rs.getString("Service"));
                v1.add(rs.getString("Doctor"));
                v1.add(rs.getString("Status"));

                dm.addRow(v1);
            }
        } catch (SQLException ex) {
            System.out.println("Error loading QueueBoard: " + ex);
        }
    }

    private void setButtonIcon(JButton button, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scale = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scale);
        button.setIcon(icon);
    }

    private void setLabelIcon(JLabel label, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scale = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scale);
        label.setIcon(icon);
    }

    private void setupTable(JTable table) {
        DefaultTableModel dm = (DefaultTableModel) table.getModel();
        sorter = new TableRowSorter<>(dm);
        table.setRowSorter(sorter);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.decode("#3C4753"));
        table.getTableHeader().setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void setupSuppliesTable(JTable table) {
        DefaultTableModel dm = (DefaultTableModel) table.getModel();
        sorter = new TableRowSorter<>(dm);
        table.setRowSorter(sorter);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.decode("#3C4753"));
        table.getTableHeader().setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        table.getColumnModel().getColumn(5).setCellRenderer(new QuantityRenderer());
    }

    private void setupQueueTable(JTable table) {
        table.setRowHeight(60);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.decode("#3C4753"));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(Color.WHITE);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new TextAreaRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new TextAreaRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidePanel = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        homeButton = new javax.swing.JButton();
        servicesButton = new javax.swing.JButton();
        suppliesButton = new javax.swing.JButton();
        recordsButton = new javax.swing.JButton();
        cardPane = new javax.swing.JLayeredPane();
        queuePanel = new javax.swing.JPanel();
        queueValidator = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queueTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        addPatientQueue = new javax.swing.JButton();
        startConsultation = new javax.swing.JButton();
        billingQueue = new javax.swing.JButton();
        skipQueue = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        servicePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        searchService = new javax.swing.JTextField();
        searchServiceValidation = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        serviceValidator = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        printServices = new javax.swing.JButton();
        suppliesPanel = new javax.swing.JPanel();
        supplyValidator = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        searchSupply = new javax.swing.JTextField();
        searchSupplyValidation = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        suppliesTable = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        printSupplies = new javax.swing.JButton();
        recordsPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        searchPatient = new javax.swing.JTextField();
        searchPatientValidation = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        patientTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        patientContact = new javax.swing.JTextField();
        patientFName = new javax.swing.JTextField();
        addPatient = new javax.swing.JButton();
        addToQueue = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        patientAge = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        patientSex = new javax.swing.JComboBox<>();
        patientLName = new javax.swing.JTextField();
        updatePatient = new javax.swing.JButton();
        patientHeight = new javax.swing.JTextField();
        patientWeight = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        patientValidator = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Clinic - Admin Menu");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidePanel.setBackground(new java.awt.Color(41, 41, 41));
        sidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutButton.setBackground(new java.awt.Color(41, 41, 41));
        logoutButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Log out");
        logoutButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(this::logoutButtonActionPerformed);
        sidePanel.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 190, 50));

        homeButton.setBackground(new java.awt.Color(51, 153, 255));
        homeButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        homeButton.setForeground(new java.awt.Color(255, 255, 255));
        homeButton.setText("Home");
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        homeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        homeButton.setIconTextGap(15);
        homeButton.setOpaque(true);
        homeButton.setVerifyInputWhenFocusTarget(false);
        homeButton.addActionListener(this::homeButtonActionPerformed);
        sidePanel.add(homeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 50));

        servicesButton.setBackground(new java.awt.Color(41, 41, 41));
        servicesButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        servicesButton.setForeground(new java.awt.Color(255, 255, 255));
        servicesButton.setText("Services");
        servicesButton.setBorderPainted(false);
        servicesButton.setFocusPainted(false);
        servicesButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        servicesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        servicesButton.setIconTextGap(15);
        servicesButton.setOpaque(true);
        servicesButton.addActionListener(this::servicesButtonActionPerformed);
        sidePanel.add(servicesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 210, 50));

        suppliesButton.setBackground(new java.awt.Color(41, 41, 41));
        suppliesButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        suppliesButton.setForeground(new java.awt.Color(255, 255, 255));
        suppliesButton.setText("Supplies");
        suppliesButton.setBorderPainted(false);
        suppliesButton.setFocusPainted(false);
        suppliesButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        suppliesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        suppliesButton.setIconTextGap(15);
        suppliesButton.setOpaque(true);
        suppliesButton.addActionListener(this::suppliesButtonActionPerformed);
        sidePanel.add(suppliesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 210, 50));

        recordsButton.setBackground(new java.awt.Color(41, 41, 41));
        recordsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        recordsButton.setForeground(new java.awt.Color(255, 255, 255));
        recordsButton.setText("Manage Patient");
        recordsButton.setBorderPainted(false);
        recordsButton.setFocusPainted(false);
        recordsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        recordsButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        recordsButton.setIconTextGap(15);
        recordsButton.setOpaque(true);
        recordsButton.addActionListener(this::recordsButtonActionPerformed);
        sidePanel.add(recordsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 210, 50));

        getContentPane().add(sidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 660));

        cardPane.setLayout(new java.awt.CardLayout());

        queuePanel.setBackground(new java.awt.Color(245, 245, 245));
        queuePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        queueValidator.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        queueValidator.setForeground(new java.awt.Color(255, 51, 0));
        queuePanel.add(queueValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 1000, 30));

        queueTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        queueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Queue No.", "PatientID", "PatientName", "Service", "Doctor", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        queueTable.getTableHeader().setResizingAllowed(false);
        queueTable.getTableHeader().setReorderingAllowed(false);
        queueTable.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(queueTable);
        if (queueTable.getColumnModel().getColumnCount() > 0) {
            queueTable.getColumnModel().getColumn(0).setResizable(false);
            queueTable.getColumnModel().getColumn(1).setResizable(false);
            queueTable.getColumnModel().getColumn(2).setResizable(false);
            queueTable.getColumnModel().getColumn(3).setResizable(false);
            queueTable.getColumnModel().getColumn(4).setResizable(false);
            queueTable.getColumnModel().getColumn(5).setResizable(false);
        }

        queuePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1000, 480));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel5.setText("Queue Board");
        queuePanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addPatientQueue.setBackground(new java.awt.Color(0, 204, 51));
        addPatientQueue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addPatientQueue.setForeground(new java.awt.Color(255, 255, 255));
        addPatientQueue.setText("Add Patient");
        addPatientQueue.addActionListener(this::addPatientQueueActionPerformed);
        jPanel1.add(addPatientQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 160, 30));

        startConsultation.setBackground(new java.awt.Color(255, 204, 0));
        startConsultation.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        startConsultation.setForeground(new java.awt.Color(255, 255, 255));
        startConsultation.setText("Start Consultation");
        startConsultation.addActionListener(this::startConsultationActionPerformed);
        jPanel1.add(startConsultation, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 160, 30));

        billingQueue.setBackground(new java.awt.Color(0, 153, 255));
        billingQueue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        billingQueue.setForeground(new java.awt.Color(255, 255, 255));
        billingQueue.setText("Proceed to Billing");
        billingQueue.addActionListener(this::billingQueueActionPerformed);
        jPanel1.add(billingQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 160, 30));

        skipQueue.setBackground(new java.awt.Color(255, 0, 0));
        skipQueue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        skipQueue.setForeground(new java.awt.Color(255, 255, 255));
        skipQueue.setText("Skip");
        skipQueue.addActionListener(this::skipQueueActionPerformed);
        jPanel1.add(skipQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 20, 160, 30));

        queuePanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 1000, 70));

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        queuePanel.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 1000, 3));

        cardPane.add(queuePanel, "card2");

        servicePanel.setBackground(new java.awt.Color(245, 245, 245));
        servicePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Search :");
        servicePanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 48, -1, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel10.setText("Services");
        servicePanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, -1));
        servicePanel.add(searchService, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 200, 30));

        searchServiceValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchServiceValidation.setForeground(new java.awt.Color(255, 51, 0));
        servicePanel.add(searchServiceValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 240, 380, 80));

        serviceTable.setBackground(new java.awt.Color(245, 245, 245));
        serviceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Service ID", "Service Name", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        serviceTable.getTableHeader().setResizingAllowed(false);
        serviceTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(serviceTable);
        if (serviceTable.getColumnModel().getColumnCount() > 0) {
            serviceTable.getColumnModel().getColumn(0).setResizable(false);
            serviceTable.getColumnModel().getColumn(1).setResizable(false);
            serviceTable.getColumnModel().getColumn(2).setResizable(false);
        }

        servicePanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 1000, 420));

        serviceValidator.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        serviceValidator.setForeground(new java.awt.Color(255, 51, 0));
        servicePanel.add(serviceValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 520, 520, 30));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        servicePanel.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

        printServices.setBackground(new java.awt.Color(0, 153, 153));
        printServices.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printServices.setForeground(new java.awt.Color(255, 255, 255));
        printServices.setText("Print Services");
        printServices.setFocusable(false);
        printServices.addActionListener(this::printServicesActionPerformed);
        servicePanel.add(printServices, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 210, 30));

        cardPane.add(servicePanel, "card3");

        suppliesPanel.setBackground(new java.awt.Color(245, 245, 245));
        suppliesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        supplyValidator.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        supplyValidator.setForeground(new java.awt.Color(255, 51, 0));
        suppliesPanel.add(supplyValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, 520, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Search :");
        suppliesPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 48, -1, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel14.setText("Supplies");
        suppliesPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 300, -1));
        suppliesPanel.add(searchSupply, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 200, 30));

        searchSupplyValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchSupplyValidation.setForeground(new java.awt.Color(255, 51, 0));
        suppliesPanel.add(searchSupplyValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 230, 380, 80));

        suppliesTable.setBackground(new java.awt.Color(245, 245, 245));
        suppliesTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        suppliesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supply ID", "Brand Name", "Generic Name", "Measurement", "Category", "Quantity", "Unit Cost"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        suppliesTable.getTableHeader().setResizingAllowed(false);
        suppliesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(suppliesTable);
        if (suppliesTable.getColumnModel().getColumnCount() > 0) {
            suppliesTable.getColumnModel().getColumn(0).setResizable(false);
            suppliesTable.getColumnModel().getColumn(1).setResizable(false);
            suppliesTable.getColumnModel().getColumn(2).setResizable(false);
            suppliesTable.getColumnModel().getColumn(3).setResizable(false);
            suppliesTable.getColumnModel().getColumn(4).setResizable(false);
            suppliesTable.getColumnModel().getColumn(5).setResizable(false);
            suppliesTable.getColumnModel().getColumn(6).setResizable(false);
        }

        suppliesPanel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 450));

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        suppliesPanel.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

        printSupplies.setBackground(new java.awt.Color(0, 153, 153));
        printSupplies.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printSupplies.setForeground(new java.awt.Color(255, 255, 255));
        printSupplies.setText("Print Supplies");
        printSupplies.setFocusable(false);
        printSupplies.addActionListener(this::printSuppliesActionPerformed);
        suppliesPanel.add(printSupplies, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 210, 30));

        cardPane.add(suppliesPanel, "card3");

        recordsPanel.setBackground(new java.awt.Color(245, 245, 245));
        recordsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Search Patient (ID or Name):");
        recordsPanel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 48, -1, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel20.setText("Registered Patient");
        recordsPanel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));
        recordsPanel.add(searchPatient, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 50, 200, 30));

        searchPatientValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchPatientValidation.setForeground(new java.awt.Color(255, 51, 0));
        recordsPanel.add(searchPatientValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, 380, 80));

        patientTable.setBackground(new java.awt.Color(245, 245, 245));
        patientTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        patientTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Patient ID", "First Name", "Last Name", "Sex", "Age", "Weight", "Height", "Contact"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patientTable.getTableHeader().setResizingAllowed(false);
        patientTable.getTableHeader().setReorderingAllowed(false);
        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patientTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(patientTable);
        if (patientTable.getColumnModel().getColumnCount() > 0) {
            patientTable.getColumnModel().getColumn(0).setResizable(false);
            patientTable.getColumnModel().getColumn(1).setResizable(false);
            patientTable.getColumnModel().getColumn(2).setResizable(false);
            patientTable.getColumnModel().getColumn(3).setResizable(false);
            patientTable.getColumnModel().getColumn(4).setResizable(false);
            patientTable.getColumnModel().getColumn(5).setResizable(false);
            patientTable.getColumnModel().getColumn(6).setResizable(false);
            patientTable.getColumnModel().getColumn(7).setResizable(false);
        }

        recordsPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 280));

        jPanel3.setBackground(new java.awt.Color(245, 245, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Contact No.");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Last Name");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        patientContact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                patientContactFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                patientContactFocusLost(evt);
            }
        });
        patientContact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                patientContactKeyTyped(evt);
            }
        });
        jPanel3.add(patientContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 160, -1));
        jPanel3.add(patientFName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 160, -1));

        addPatient.setBackground(new java.awt.Color(51, 153, 255));
        addPatient.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addPatient.setForeground(new java.awt.Color(255, 255, 255));
        addPatient.setText("Add Patient");
        addPatient.addActionListener(this::addPatientActionPerformed);
        jPanel3.add(addPatient, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, 160, 30));

        addToQueue.setBackground(new java.awt.Color(0, 204, 51));
        addToQueue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addToQueue.setForeground(new java.awt.Color(255, 255, 255));
        addToQueue.setText("Add to Queue");
        addToQueue.addActionListener(this::addToQueueActionPerformed);
        jPanel3.add(addToQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 180, 160, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("First Name");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel3.add(patientAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 160, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("Sex");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setText("Height");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        patientSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Sex", "Male", "Female" }));
        jPanel3.add(patientSex, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 160, -1));
        jPanel3.add(patientLName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 160, -1));

        updatePatient.setBackground(new java.awt.Color(255, 153, 0));
        updatePatient.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updatePatient.setForeground(new java.awt.Color(255, 255, 255));
        updatePatient.setText("Update Patient");
        updatePatient.addActionListener(this::updatePatientActionPerformed);
        jPanel3.add(updatePatient, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 100, 160, 30));
        jPanel3.add(patientHeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 160, -1));
        jPanel3.add(patientWeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 160, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("Age");
        jPanel3.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setText("Weight");
        jPanel3.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        recordsPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 1000, 220));

        patientValidator.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        recordsPanel.add(patientValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 820, 30));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        recordsPanel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        recordsPanel.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 1000, 3));

        cardPane.add(recordsPanel, "card3");

        getContentPane().add(cardPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 1020, 660));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        // TODO add your handling code here:
        queuePanel.setVisible(true);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(false);

        homeButton.setBackground(Color.decode("#3399FF"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#292929"));

        queueValidator.setText("");
        serviceValidator.setText("");
        supplyValidator.setText("");
        patientValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");

        queueTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();

        clearPatient();
    }//GEN-LAST:event_homeButtonActionPerformed

    private void servicesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicesButtonActionPerformed
        // TODO add your handling code here:
        queuePanel.setVisible(false);
        servicePanel.setVisible(true);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(false);

        homeButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#3399FF"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#292929"));

        queueValidator.setText("");
        serviceValidator.setText("");
        supplyValidator.setText("");
        patientValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");

        queueTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();

        clearPatient();
    }//GEN-LAST:event_servicesButtonActionPerformed

    private void suppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesButtonActionPerformed
        // TODO add your handling code here:
        queuePanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(true);
        recordsPanel.setVisible(false);

        homeButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#3399FF"));
        recordsButton.setBackground(Color.decode("#292929"));

        queueValidator.setText("");
        serviceValidator.setText("");
        supplyValidator.setText("");
        patientValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");

        queueTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();

        clearPatient();

    }//GEN-LAST:event_suppliesButtonActionPerformed

    private void recordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordsButtonActionPerformed
        // TODO add your handling code here:
        queuePanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(true);

        homeButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#3399FF"));

        queueValidator.setText("");
        serviceValidator.setText("");
        supplyValidator.setText("");
        patientValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");

        queueTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();

        clearPatient();
    }//GEN-LAST:event_recordsButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log out Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            LoginForm form = new LoginForm();
            form.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void addPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPatientActionPerformed
        try {
            if (patientFName.getText().trim().isEmpty()
                    || patientLName.getText().trim().isEmpty()
                    || patientAge.getText().trim().isEmpty()
                    || patientWeight.getText().trim().isEmpty()
                    || patientHeight.getText().trim().isEmpty()
                    || patientContact.getText().trim().isEmpty()) {

                patientValidator.setForeground(Color.red);
                patientValidator.setText("Please fill in all patient fields to add");
                return;
            }

            int age = Integer.parseInt(patientAge.getText());
            if (age < 0 || age > 120) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Age must be between 0 and 120");
                return;
            }

            if (patientSex.getSelectedIndex() == 0) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Please select sex");
                return;
            }

            String contact = patientContact.getText().trim();
            if (!contact.matches("^09\\d*")) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Contact number must start with 09 and contain numbers only");
                return;
            }

            if (!contact.matches("^09\\d{9}$")) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Contact number must be 11 digits starting with 09");
                return;
            }

            String sex = patientSex.getSelectedItem().toString();
            String fullName = patientFName.getText().trim() + patientLName.getText().trim();
            ps = conn.prepareStatement("INSERT INTO Patient(FirstName, LastName, Sex, Age, Weight, Height, ContactNumber) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, patientFName.getText().trim());
            ps.setString(2, patientLName.getText().trim());
            ps.setString(3, sex);
            ps.setInt(4, age);
            ps.setDouble(5, Double.parseDouble(patientWeight.getText().trim()));
            ps.setDouble(6, Double.parseDouble(patientHeight.getText().trim()));
            ps.setString(7, contact);

            if (ps.executeUpdate() == 1) {
                patientValidator.setForeground(Color.green);
                patientValidator.setText("Successfully Added");
                admin.addReport(userId, "Add Patient", "Added Patient: " + fullName);
                try {
                    String sql = "UPDATE Dashboard SET PatientRegistered = PatientRegistered + 1";
                    ps = conn.prepareStatement(sql);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e);
                }
                showPatientData();
                clearPatient();
            }
        } catch (NumberFormatException e) {
            patientValidator.setForeground(Color.red);
            patientValidator.setText("Age must be a number");
        } catch (SQLException ex) {
            System.out.println(":" + ex);
        }
    }//GEN-LAST:event_addPatientActionPerformed

    private void patientTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patientTableMouseClicked
        try {
            row = patientTable.getSelectedRow();
            if (row == -1) {
                return;
            }
            String displayedID = patientTable.getModel().getValueAt(row, 0).toString();
            int patientID;
            if (displayedID.startsWith("P-")) {
                patientID = Integer.parseInt(displayedID.replace("P-", ""));
            } else {
                patientID = Integer.parseInt(displayedID);
            }
            ps = conn.prepareStatement("SELECT * FROM Patient WHERE PatientID = ?");
            ps.setInt(1, patientID);
            rs = ps.executeQuery();

            if (rs.next()) {
                patientFName.setText(rs.getString("FirstName"));
                patientLName.setText(rs.getString("LastName"));
                patientSex.setSelectedItem(rs.getString("Sex"));
                patientAge.setText(rs.getString("Age"));
                patientWeight.setText(rs.getString("Weight"));
                patientHeight.setText(rs.getString("Height"));
                patientContact.setText(rs.getString("ContactNumber"));
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_patientTableMouseClicked

    private void addToQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToQueueActionPerformed
        row = patientTable.getSelectedRow();

        if (row == -1) {
            patientValidator.setForeground(Color.red);
            patientValidator.setText("Please select a patient first to add to queue");
            return;
        }

        String displayedID = patientTable.getModel().getValueAt(row, 0).toString();
        String fullName = patientFName.getText().trim() + " " + patientLName.getText().trim();

        LocalDate now = LocalDate.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        AddToQueue dialog = new AddToQueue(this, true, userId, displayedID, fullName);
        dialog.setLabel(formattedDate);
        dialog.setVisible(true);
        showQueueData();
    }//GEN-LAST:event_addToQueueActionPerformed

    private void updatePatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePatientActionPerformed
        // TODO add your handling code here:
        try {
            row = patientTable.getSelectedRow();

            if (row == -1) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Please select a patient first to update");
                return;
            }

            if (patientFName.getText().trim().isEmpty()
                    || patientLName.getText().trim().isEmpty()
                    || patientAge.getText().trim().isEmpty()
                    || patientContact.getText().trim().isEmpty()) {

                patientValidator.setForeground(Color.red);
                patientValidator.setText("Please fill in all patient fields to update");
                return;
            }

            int age = Integer.parseInt(patientAge.getText());
            if (age < 0 || age > 120) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Age must be between 0 and 120");
                return;
            }

            if (patientSex.getSelectedIndex() == 0) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Please select sex");
                return;
            }

            String contact = patientContact.getText().trim();
            if (!contact.matches("^09\\d*")) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Contact number must start with 09 and contain numbers only");
                return;
            }

            if (!contact.matches("^09\\d{9}$")) {
                patientValidator.setForeground(Color.red);
                patientValidator.setText("Contact number must be 11 digits starting with 09");
                return;
            }

            String displayedID = patientTable.getModel().getValueAt(row, 0).toString();
            int patientID = displayedID.startsWith("P-")
                    ? Integer.parseInt(displayedID.replace("P-", ""))
                    : Integer.parseInt(displayedID);

            String fullName = patientFName.getText().trim() + " " + patientLName.getText().trim();

            ps = conn.prepareStatement(
                    "UPDATE Patient SET FirstName = ?, LastName = ?, Sex = ?, Age = ?, Weight = ?, Height = ?, ContactNumber = ? WHERE PatientID = ?"
            );
            ps.setString(1, patientFName.getText().trim());
            ps.setString(2, patientLName.getText().trim());
            ps.setString(3, patientSex.getSelectedItem().toString());
            ps.setString(4, patientAge.getText().trim());
            ps.setString(5, patientWeight.getText().trim());
            ps.setString(6, patientHeight.getText().trim());
            ps.setString(7, patientContact.getText().trim());
            ps.setInt(8, patientID);

            if (ps.executeUpdate() == 1) {
                patientValidator.setForeground(Color.green);
                patientValidator.setText("Successfully Updated");
                admin.addReport(userId, "Update Patient", "Updated Patient: " + fullName);
                showPatientData();
                clearPatient();
            }

        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_updatePatientActionPerformed

    private void addPatientQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPatientQueueActionPerformed
        // TODO add your handling code here:
        queuePanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(true);

        homeButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#3399FF"));
    }//GEN-LAST:event_addPatientQueueActionPerformed

    private void skipQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipQueueActionPerformed
        row = queueTable.getSelectedRow();

        if (row == -1) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Please select a patient to skip");
            return;
        }

        int skippedNum = (int) queueTable.getValueAt(row, 0);
        String displayId = queueTable.getValueAt(row, 1).toString();

        try {
            String sqlDelete = "DELETE FROM QueueBoard WHERE QueueNumber = ?";
            ps = conn.prepareStatement(sqlDelete);
            ps.setInt(1, skippedNum);
            ps.executeUpdate();

            String sqlShift = "UPDATE QueueBoard SET QueueNumber = QueueNumber - 1 WHERE QueueNumber > ?";
            ps = conn.prepareStatement(sqlShift);
            ps.setInt(1, skippedNum);
            ps.executeUpdate();

            showQueueData();
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Patient " + displayId + " skipped");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

    }//GEN-LAST:event_skipQueueActionPerformed

    private void startConsultationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startConsultationActionPerformed
        // TODO add your handling code here:
        row = queueTable.getSelectedRow();

        if (row == -1) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Please select a patient to start consultation");
            return;
        }

        String currentStatus = queueTable.getValueAt(row, 5).toString();

        if (currentStatus.equalsIgnoreCase("Billing")) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("This patient has already completed their consultation.");
            return;
        }

        boolean hasWaitingAhead = false;
        for (int i = 0; i < row; i++) {
            String statusAbove = queueTable.getValueAt(i, 5).toString();
            if (statusAbove.equalsIgnoreCase("Waiting")) {
                hasWaitingAhead = true;
                break;
            }
        }

        if (hasWaitingAhead) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Please select the next patient in 'Waiting' status.");
            return;
        }

        String patientID = queueTable.getValueAt(row, 1).toString();
        String patientName = queueTable.getValueAt(row, 2).toString();
        String service = queueTable.getValueAt(row, 3).toString();
        String doctor = queueTable.getValueAt(row, 4).toString();

        Consultation conDialog = new Consultation(new javax.swing.JFrame(), true, userId, patientID, patientName, service, doctor);
        conDialog.setVisible(true);
        showQueueData();
    }//GEN-LAST:event_startConsultationActionPerformed

    private void billingQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billingQueueActionPerformed
        // 1. Get selected row
        row = queueTable.getSelectedRow();
        if (row == -1) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Please select a patient to start billing");
            return;
        }

        String patientID = queueTable.getValueAt(row, 1).toString();
        String patientName = queueTable.getValueAt(row, 2).toString();
        String serviceName = queueTable.getValueAt(row, 3).toString();
        String status = queueTable.getValueAt(row, 5).toString();

        int realPatientID = Integer.parseInt(patientID.replace("P-", ""));
        if (status.equalsIgnoreCase("Waiting")) {
            queueValidator.setForeground(Color.red);
            queueValidator.setText("Please start consultation first");
            return;
        }

        try {
            int patientAge = 0;
            int serviceID = 0;

            String ageSql = "SELECT Age FROM Patient WHERE PatientID = ?";
            ps = conn.prepareStatement(ageSql);
            ps.setInt(1, realPatientID);
            rs = ps.executeQuery();
            if (rs.next()) {
                patientAge = rs.getInt("Age");
            }

            String serviceSql = "SELECT ServiceID FROM Service WHERE ServiceName = ?";
            ps = conn.prepareStatement(serviceSql);
            ps.setString(1, serviceName);
            rs = ps.executeQuery();
            if (rs.next()) {
                serviceID = rs.getInt("ServiceID");
            }

            Billing billingDialog = new Billing(this, true, userId, realPatientID, patientName, patientAge, serviceID);
            billingDialog.setVisible(true);
            showQueueData();
            showSupplyData();
            queueValidator.setText("");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_billingQueueActionPerformed

    private void patientContactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_patientContactFocusGained
        // TODO add your handling code here:
        if (patientContact.getText().trim().isEmpty()) {
            patientContact.setText("09");
        }
    }//GEN-LAST:event_patientContactFocusGained

    private void patientContactFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_patientContactFocusLost
        // TODO add your handling code here:
        if (patientContact.getText().trim().equals("09")) {
            patientContact.setText("");
        }
    }//GEN-LAST:event_patientContactFocusLost

    private void patientContactKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_patientContactKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || patientContact.getText().length() >= 11) {
            evt.consume();
        }
    }//GEN-LAST:event_patientContactKeyTyped

    private void printSuppliesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSuppliesActionPerformed
        // TODO add your handling code here:
        try {
            JasperReport studentReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Angelo Javier\\Downloads\\JaspersoftWorkspace\\MyReports\\Supply.jasper");
            JasperPrint reportPrint = JasperFillManager.fillReport(studentReport, null, conn);
            JasperViewer.viewReport(reportPrint, false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_printSuppliesActionPerformed

    private void printServicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printServicesActionPerformed
        // TODO add your handling code here:
        try {
            JasperReport studentReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Angelo Javier\\Downloads\\JaspersoftWorkspace\\MyReports\\Service.jasper");
            JasperPrint reportPrint = JasperFillManager.fillReport(studentReport, null, conn);
            JasperViewer.viewReport(reportPrint, false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_printServicesActionPerformed
    private void clearPatient() {
        patientFName.setText("");
        patientLName.setText("");
        patientSex.setSelectedIndex(0);
        patientAge.setText("");
        patientContact.setText("");
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPatient;
    private javax.swing.JButton addPatientQueue;
    private javax.swing.JButton addToQueue;
    private javax.swing.JButton billingQueue;
    private javax.swing.JLayeredPane cardPane;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField patientAge;
    private javax.swing.JTextField patientContact;
    private javax.swing.JTextField patientFName;
    private javax.swing.JTextField patientHeight;
    private javax.swing.JTextField patientLName;
    private javax.swing.JComboBox<String> patientSex;
    private javax.swing.JTable patientTable;
    public static javax.swing.JLabel patientValidator;
    private javax.swing.JTextField patientWeight;
    private javax.swing.JButton printServices;
    private javax.swing.JButton printSupplies;
    private javax.swing.JPanel queuePanel;
    private javax.swing.JTable queueTable;
    public static javax.swing.JLabel queueValidator;
    private javax.swing.JButton recordsButton;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JTextField searchPatient;
    private javax.swing.JLabel searchPatientValidation;
    private javax.swing.JTextField searchService;
    private javax.swing.JLabel searchServiceValidation;
    private javax.swing.JTextField searchSupply;
    private javax.swing.JLabel searchSupplyValidation;
    private javax.swing.JPanel servicePanel;
    private javax.swing.JTable serviceTable;
    private javax.swing.JLabel serviceValidator;
    private javax.swing.JButton servicesButton;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JButton skipQueue;
    private javax.swing.JButton startConsultation;
    private javax.swing.JButton suppliesButton;
    private javax.swing.JPanel suppliesPanel;
    private javax.swing.JTable suppliesTable;
    private javax.swing.JLabel supplyValidator;
    private javax.swing.JButton updatePatient;
    // End of variables declaration//GEN-END:variables
}
