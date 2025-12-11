package javaswingg.newpackage.Entity;
public class Facility {
    private String facilityId;
    private String facilityName;
    private String category;
    private int quantity;
    private String condition;
    
    public Facility(String facilityId, String facilityName, String category, int quantity, String condition) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.category = category;
        this.quantity = quantity;
        this.condition = condition;
    }

    public String getId() { 
        return facilityId; 
    }
    public String getFacilityId() { 
        return facilityId; 
    }
    public String getName() { 
        return facilityName; 
    }
    public String getFacilityName() { 
        return facilityName; 
    }
    public String getCategory() { 
        return category; 
    }
    public int getQuantity() { 
        return quantity; 
    }
    public String getCondition() { 
        return condition; 
    }

    public void setFacilityId(String facilityId) { 
        this.facilityId = facilityId; 
    }
    public void setName(String name) { 
        this.facilityName = name; 
    }
    public void setFacilityName(String facilityName) { 
        this.facilityName = facilityName; 
    }
    public void setCategory(String category) { 
        this.category = category; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
    }
    public void setCondition(String condition) { 
        this.condition = condition; 
    }    
    public void addFacility() {       
    }
    
    public void updateFacility() {       
    }
    
    public void deleteFacility() {
    }    
    public Facility searchFacility(String id) {
        return null;
    }
}