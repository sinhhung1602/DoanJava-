package javaswingg.newpackage.Entity;
public class Customer {
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private String identityCard;

    public Customer(String customerId, String customerName, String phoneNumber, String identityCard) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.identityCard = identityCard;
    }
    public String getCustomerId() { 
        return customerId; 
    }

    public String getCustomerName() { 
        return customerName; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }

    public String getIdentityCard() { 
        return identityCard; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }

    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public void setIdentityCard(String identityCard) { 
        this.identityCard = identityCard; 
    }

    public void addCustomer() {
       
    }

    public void updateCustomer() {
        
    }

    public void deleteCustomer() {
        
    }

    public Customer searchCustomer(String id) {
        return null;    
    }   
}