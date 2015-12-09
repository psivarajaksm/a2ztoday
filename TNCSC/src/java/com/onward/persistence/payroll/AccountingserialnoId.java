package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA



/**
 * AccountingserialnoId generated by hbm2java
 */
public class AccountingserialnoId  implements java.io.Serializable {


     private String id;
     private String accountingyearid;

    public AccountingserialnoId() {
    }

    public AccountingserialnoId(String id, String accountingyearid) {
       this.id = id;
       this.accountingyearid = accountingyearid;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getAccountingyearid() {
        return this.accountingyearid;
    }
    
    public void setAccountingyearid(String accountingyearid) {
        this.accountingyearid = accountingyearid;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AccountingserialnoId) ) return false;
		 AccountingserialnoId castOther = ( AccountingserialnoId ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getAccountingyearid()==castOther.getAccountingyearid()) || ( this.getAccountingyearid()!=null && castOther.getAccountingyearid()!=null && this.getAccountingyearid().equals(castOther.getAccountingyearid()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getAccountingyearid() == null ? 0 : this.getAccountingyearid().hashCode() );
         return result;
   }   


}


