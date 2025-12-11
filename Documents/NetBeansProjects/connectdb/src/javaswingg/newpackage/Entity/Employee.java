package javaswingg.newpackage.Entity;
public class Employee {
    private String employeeId;
    private String employeeName;
    private String position;
    private String department;
    private String phoneNumber;
    
    public Employee(String employeeId, String employeeName, String position, String department, String phoneNumber) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.department = department;
        this.phoneNumber = phoneNumber;
    }

    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setPosition(String position) { this.position = position; }
    public void setDepartment(String department) { this.department = department; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public void addEmployee() {
    }
    
    public void updateEmployee() {
    }
    
    public void deleteEmployee() {
    }
    
    public Employee searchEmployee(String id) {
        return null;
    }
}