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
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.Timestamp;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Angelo Javier
 */
public class AdminForm extends javax.swing.JFrame {

    ResultSet rs;
    Connection conn;
    PreparedStatement ps;
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();

    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/Icon.png"));
    Image iconImage = imageIcon.getImage();
    private TableRowSorter<DefaultTableModel> sorter;

    String id;
    int row;
    String oldUsername;
    String userId;

    /**
     * Creates new form AdminForm
     */
    public AdminForm(String id) {
        initComponents();
        this.setTitle("Admin - " + id);
        this.setIconImage(iconImage);
        this.setLocationRelativeTo(null);
        this.userId = id;

        conn = Database.sqlConnect();
        center.setHorizontalAlignment(JLabel.CENTER);

        // show data
        showDashboardData();
        showReportData();
        showServiceData();
        showSupplyData();
        showPatientData();
        showStaffData();

        // table setup
        setupReportTable(reportTable);
        setupTable(serviceTable);
        setupSuppliesTable(suppliesTable);
        setupTable(patientTable);
        setupTable(staffTable);

        // button icons
        setButtonIcon(dashboardButton, "src\\image\\Home.png");
        setButtonIcon(servicesButton, "src\\image\\Service.png");
        setButtonIcon(suppliesButton, "src\\image\\Supplies.png");
        setButtonIcon(recordsButton, "src\\image\\Records.png");
        setButtonIcon(staffButton, "src\\image\\Staff.png");

        // label icons
        setLabelIcon(jLabel14, "src\\image\\Supplies.png");
        setLabelIcon(jLabel10, "src\\image\\Service.png");
        setLabelIcon(jLabel20, "src\\image\\Records.png");
        setLabelIcon(jLabel24, "src\\image\\Staff.png");
        setLabelIcon(suppliesCount, "src\\image\\Supplies.png");
        setLabelIcon(patientCount, "src\\image\\patients.png");

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

        searchStaff.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchStaffData();
            }

            public void removeUpdate(DocumentEvent e) {
                searchStaffData();
            }

            public void changedUpdate(DocumentEvent e) {
                searchStaffData();
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

    private void searchStaffData() {
        try {
            String keywordInput = searchStaff.getText().trim();
            if (keywordInput.isEmpty()) {
                showStaffData();
                searchStaffValidation.setText("");
                return;
            }

            String keyword = "%" + keywordInput + "%";
            ps = conn.prepareStatement(
                    "SELECT * FROM StaffAccount WHERE "
                    + "CONCAT('S-', LPAD(StaffID, 3, '0')) LIKE ? OR "
                    + "Username LIKE ? OR "
                    + "FullName LIKE ? OR "
                    + "Password LIKE ?"
            );

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            rs = ps.executeQuery();

            DefaultTableModel dm = (DefaultTableModel) staffTable.getModel();
            dm.setRowCount(0);

            boolean found = false;
            while (rs.next()) {
                found = true;
                Vector<Object> v1 = new Vector<>();
                int staffID = rs.getInt("StaffID");
                v1.add(String.format("S-%03d", staffID));
                v1.add(rs.getString("Username"));
                v1.add(rs.getString("FullName"));
                v1.add(rs.getString("Password"));
                dm.addRow(v1);
            }
            if (!found) {
                searchStaffValidation.setForeground(Color.red);
                searchStaffValidation.setText("No result found");
            } else {
                searchStaffValidation.setText("");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addReport(String userId, String actionType, String details) {
        try {
            String sql = "INSERT INTO Reports (UserID, ActionType, Details, DateAndTime) VALUES (?, ?, ?, CURRENT_TIMESTAMP(0))";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, actionType);
            ps.setString(3, details);
            ps.executeUpdate();
            showReportData();
        } catch (SQLException ex) {
            System.out.println("E: " + ex);
        }
    }

    private void showDashboardData() {
        try {
            ps = conn.prepareStatement("SELECT PatientRegistered, SuppliesSold, Earnings FROM Dashboard LIMIT 1");
            rs = ps.executeQuery();

            if (rs.next()) {
                patientCount.setText(String.valueOf(rs.getInt("PatientRegistered")));
                suppliesCount.setText(String.valueOf(rs.getInt("SuppliesSold")));
                earningsCount.setText(String.format("₱%,.2f", rs.getDouble("Earnings")));
            } else {
                patientCount.setText("0");
                suppliesCount.setText("0");
                earningsCount.setText("₱0.00");
            }
        } catch (SQLException e) {
            System.out.println("Dashboard Error: " + e.getMessage());
        }
    }

    private void showReportData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM Reports order by DateAndTime desc");
            rs = ps.executeQuery();
            DefaultTableModel dm = (DefaultTableModel) reportTable.getModel();
            dm.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(rs.getString("UserID"));
                v1.add(rs.getString("ActionType"));
                v1.add(rs.getString("Details"));
                Timestamp ts = rs.getTimestamp("DateAndTime");
                v1.add(sdf.format(ts));
                dm.addRow(v1);
            }
        } catch (SQLException ex) {
            System.out.println("" + ex);
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
            System.out.println("Patient: " + ex);
        }
    }

