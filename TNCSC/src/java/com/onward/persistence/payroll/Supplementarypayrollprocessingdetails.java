package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Supplementarypayrollprocessingdetails generated by hbm2java
 */
public class Supplementarypayrollprocessingdetails  implements java.io.Serializable {


     private String id;
     private Supplementatypaybill supplementatypaybill;
     private Integer nooddayscalculated;
     private Integer calculatedyear;
     private Integer calculatedmonth;
     private String accregion;
     private Boolean cancelled;
     private Date createddate;
     private String createdby;
     private String type;
     private String typeid;
     private Set supplementaryemployeedeductionstransactionses = new HashSet(0);
     private Set supplementaryemployeeloansandadvancesdetailses = new HashSet(0);
     private Set supplementaryemployeeearningstransactionses = new HashSet(0);

    public Supplementarypayrollprocessingdetails() {
    }

	
    public Supplementarypayrollprocessingdetails(String id) {
        this.id = id;
    }
    public Supplementarypayrollprocessingdetails(String id, Supplementatypaybill supplementatypaybill, Integer nooddayscalculated, Integer calculatedyear, Integer calculatedmonth, String accregion, Boolean cancelled, Date createddate, String createdby, String type, String typeid, Set supplementaryemployeedeductionstransactionses, Set supplementaryemployeeloansandadvancesdetailses, Set supplementaryemployeeearningstransactionses) {
       this.id = id;
       this.supplementatypaybill = supplementatypaybill;
       this.nooddayscalculated = nooddayscalculated;
       this.calculatedyear = calculatedyear;
       this.calculatedmonth = calculatedmonth;
       this.accregion = accregion;
       this.cancelled = cancelled;
       this.createddate = createddate;
       this.createdby = createdby;
       this.type = type;
       this.typeid = typeid;
       this.supplementaryemployeedeductionstransactionses = supplementaryemployeedeductionstransactionses;
       this.supplementaryemployeeloansandadvancesdetailses = supplementaryemployeeloansandadvancesdetailses;
       this.supplementaryemployeeearningstransactionses = supplementaryemployeeearningstransactionses;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Supplementatypaybill getSupplementatypaybill() {
        return this.supplementatypaybill;
    }
    
    public void setSupplementatypaybill(Supplementatypaybill supplementatypaybill) {
        this.supplementatypaybill = supplementatypaybill;
    }
    public Integer getNooddayscalculated() {
        return this.nooddayscalculated;
    }
    
    public void setNooddayscalculated(Integer nooddayscalculated) {
        this.nooddayscalculated = nooddayscalculated;
    }
    public Integer getCalculatedyear() {
        return this.calculatedyear;
    }
    
    public void setCalculatedyear(Integer calculatedyear) {
        this.calculatedyear = calculatedyear;
    }
    public Integer getCalculatedmonth() {
        return this.calculatedmonth;
    }
    
    public void setCalculatedmonth(Integer calculatedmonth) {
        this.calculatedmonth = calculatedmonth;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Date getCreateddate() {
        return this.createddate;
    }
    
    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }
    public String getCreatedby() {
        return this.createdby;
    }
    
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeid() {
        return this.typeid;
    }
    
    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
    public Set getSupplementaryemployeedeductionstransactionses() {
        return this.supplementaryemployeedeductionstransactionses;
    }
    
    public void setSupplementaryemployeedeductionstransactionses(Set supplementaryemployeedeductionstransactionses) {
        this.supplementaryemployeedeductionstransactionses = supplementaryemployeedeductionstransactionses;
    }
    public Set getSupplementaryemployeeloansandadvancesdetailses() {
        return this.supplementaryemployeeloansandadvancesdetailses;
    }
    
    public void setSupplementaryemployeeloansandadvancesdetailses(Set supplementaryemployeeloansandadvancesdetailses) {
        this.supplementaryemployeeloansandadvancesdetailses = supplementaryemployeeloansandadvancesdetailses;
    }
    public Set getSupplementaryemployeeearningstransactionses() {
        return this.supplementaryemployeeearningstransactionses;
    }
    
    public void setSupplementaryemployeeearningstransactionses(Set supplementaryemployeeearningstransactionses) {
        this.supplementaryemployeeearningstransactionses = supplementaryemployeeearningstransactionses;
    }




}


