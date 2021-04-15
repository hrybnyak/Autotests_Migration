package testDB_1;

import java.io.*;
import java.sql.*;
 
public class SimpleTblExporter {
 
    public static void main(String[] args) {         
        String csvFilePath = "Export.csv";
        
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr","hr")) {
            String sql = "select emp.EMPLOYEE_ID, emp.FIRST_NAME, emp.LAST_NAME, emp.SALARY, emp.COMMISSION_PCT from employees emp";
             
            Statement statement = connection.createStatement();
             
            ResultSet result = statement.executeQuery(sql);
             
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));
             
            // write header line containing column names       
            fileWriter.write("EMPLOYEE_ID,FIRST_NAME,LAST_NAME,SALARY,COMMISSION_PCT");
             
            while (result.next()) {
                int employeeId = result.getInt("EMPLOYEE_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                float salary = result.getFloat("SALARY")/100;
                String commissionPCT = result.getString("COMMISSION_PCT");
                 
                if (commissionPCT == null) {
                	commissionPCT = "";   // write empty value for null
                } else {
                	commissionPCT = "\"" + commissionPCT + "\""; // escape double quotes
                }
                 
                String line = String.format("%d,%s,%s,%.2f,%s",
                		employeeId, firstName, lastName, salary, commissionPCT);
                 
                fileWriter.newLine();
                fileWriter.write(line);            
            }
             
            statement.close();
            fileWriter.close();
             
        } catch (SQLException e) {
            System.out.println("Datababse error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }       
    } 
    
}