    private void showStaffData() {
        try {
            ps = conn.prepareStatement("SELECT * FROM StaffAccount");
            rs = ps.executeQuery();
            DefaultTableModel dm = (DefaultTableModel) staffTable.getModel();
            dm.setRowCount(0);
            while (rs.next()) {
                Vector<Object> v1 = new Vector<>();
                v1.add(String.format("S-%03d", rs.getInt("StaffID")));
                v1.add(rs.getString("Username"));
                v1.add(rs.getString("FullName"));
                v1.add(rs.getString("Password"));
                dm.addRow(v1);
            }
        } catch (SQLException ex) {
            System.out.println("" + ex);
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

    private void setupReportTable(JTable table) {
        table.setRowHeight(60);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.decode("#3C4753"));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(Color.WHITE);
        for (int i = 0; i < reportTable.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
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
        dashboardButton = new javax.swing.JButton();
        servicesButton = new javax.swing.JButton();
        suppliesButton = new javax.swing.JButton();
        recordsButton = new javax.swing.JButton();
        staffButton = new javax.swing.JButton();
        cardPane = new javax.swing.JLayeredPane();
        dashboardPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        earningsPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        earningsCount = new javax.swing.JLabel();
        patientPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        patientCount = new javax.swing.JLabel();
        soldPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        suppliesCount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        servicePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        searchService = new javax.swing.JTextField();
        searchServiceValidation = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        servicePrice = new javax.swing.JTextField();
        serviceName = new javax.swing.JTextField();
        deleteService = new javax.swing.JButton();
        addService = new javax.swing.JButton();
        updateService = new javax.swing.JButton();
        serviceValidator = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        printServices = new javax.swing.JButton();
        suppliesPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        searchSupply = new javax.swing.JTextField();
        searchSupplyValidation = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        suppliesTable = new javax.swing.JTable();
        supplyValidator = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        supplyCost = new javax.swing.JTextField();
        brandName = new javax.swing.JTextField();
        deleteSupply = new javax.swing.JButton();
        addSupply = new javax.swing.JButton();
        updateSupply = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        supplyQuantity = new javax.swing.JTextField();
        supplyCategory = new javax.swing.JTextField();
        supplyMeasurement = new javax.swing.JTextField();
        genericName = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        printSupplies = new javax.swing.JButton();
        recordsPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        searchPatient = new javax.swing.JTextField();
        viewHistory = new javax.swing.JButton();
        searchPatientValidation = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        patientTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        printPatientList = new javax.swing.JButton();
        staffPanel = new javax.swing.JPanel();
        searchStaffValidation = new javax.swing.JLabel();
        staffValidator = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        searchStaff = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        staffTable = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        staffFName = new javax.swing.JTextField();
        deleteStaff = new javax.swing.JButton();
        addStaff = new javax.swing.JButton();
        updateStaff = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        staffLName = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        staffUsername = new javax.swing.JTextField();
        showPasswordCheck = new javax.swing.JCheckBox();
        staffPassword = new javax.swing.JPasswordField();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();

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

        dashboardButton.setBackground(new java.awt.Color(51, 153, 255));
        dashboardButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dashboardButton.setForeground(new java.awt.Color(255, 255, 255));
        dashboardButton.setText("Dashboard");
        dashboardButton.setBorderPainted(false);
        dashboardButton.setFocusPainted(false);
        dashboardButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        dashboardButton.setIconTextGap(15);
        dashboardButton.setOpaque(true);
        dashboardButton.setVerifyInputWhenFocusTarget(false);
        dashboardButton.addActionListener(this::dashboardButtonActionPerformed);
        sidePanel.add(dashboardButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 50));

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
        recordsButton.setText("Patient Records");
        recordsButton.setBorderPainted(false);
        recordsButton.setFocusPainted(false);
        recordsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        recordsButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        recordsButton.setIconTextGap(15);
        recordsButton.setOpaque(true);
        recordsButton.addActionListener(this::recordsButtonActionPerformed);
        sidePanel.add(recordsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 210, 50));

        staffButton.setBackground(new java.awt.Color(41, 41, 41));
        staffButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        staffButton.setForeground(new java.awt.Color(255, 255, 255));
        staffButton.setText("Staff Management");
        staffButton.setBorderPainted(false);
        staffButton.setFocusPainted(false);
        staffButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        staffButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        staffButton.setIconTextGap(15);
        staffButton.setOpaque(true);
        staffButton.addActionListener(this::staffButtonActionPerformed);
        sidePanel.add(staffButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 210, 50));

