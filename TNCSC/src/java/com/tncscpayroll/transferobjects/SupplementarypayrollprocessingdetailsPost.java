/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import com.onward.persistence.payroll.Supplementatypaybill;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "supplementarypayrollprocessingdetailspost")
public class SupplementarypayrollprocessingdetailsPost {

    private String id;
    private String supplementatypaybillId;
    private String nooddayscalculated;
    private String calculatedyear;
    private String calculatedmonth;
    private String accregion;
    private String cancelled;
    private List<SupplementaryemployeedeductionstransactionsPost> supplementaryemployeedeductionstransactionsPostList;
    private List<SupplementaryemployeeearningstransactionsPost> supplementaryemployeeearningstransactionsPostList;
    private List<SupplementaryemployeeloansandadvancesdetailsPost> supplementaryemployeeloansandadvancesdetailsPostList;

    public List<SupplementaryemployeedeductionstransactionsPost> getSupplementaryemployeedeductionstransactionsPostList() {
        return supplementaryemployeedeductionstransactionsPostList;
    }

    public void setSupplementaryemployeedeductionstransactionsPostList(List<SupplementaryemployeedeductionstransactionsPost> supplementaryemployeedeductionstransactionsPostList) {
        this.supplementaryemployeedeductionstransactionsPostList = supplementaryemployeedeductionstransactionsPostList;
    }

    public List<SupplementaryemployeeearningstransactionsPost> getSupplementaryemployeeearningstransactionsPostList() {
        return supplementaryemployeeearningstransactionsPostList;
    }

    public void setSupplementaryemployeeearningstransactionsPostList(List<SupplementaryemployeeearningstransactionsPost> supplementaryemployeeearningstransactionsPostList) {
        this.supplementaryemployeeearningstransactionsPostList = supplementaryemployeeearningstransactionsPostList;
    }

    public List<SupplementaryemployeeloansandadvancesdetailsPost> getSupplementaryemployeeloansandadvancesdetailsPostList() {
        return supplementaryemployeeloansandadvancesdetailsPostList;
    }

    public void setSupplementaryemployeeloansandadvancesdetailsPostList(List<SupplementaryemployeeloansandadvancesdetailsPost> supplementaryemployeeloansandadvancesdetailsPostList) {
        this.supplementaryemployeeloansandadvancesdetailsPostList = supplementaryemployeeloansandadvancesdetailsPostList;
    }

    public String getAccregion() {
        return accregion;
    }

    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

    public String getCalculatedmonth() {
        return calculatedmonth;
    }

    public void setCalculatedmonth(String calculatedmonth) {
        this.calculatedmonth = calculatedmonth;
    }

    public String getCalculatedyear() {
        return calculatedyear;
    }

    public void setCalculatedyear(String calculatedyear) {
        this.calculatedyear = calculatedyear;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNooddayscalculated() {
        return nooddayscalculated;
    }

    public void setNooddayscalculated(String nooddayscalculated) {
        this.nooddayscalculated = nooddayscalculated;
    }

    public String getSupplementatypaybillId() {
        return supplementatypaybillId;
    }

    public void setSupplementatypaybillId(String supplementatypaybillId) {
        this.supplementatypaybillId = supplementatypaybillId;
    }
}
