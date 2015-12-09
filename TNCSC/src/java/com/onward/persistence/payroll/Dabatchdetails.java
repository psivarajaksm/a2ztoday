package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Dabatchdetails generated by hbm2java
 */
public class Dabatchdetails  implements java.io.Serializable {


     private String id;
     private Daarrear daarrear;
     private Regionmaster regionmaster;
     private String daname;
     private Boolean active;
     private Boolean cancelled;
     private Set supplementatypaybills = new HashSet(0);

    public Dabatchdetails() {
    }

	
    public Dabatchdetails(String id) {
        this.id = id;
    }
    public Dabatchdetails(String id, Daarrear daarrear, Regionmaster regionmaster, String daname, Boolean active, Boolean cancelled, Set supplementatypaybills) {
       this.id = id;
       this.daarrear = daarrear;
       this.regionmaster = regionmaster;
       this.daname = daname;
       this.active = active;
       this.cancelled = cancelled;
       this.supplementatypaybills = supplementatypaybills;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Daarrear getDaarrear() {
        return this.daarrear;
    }
    
    public void setDaarrear(Daarrear daarrear) {
        this.daarrear = daarrear;
    }
    public Regionmaster getRegionmaster() {
        return this.regionmaster;
    }
    
    public void setRegionmaster(Regionmaster regionmaster) {
        this.regionmaster = regionmaster;
    }
    public String getDaname() {
        return this.daname;
    }
    
    public void setDaname(String daname) {
        this.daname = daname;
    }
    public Boolean getActive() {
        return this.active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Set getSupplementatypaybills() {
        return this.supplementatypaybills;
    }
    
    public void setSupplementatypaybills(Set supplementatypaybills) {
        this.supplementatypaybills = supplementatypaybills;
    }




}