        getContentPane().add(sidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 660));

        cardPane.setLayout(new java.awt.CardLayout());

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));
        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("System Activity Reports");
        dashboardPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        reportTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "Action Type", "Details", "Date & Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reportTable.getTableHeader().setResizingAllowed(false);
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportTable.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(reportTable);
        if (reportTable.getColumnModel().getColumnCount() > 0) {
            reportTable.getColumnModel().getColumn(0).setMinWidth(80);
            reportTable.getColumnModel().getColumn(1).setMinWidth(120);
            reportTable.getColumnModel().getColumn(2).setMinWidth(300);
            reportTable.getColumnModel().getColumn(3).setMinWidth(150);
        }

        dashboardPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 1000, 380));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel5.setText("Dashboard");
        dashboardPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        earningsPanel.setBackground(new java.awt.Color(255, 255, 255));
        earningsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        earningsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Total Earnings Today");
        earningsPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        earningsCount.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        earningsCount.setForeground(new java.awt.Color(76, 175, 80));
        earningsCount.setText("₱0.00");
        earningsPanel.add(earningsCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 250, -1));

        dashboardPanel.add(earningsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 60, 280, 130));

        patientPanel.setBackground(new java.awt.Color(255, 255, 255));
        patientPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        patientPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Patient Registered Today");
        patientPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        patientCount.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        patientCount.setForeground(new java.awt.Color(47, 123, 238));
        patientCount.setText("0");
        patientPanel.add(patientCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 230, -1));

        dashboardPanel.add(patientPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 280, 130));

        soldPanel.setBackground(new java.awt.Color(255, 255, 255));
        soldPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        soldPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Supplies Sold Today");
        soldPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        suppliesCount.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        suppliesCount.setForeground(new java.awt.Color(255, 179, 0));
        suppliesCount.setText("0");
        soldPanel.add(suppliesCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 220, -1));

        dashboardPanel.add(soldPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 280, 130));

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        dashboardPanel.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 1000, 3));

        cardPane.add(dashboardPanel, "card2");

        servicePanel.setBackground(new java.awt.Color(245, 245, 245));
        servicePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Search :");
        servicePanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 48, -1, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel10.setText("Services Management");
        servicePanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 430, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Manage Service Details");
        servicePanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, -1, 30));
        servicePanel.add(searchService, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 200, 30));

        searchServiceValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchServiceValidation.setForeground(new java.awt.Color(255, 51, 0));
        searchServiceValidation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        servicePanel.add(searchServiceValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 250, 380, 80));

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
        serviceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serviceTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(serviceTable);
        if (serviceTable.getColumnModel().getColumnCount() > 0) {
            serviceTable.getColumnModel().getColumn(0).setResizable(false);
            serviceTable.getColumnModel().getColumn(1).setResizable(false);
            serviceTable.getColumnModel().getColumn(2).setResizable(false);
        }

        servicePanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 400));

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Service Name :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Service Price :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));
        jPanel1.add(servicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 45, 160, -1));
        jPanel1.add(serviceName, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 160, -1));

        deleteService.setBackground(new java.awt.Color(255, 51, 51));
        deleteService.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteService.setForeground(new java.awt.Color(255, 255, 255));
        deleteService.setText("Delete Selected");
        deleteService.addActionListener(this::deleteServiceActionPerformed);
        jPanel1.add(deleteService, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 30, 150, 30));

        addService.setBackground(new java.awt.Color(51, 153, 255));
        addService.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addService.setForeground(new java.awt.Color(255, 255, 255));
        addService.setText("Add New Service");
        addService.addActionListener(this::addServiceActionPerformed);
        jPanel1.add(addService, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 150, 30));

        updateService.setBackground(new java.awt.Color(255, 153, 0));
        updateService.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateService.setForeground(new java.awt.Color(255, 255, 255));
        updateService.setText("Update Service");
        updateService.addActionListener(this::updateServiceActionPerformed);
        jPanel1.add(updateService, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 30, 150, 30));

        servicePanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 1000, 80));

        serviceValidator.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        serviceValidator.setForeground(new java.awt.Color(255, 51, 0));
        serviceValidator.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        servicePanel.add(serviceValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 530, 550, 30));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        servicePanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 1000, 3));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        servicePanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

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

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Search :");
        suppliesPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 48, -1, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel14.setText("Supplies Mangement");
        suppliesPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 450, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Manage Supply Details");
        suppliesPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, -1, 30));
        suppliesPanel.add(searchSupply, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 200, 30));

        searchSupplyValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchSupplyValidation.setForeground(new java.awt.Color(255, 51, 0));
        searchSupplyValidation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        suppliesPanel.add(searchSupplyValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, 380, 80));

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
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class
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
        suppliesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliesTableMouseClicked(evt);
            }
        });
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

        suppliesPanel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 310));

        supplyValidator.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        supplyValidator.setForeground(new java.awt.Color(255, 51, 0));
        supplyValidator.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        suppliesPanel.add(supplyValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 430, 530, 30));

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Unit Cost :");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Quantity :");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));
        jPanel2.add(supplyCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 160, -1));
        jPanel2.add(brandName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, -1));

        deleteSupply.setBackground(new java.awt.Color(255, 51, 51));
        deleteSupply.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteSupply.setForeground(new java.awt.Color(255, 255, 255));
        deleteSupply.setText("Delete Selected");
        deleteSupply.addActionListener(this::deleteSupplyActionPerformed);
        jPanel2.add(deleteSupply, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 140, 160, 30));

        addSupply.setBackground(new java.awt.Color(51, 153, 255));
        addSupply.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addSupply.setForeground(new java.awt.Color(255, 255, 255));
        addSupply.setText("Add New Supplies");
        addSupply.addActionListener(this::addSupplyActionPerformed);
        jPanel2.add(addSupply, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, 160, 30));

        updateSupply.setBackground(new java.awt.Color(255, 153, 0));
        updateSupply.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateSupply.setForeground(new java.awt.Color(255, 255, 255));
        updateSupply.setText("Update Supplies");
        updateSupply.addActionListener(this::updateSupplyActionPerformed);
        jPanel2.add(updateSupply, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 80, 160, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Brand Name :");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel2.add(supplyQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 160, -1));
        jPanel2.add(supplyCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 160, -1));
        jPanel2.add(supplyMeasurement, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, -1));
        jPanel2.add(genericName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 160, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Category :");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Generic Name :");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Measurement :");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        suppliesPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 1000, 190));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        suppliesPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 1000, 3));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        suppliesPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

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
        jLabel19.setText("Search Patient :");
        recordsPanel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, -1, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel20.setText("Patient Records");
        recordsPanel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 360, -1));
        recordsPanel.add(searchPatient, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 48, 200, 30));

        viewHistory.setBackground(new java.awt.Color(0, 153, 153));
        viewHistory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewHistory.setForeground(new java.awt.Color(255, 255, 255));
        viewHistory.setText("View History");
        viewHistory.setFocusable(false);
        viewHistory.addActionListener(this::viewHistoryActionPerformed);
        recordsPanel.add(viewHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 210, 30));

        searchPatientValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchPatientValidation.setForeground(new java.awt.Color(255, 51, 0));
        searchPatientValidation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchPatientValidation.setFocusable(false);
        searchPatientValidation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        recordsPanel.add(searchPatientValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 300, 970, 80));

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
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
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

        recordsPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 540));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        recordsPanel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

        printPatientList.setBackground(new java.awt.Color(0, 153, 153));
        printPatientList.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printPatientList.setForeground(new java.awt.Color(255, 255, 255));
        printPatientList.setText("Print Patient List");
        printPatientList.setFocusable(false);
        printPatientList.addActionListener(this::printPatientListActionPerformed);
        recordsPanel.add(printPatientList, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 15, 210, 30));

        cardPane.add(recordsPanel, "card3");

        staffPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchStaffValidation.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        searchStaffValidation.setForeground(new java.awt.Color(255, 51, 0));
        staffPanel.add(searchStaffValidation, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 230, 380, 80));

        staffValidator.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        staffValidator.setForeground(new java.awt.Color(255, 51, 0));
        staffValidator.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        staffPanel.add(staffValidator, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 480, 490, 30));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("Search Staff (Username or Name):");
        staffPanel.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 48, -1, 30));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel24.setText("Staff Management");
        staffPanel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 420, -1));
        staffPanel.add(searchStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 50, 200, 30));

        staffTable.setBackground(new java.awt.Color(245, 245, 245));
        staffTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        staffTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "StaffID", "Username", "Full Name", "Password"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        staffTable.getTableHeader().setResizingAllowed(false);
        staffTable.getTableHeader().setReorderingAllowed(false);
        staffTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                staffTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(staffTable);
        if (staffTable.getColumnModel().getColumnCount() > 0) {
            staffTable.getColumnModel().getColumn(0).setResizable(false);
            staffTable.getColumnModel().getColumn(1).setResizable(false);
            staffTable.getColumnModel().getColumn(2).setResizable(false);
            staffTable.getColumnModel().getColumn(3).setResizable(false);
        }

        staffPanel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 350));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setText("Manage Staff Details");
        staffPanel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, -1, 30));

        jPanel3.setBackground(new java.awt.Color(245, 245, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Password :");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Last Name :");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel3.add(staffFName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 160, -1));

        deleteStaff.setBackground(new java.awt.Color(255, 51, 51));
        deleteStaff.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteStaff.setForeground(new java.awt.Color(255, 255, 255));
        deleteStaff.setText("Delete Selected");
        deleteStaff.addActionListener(this::deleteStaffActionPerformed);
        jPanel3.add(deleteStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 50, 160, 30));

        addStaff.setBackground(new java.awt.Color(51, 153, 255));
        addStaff.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addStaff.setForeground(new java.awt.Color(255, 255, 255));
        addStaff.setText("Add Staff");
        addStaff.addActionListener(this::addStaffActionPerformed);
        jPanel3.add(addStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 160, 30));

        updateStaff.setBackground(new java.awt.Color(255, 153, 0));
        updateStaff.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateStaff.setForeground(new java.awt.Color(255, 255, 255));
        updateStaff.setText("Update Selected");
        updateStaff.addActionListener(this::updateStaffActionPerformed);
        jPanel3.add(updateStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, 160, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("First Name :");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel3.add(staffLName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 160, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("Username :");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        jPanel3.add(staffUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 160, -1));

        showPasswordCheck.setText("Show Password");
        showPasswordCheck.addActionListener(this::showPasswordCheckActionPerformed);
        jPanel3.add(showPasswordCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));
        jPanel3.add(staffPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 160, -1));

        staffPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 1000, 130));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        staffPanel.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1000, 3));

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        staffPanel.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 1000, 3));

        cardPane.add(staffPanel, "card6");

        getContentPane().add(cardPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 1020, 660));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardButtonActionPerformed
        // TODO add your handling code here:
        dashboardPanel.setVisible(true);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(false);
        staffPanel.setVisible(false);

        dashboardButton.setBackground(Color.decode("#3399FF"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#292929"));
        staffButton.setBackground(Color.decode("#292929"));

        serviceValidator.setText("");
        supplyValidator.setText("");
        staffValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");
        searchStaff.setText("");

        reportTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();
        staffTable.clearSelection();

        clearService();
        clearSupplies();
        clearStaff();
    }//GEN-LAST:event_dashboardButtonActionPerformed

    private void servicesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicesButtonActionPerformed
        // TODO add your handling code here:
        dashboardPanel.setVisible(false);
        servicePanel.setVisible(true);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(false);
        staffPanel.setVisible(false);

        dashboardButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#3399FF"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#292929"));
        staffButton.setBackground(Color.decode("#292929"));

        serviceValidator.setText("");
        supplyValidator.setText("");
        staffValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");
        searchStaff.setText("");

        reportTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();
        staffTable.clearSelection();

        clearService();
        clearSupplies();
        clearStaff();
    }//GEN-LAST:event_servicesButtonActionPerformed

    private void suppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesButtonActionPerformed
        // TODO add your handling code here:
        dashboardPanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(true);
        recordsPanel.setVisible(false);
        staffPanel.setVisible(false);

        dashboardButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#3399FF"));
        recordsButton.setBackground(Color.decode("#292929"));
        staffButton.setBackground(Color.decode("#292929"));

        serviceValidator.setText("");
        supplyValidator.setText("");
        staffValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");
        searchStaff.setText("");

        reportTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();
        staffTable.clearSelection();

        clearService();
        clearSupplies();
        clearStaff();
    }//GEN-LAST:event_suppliesButtonActionPerformed

    private void recordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordsButtonActionPerformed
        // TODO add your handling code here:
        dashboardPanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(true);
        staffPanel.setVisible(false);

        dashboardButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#3399FF"));
        staffButton.setBackground(Color.decode("#292929"));

        serviceValidator.setText("");
        supplyValidator.setText("");
        staffValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");
        searchStaff.setText("");

        reportTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();
        staffTable.clearSelection();

        clearService();
        clearSupplies();
        clearStaff();
    }//GEN-LAST:event_recordsButtonActionPerformed

    private void staffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffButtonActionPerformed
        // TODO add your handling code here:
        dashboardPanel.setVisible(false);
        servicePanel.setVisible(false);
        suppliesPanel.setVisible(false);
        recordsPanel.setVisible(false);
        staffPanel.setVisible(true);

        dashboardButton.setBackground(Color.decode("#292929"));
        servicesButton.setBackground(Color.decode("#292929"));
        suppliesButton.setBackground(Color.decode("#292929"));
        recordsButton.setBackground(Color.decode("#292929"));
        staffButton.setBackground(Color.decode("#3399FF"));

        serviceValidator.setText("");
        supplyValidator.setText("");
        staffValidator.setText("");

        searchService.setText("");
        searchSupply.setText("");
        searchPatient.setText("");
        searchStaff.setText("");

        reportTable.clearSelection();
        serviceTable.clearSelection();
        suppliesTable.clearSelection();
        patientTable.clearSelection();
        staffTable.clearSelection();

        clearService();
        clearSupplies();
        clearStaff();
    }//GEN-LAST:event_staffButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log out Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            LoginForm form = new LoginForm();
            form.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void addSupplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplyActionPerformed
        // TODO add your handling code here:
        try {
            if (brandName.getText().isEmpty()
                    || genericName.getText().isEmpty()
                    || supplyMeasurement.getText().isEmpty()
                    || supplyCategory.getText().isEmpty()
                    || supplyQuantity.getText().isEmpty()
                    || supplyCost.getText().isEmpty()) {
                supplyValidator.setForeground(Color.red);
                supplyValidator.setText("Please fill in fields to add supply");
                return;
            }
            String measurement = supplyMeasurement.getText() + " " + "mL";
            ps = conn.prepareStatement("insert into Supply(BrandName, GenericName, Measurement, Category, Quantity, UnitCost) values (?,?,?,?,?,?)");
            ps.setString(1, brandName.getText());
            ps.setString(2, genericName.getText());
            ps.setString(3, measurement);
            ps.setString(4, supplyCategory.getText());
            ps.setInt(5, Integer.parseInt(supplyQuantity.getText()));
            ps.setDouble(6, Double.parseDouble(supplyCost.getText()));

            if (ps.executeUpdate() == 1) {
                supplyValidator.setForeground(Color.green);
                supplyValidator.setText("Successfully Added");
                showSupplyData();
                addReport(userId, "Add Supply", "Added Supply: " + brandName.getText() + " " + genericName.getText() + "\nQuantity: " + supplyQuantity.getText() + "\nUnit Cost: " + supplyCost.getText());
                clearSupplies();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (NumberFormatException ex) {
            supplyValidator.setForeground(Color.red);
            supplyValidator.setText("Please enter numbers only on Quantity and Unit Cost fields");
        }
    }//GEN-LAST:event_addSupplyActionPerformed

    private void deleteServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteServiceActionPerformed
        // TODO add your handling code here:
        try {
            row = serviceTable.getSelectedRow();

            if (row == -1) {
                serviceValidator.setForeground(Color.red);
                serviceValidator.setText("Please select a service first to delete");
                return;
            }
            String displayedID = serviceTable.getModel().getValueAt(row, 0).toString();
            int serviceID = displayedID.startsWith("SR-")
                    ? Integer.parseInt(displayedID.replace("SR-", ""))
                    : Integer.parseInt(displayedID);

            ps = conn.prepareStatement("DELETE FROM Service WHERE ServiceID = ?");
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ID = " + id + "?", "Delete confirmation", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                ps.setInt(1, serviceID);
                ps.executeUpdate();
                serviceValidator.setForeground(Color.green);
                serviceValidator.setText("Successfully Deleted");
                showServiceData();
                addReport(userId, "Delete Service", "Deleted Service: " + serviceName.getText() + "\nPrice: " + servicePrice.getText());
                clearService();
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_deleteServiceActionPerformed

    private void deleteSupplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplyActionPerformed
        // TODO add your handling code here:
        try {
            row = suppliesTable.getSelectedRow();

            if (row == -1) {
                supplyValidator.setForeground(Color.red);
                supplyValidator.setText("Please select a supply first to delete");
                return;
            }
            String displayedID = suppliesTable.getModel().getValueAt(row, 0).toString();
            int supplyID = displayedID.startsWith("SP-")
                    ? Integer.parseInt(displayedID.replace("SP-", ""))
                    : Integer.parseInt(displayedID);

            ps = conn.prepareStatement("DELETE FROM Supply WHERE SupplyID = ?");
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ID = " + id + "?", "Delete confirmation", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                ps.setInt(1, supplyID);
                ps.executeUpdate();
                supplyValidator.setForeground(Color.green);
                supplyValidator.setText("Successfully Deleted");
                showSupplyData();
                addReport(userId, "Delete Supply", "Deleted Supply: " + brandName.getText() + " " + genericName.getText() + "\nQuantity: " + supplyQuantity.getText() + "\nUnit Cost: " + supplyCost.getText());
                clearSupplies();
            }
        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }//GEN-LAST:event_deleteSupplyActionPerformed

    private void suppliesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliesTableMouseClicked
        // TODO add your handling code here:
        try {
            row = suppliesTable.getSelectedRow();
            id = String.valueOf(suppliesTable.getModel().getValueAt(row, 0));
            int supplyID;
            if (id.startsWith("SP-")) {
                supplyID = Integer.parseInt(id.replace("SP-", ""));
            } else {
                supplyID = Integer.parseInt(id);
            }
            ps = conn.prepareStatement("SELECT * FROM Supply WHERE SupplyID = ?");
            ps.setInt(1, supplyID);
            rs = ps.executeQuery();
            if (rs.next()) {
                brandName.setText(rs.getString("BrandName"));
                genericName.setText(rs.getString("GenericName"));
                supplyMeasurement.setText(rs.getString("Measurement"));
                supplyCategory.setText(rs.getString("Category"));
                supplyQuantity.setText(String.valueOf(rs.getInt("Quantity")));
                supplyCost.setText(String.valueOf(rs.getDouble("UnitCost")));
            }
        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }//GEN-LAST:event_suppliesTableMouseClicked

    private void addServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addServiceActionPerformed
        // TODO add your handling code here:
        try {
            if (serviceName.getText().isEmpty()
                    || servicePrice.getText().isEmpty()) {
                serviceValidator.setForeground(Color.red);
                serviceValidator.setText("Please fill in fields to add service");
                return;
            }

            ps = conn.prepareStatement("insert into Service(ServiceName, Price) values (?,?)");
            ps.setString(1, serviceName.getText());
            ps.setInt(2, Integer.parseInt(servicePrice.getText()));
            if (ps.executeUpdate() == 1) {
                serviceValidator.setForeground(Color.green);
                serviceValidator.setText("Successfully Added");
                showServiceData();
                addReport(userId, "Add Service", "Added Service: " + serviceName.getText() + "\nPrice: " + servicePrice.getText());
                clearService();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (NumberFormatException ex) {
            serviceValidator.setForeground(Color.red);
            serviceValidator.setText("Please enter numbers only on Price fields");
        }
    }//GEN-LAST:event_addServiceActionPerformed

    private void updateServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateServiceActionPerformed
        // TODO add your handling code here:
        try {
            row = serviceTable.getSelectedRow();

            if (row == -1) {
                serviceValidator.setForeground(Color.red);
                serviceValidator.setText("Please select a service first to update");
                return;
            }
            String displayedID = serviceTable.getModel().getValueAt(row, 0).toString();
            int serviceID = displayedID.startsWith("SR-")
                    ? Integer.parseInt(displayedID.replace("SR-", ""))
                    : Integer.parseInt(displayedID);

            ps = conn.prepareStatement("UPDATE Service Set ServiceName = ?, Price = ? WHERE ServiceID = ?");
            ps.setString(1, serviceName.getText());
            ps.setInt(2, Integer.parseInt(servicePrice.getText()));
            ps.setInt(3, serviceID);

            if (ps.executeUpdate() == 1) {
                serviceValidator.setForeground(Color.green);
                serviceValidator.setText("Successfully Updated");
                addReport(userId, "Update Service", "Updated Service: " + serviceName.getText() + "\nPrice: " + servicePrice.getText());
                showServiceData();
                clearService();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (NumberFormatException e) {
            serviceValidator.setForeground(Color.red);
            serviceValidator.setText("Please enter numbers only on Price fields");
        }
    }//GEN-LAST:event_updateServiceActionPerformed

    private void updateSupplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateSupplyActionPerformed
        // TODO add your handling code here:
        try {
            row = suppliesTable.getSelectedRow();

            if (row == -1) {
                supplyValidator.setForeground(Color.red);
                supplyValidator.setText("Please select a supply first to update");
                return;
            }
            String displayedID = suppliesTable.getModel().getValueAt(row, 0).toString();
            int supplyID = displayedID.startsWith("SP-")
                    ? Integer.parseInt(displayedID.replace("SP-", ""))
                    : Integer.parseInt(displayedID);
            
            ps = conn.prepareStatement("UPDATE Supply Set BrandName = ?, GenericName = ?, Measurement = ?, Category = ?, Quantity = ?, UnitCost = ? WHERE SupplyID = ?");
            ps.setString(1, brandName.getText());
            ps.setString(2, genericName.getText());
            ps.setString(3, supplyMeasurement.getText());
            ps.setString(4, supplyCategory.getText());
            ps.setInt(5, Integer.parseInt(supplyQuantity.getText()));
            ps.setDouble(6, Double.parseDouble(supplyCost.getText()));
            ps.setInt(7, supplyID);

            if (ps.executeUpdate() == 1) {
                supplyValidator.setForeground(Color.green);
                supplyValidator.setText("Successfully Updated");
                addReport(userId, "Update Supply", "Updated Supply: " + brandName.getText() + " " + genericName.getText() + "\nQuantity: " + supplyQuantity.getText() + "\nCost: " + supplyCost.getText());
                showSupplyData();
                clearSupplies();
            }
        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error updating supplies" + e);
        } catch (NumberFormatException ex) {
            supplyValidator.setForeground(Color.red);
            supplyValidator.setText("Please enter numbers only on Quantity and Unit Cost fields");
        }
    }//GEN-LAST:event_updateSupplyActionPerformed

    private void serviceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serviceTableMouseClicked
        // TODO add your handling code here:
        try {
            row = serviceTable.getSelectedRow();
            id = String.valueOf(serviceTable.getModel().getValueAt(row, 0));
            int serviceID;
            if (id.startsWith("SR-")) {
                serviceID = Integer.parseInt(id.replace("SR-", ""));
            } else {
                serviceID = Integer.parseInt(id);
            }
            ps = conn.prepareStatement("SELECT * FROM Service WHERE ServiceID = ?");
            ps.setInt(1, serviceID);
            rs = ps.executeQuery();
            if (rs.next()) {
                serviceName.setText(rs.getString("ServiceName"));
                servicePrice.setText(String.valueOf(rs.getInt("Price")));
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_serviceTableMouseClicked

    private void deleteStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStaffActionPerformed
        try {
            row = staffTable.getSelectedRow();
            if (row == -1) {
                staffValidator.setForeground(Color.red);
                staffValidator.setText("Please select a staff account first to delete");
                return;
            }

            String displayedID = staffTable.getModel().getValueAt(row, 0).toString();
            int staffID = displayedID.startsWith("S-")
                    ? Integer.parseInt(displayedID.replace("S-", ""))
                    : Integer.parseInt(displayedID);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete ID = " + displayedID + "?",
                    "Delete confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                ps = conn.prepareStatement("DELETE FROM StaffAccount WHERE StaffID = ?");
                ps.setInt(1, staffID);
                ps.executeUpdate();
                staffValidator.setForeground(Color.green);
                staffValidator.setText("Successfully Deleted");
                addReport(userId, "Delete Staff", "Deleted Staff: " + staffUsername.getText());
                showStaffData();
                clearStaff();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_deleteStaffActionPerformed

    private void addStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStaffActionPerformed
        // TODO add your handling code here:
        try {
            if (staffFName.getText().isEmpty()
                    || staffLName.getText().isEmpty()
                    || staffUsername.getText().isEmpty()
                    || staffPassword.getText().isEmpty()) {

                staffValidator.setForeground(Color.red);
                staffValidator.setText("Please fill in fields to add staff");
                return;
            }

            String fullName = staffFName.getText().trim() + " " + staffLName.getText().trim();
            String username = staffUsername.getText().trim();
            String plainPassword = staffPassword.getText().trim(); // Capture the plain text temporarily

            ps = conn.prepareStatement("SELECT COUNT(*) FROM StaffAccount WHERE Username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                staffValidator.setForeground(Color.red);
                staffValidator.setText("Username already exists");
                return;
            }

            if (plainPassword.length() < 8) {
                staffValidator.setForeground(Color.red);
                staffValidator.setText("Password must be at least 8 characters long");
                return;
            }

            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());

            ps = conn.prepareStatement("INSERT INTO StaffAccount(Username, FullName, Password) VALUES (?,?,?)");
            ps.setString(1, username);
            ps.setString(2, fullName);
            ps.setString(3, hashedPassword);

            if (ps.executeUpdate() == 1) {
                staffValidator.setForeground(Color.green);
                staffValidator.setText("Successfully Added");

                addReport(userId, "Add Staff", "Added Staff: " + username);

                showStaffData();
                clearStaff();
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }

    }//GEN-LAST:event_addStaffActionPerformed

    private void updateStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStaffActionPerformed
        try {
            row = staffTable.getSelectedRow();

            if (row == -1) {
                staffValidator.setForeground(Color.red);
                staffValidator.setText("Please select a staff first to update");
                return;
            }

            String displayedID = staffTable.getModel().getValueAt(row, 0).toString();
            int staffID = displayedID.startsWith("S-")
                    ? Integer.parseInt(displayedID.replace("S-", ""))
                    : Integer.parseInt(displayedID);

            String username = staffUsername.getText().trim();
            String fullName = staffFName.getText().trim() + " " + staffLName.getText().trim();
            String password = staffPassword.getText().trim();
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
            if (!username.equals(oldUsername)) {
                ps = conn.prepareStatement("SELECT COUNT(*) FROM StaffAccount WHERE Username = ? AND StaffID <> ?");
                ps.setString(1, username);
                ps.setInt(2, staffID);
                rs = ps.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    staffValidator.setForeground(Color.red);
                    staffValidator.setText("Username already exists");
                    return;
                }
            }

            if (staffPassword.getText().trim().length() < 8) {
                staffValidator.setForeground(Color.red);
                staffValidator.setText("Password must be at least 8 characters long");
                return;
            }

            ps = conn.prepareStatement("UPDATE StaffAccount SET Username = ?, FullName = ?, Password = ? WHERE StaffID = ?");
            ps.setString(1, username);
            ps.setString(2, fullName);
            ps.setString(3, hashedPassword);
            ps.setInt(4, staffID);

            if (ps.executeUpdate() == 1) {
                staffValidator.setForeground(Color.green);
                staffValidator.setText("Successfully Updated");

                addReport(userId, "Update Staff", "Updated Staff: " + username);

                showStaffData();
                clearStaff();
                oldUsername = "";
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_updateStaffActionPerformed

    private void staffTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffTableMouseClicked
        try {
            row = staffTable.getSelectedRow();
            if (row == -1) {
                return;
            }
            String displayedID = staffTable.getModel().getValueAt(row, 0).toString();
            int staffID;
            if (displayedID.startsWith("S-")) {
                staffID = Integer.parseInt(displayedID.replace("S-", ""));
            } else {
                staffID = Integer.parseInt(displayedID);
            }
            ps = conn.prepareStatement("SELECT * FROM StaffAccount WHERE StaffID = ?");
            ps.setInt(1, staffID);
            rs = ps.executeQuery();

            if (rs.next()) {
                oldUsername = rs.getString("Username");

                String fullName = rs.getString("FullName");
                String[] parts = fullName.split(" ", 2);
                staffFName.setText(parts[0]);
                staffLName.setText(parts.length > 1 ? parts[1] : "");

                staffUsername.setText(rs.getString("Username"));
                staffPassword.setText(rs.getString("Password"));
            }

        } catch (SQLException e) {
            System.out.println("" + e);
        }
    }//GEN-LAST:event_staffTableMouseClicked

    private void viewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewHistoryActionPerformed
        // TODO add your handling code here:
        row = patientTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a patient first to view history");
            return;
        }

        String displayedID = patientTable.getModel().getValueAt(row, 0).toString();
        PatientHistory history = new PatientHistory(this, true, displayedID);
        history.setVisible(true);
    }//GEN-LAST:event_viewHistoryActionPerformed

    private void showPasswordCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPasswordCheckActionPerformed
        // TODO add your handling code here:
        if (showPasswordCheck.isSelected()) {
            staffPassword.setEchoChar((char) 0);
        } else {
            staffPassword.setEchoChar('•');
        }
    }//GEN-LAST:event_showPasswordCheckActionPerformed

    private void printPatientListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPatientListActionPerformed
        // TODO add your handling code here:
        try {
            JasperReport studentReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Angelo Javier\\Downloads\\JaspersoftWorkspace\\MyReports\\PatientList.jasper");
            JasperPrint reportPrint = JasperFillManager.fillReport(studentReport, null, conn);
            JasperViewer.viewReport(reportPrint, false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_printPatientListActionPerformed

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

    private void clearSupplies() {
        brandName.setText("");
        genericName.setText("");
        supplyMeasurement.setText("");
        supplyCategory.setText("");
        supplyQuantity.setText("");
        supplyCost.setText("");
    }

    private void clearService() {
        serviceName.setText("");
        servicePrice.setText("");
    }

    private void clearStaff() {
        staffFName.setText("");
        staffLName.setText("");
        staffUsername.setText("");
        staffPassword.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        AdminForm admin = new AdminForm("Admin");
        admin.setVisible(true);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
//            logger.log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>
        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> new AdminForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addService;
    private javax.swing.JButton addStaff;
    private javax.swing.JButton addSupply;
    private javax.swing.JTextField brandName;
    private javax.swing.JLayeredPane cardPane;
    private javax.swing.JButton dashboardButton;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JButton deleteService;
    private javax.swing.JButton deleteStaff;
    private javax.swing.JButton deleteSupply;
    public static javax.swing.JLabel earningsCount;
    private javax.swing.JPanel earningsPanel;
    private javax.swing.JTextField genericName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton logoutButton;
    public static javax.swing.JLabel patientCount;
    private javax.swing.JPanel patientPanel;
    private javax.swing.JTable patientTable;
    private javax.swing.JButton printPatientList;
    private javax.swing.JButton printServices;
    private javax.swing.JButton printSupplies;
    private javax.swing.JButton recordsButton;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JTable reportTable;
    private javax.swing.JTextField searchPatient;
    private javax.swing.JLabel searchPatientValidation;
    private javax.swing.JTextField searchService;
    private javax.swing.JLabel searchServiceValidation;
    private javax.swing.JTextField searchStaff;
    private javax.swing.JLabel searchStaffValidation;
    private javax.swing.JTextField searchSupply;
    private javax.swing.JLabel searchSupplyValidation;
    private javax.swing.JTextField serviceName;
    private javax.swing.JPanel servicePanel;
    private javax.swing.JTextField servicePrice;
    private javax.swing.JTable serviceTable;
    private javax.swing.JLabel serviceValidator;
    private javax.swing.JButton servicesButton;
    private javax.swing.JCheckBox showPasswordCheck;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel soldPanel;
    private javax.swing.JButton staffButton;
    private javax.swing.JTextField staffFName;
    private javax.swing.JTextField staffLName;
    private javax.swing.JPanel staffPanel;
    private javax.swing.JPasswordField staffPassword;
    private javax.swing.JTable staffTable;
    private javax.swing.JTextField staffUsername;
    private javax.swing.JLabel staffValidator;
    private javax.swing.JButton suppliesButton;
    public static javax.swing.JLabel suppliesCount;
    private javax.swing.JPanel suppliesPanel;
    private javax.swing.JTable suppliesTable;
    private javax.swing.JTextField supplyCategory;
    private javax.swing.JTextField supplyCost;
    private javax.swing.JTextField supplyMeasurement;
    private javax.swing.JTextField supplyQuantity;
    private javax.swing.JLabel supplyValidator;
    private javax.swing.JButton updateService;
    private javax.swing.JButton updateStaff;
    private javax.swing.JButton updateSupply;
    private javax.swing.JButton viewHistory;
    // End of variables declaration//GEN-END:variables
}
