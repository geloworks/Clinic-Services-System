# Clinic-Services-System

## Overview
The Clinic Services System is a digital assistant designed to replace paper records in a clinic. Built using Java, it connects to a MySQL database to store and retrieve all essential information such as patient medical history, supplies stock, staff accounts, and available services. So that your clinic data remains safe and organized.

## Features
- **Admin CRUDS Operations:** Full control over users, services, and clinical data.
- **Stock Monitoring:** Real-time tracking of clinic inventory.
- **Patient Registration:** Digital intake and comprehensive medical history management.
- **Queue Management:** Efficient handling of patient appointments and flow.
- **Billing and Transactions:** Automated billing with professional receipt generation.

## Prerequisites
Before you begin, ensure you have met the following requirements:
- **JDK:** Version 8 or higher (Compatible with modern versions like JDK 21).
- **Database:** MySQL Server 8.0 or higher.
- **IDE:** Apache NetBeans (Version 28 recommended).
- **Reporting Tool:** Jaspersoft Studio (for editing .jrxml receipt templates).

## Setup Instructions
1. **Clone the repository**
   ```bash
   git clone https://github.com/geloworks/Clinic-Services-System.git
   ```
2. **Configure Libraries (JAR Files)**
- Open the project in NetBeans
- Right-click the Libraries folder in the Projects pane
- Select Add JAR/Folder
- Navigate to the /lib folder in the project directory and select all JAR files (MySQL Connector, JasperReports, etc.)
3. **Set Up MySQL Database**
   - Open **MySQL Workbench**.
   - Go to the **Administration** tab > **Data Import/Restore**.
   - Select **Import from Self-Contained File** and choose the `clinicdb.sql` file from this project.
   - Set the **Default Target Schema** to `ClinicDB`
   - Click **Start Import**.
   - Refresh your Schemas to verify that all tables (Patients, Supplies, Users, etc.) have been created.
4. **Configure Database Connection**
   - Open `src/EventDriven/Database.java`.
   - Update the database connection details:
     ```java
     conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ClinicDB", "root", "");
                                         // Database URL                    //Username //Password
     ```
5. **Compile and Run the Application**
   - Open the project in your IDE.
   - Run the `LoginForm` class to start.
## Usage
1. Default Admin Credentials
   - **Username:** admin
   - **Password:** admin123
2. Managing Patients
   - Navigate to the Patient Page
   - Click Register to add a new patient record
   - You can view the Patient History to track previous visits and treatments
3. Inventory & Supplies
   - Go to Supplies Page
   - Add, update, delete, and search the stock of clinic supplies
   - The system will highlight color red if an item is low on stock (below 50)
   - Print button to generate a reports of list of supplies
4. Billing and Receipts
   - Select a patient from the Queue
   - Input the services rendered and supplies based on the diagnosis of the patient
   - Auto generated receipt to view and print a billing statement via JasperReports
5. Database Interaction
   - Any changes made in the GUI (Adding/Updating/Deleting) are reflected in real-time in MySQL Workbench.
   - You can verify transactions by checking the receipts table.
## License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
