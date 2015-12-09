/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tncscpayroll.transferobjects;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */

@XmlRootElement(namespace = "masterDetails")
public class masterDetails {
    private List<PaycodeMasterValueObjectModel> paycodeMasterPostList;
    private List<RegionMasterValueObjectModel> regionMasterPostList;
    private List<SectionMasterValueObjectModel> sectionMasterPostList;
    private List<DesignationMasterValueObjectModel> designationMasterPostList;
    private List<CcahraMasterValueObjectModel> ccahraMasterPostList;
    private List<EarningsSlapDetailsValueObjectModel> earningsSlapDetailsPostList;

    /**
     * @return the paycodeMasterPostList
     */
    public List<PaycodeMasterValueObjectModel> getPaycodeMasterPostList() {
        return paycodeMasterPostList;
    }

    /**
     * @param paycodeMasterPostList the paycodeMasterPostList to set
     */
    public void setPaycodeMasterPostList(List<PaycodeMasterValueObjectModel> paycodeMasterPostList) {
        this.paycodeMasterPostList = paycodeMasterPostList;
    }

    /**
     * @return the regionMasterPostList
     */
    public List<RegionMasterValueObjectModel> getRegionMasterPostList() {
        return regionMasterPostList;
    }

    /**
     * @param regionMasterPostList the regionMasterPostList to set
     */
    public void setRegionMasterPostList(List<RegionMasterValueObjectModel> regionMasterPostList) {
        this.regionMasterPostList = regionMasterPostList;
    }

    /**
     * @return the sectionMasterPostList
     */
    public List<SectionMasterValueObjectModel> getSectionMasterPostList() {
        return sectionMasterPostList;
    }

    /**
     * @param sectionMasterPostList the sectionMasterPostList to set
     */
    public void setSectionMasterPostList(List<SectionMasterValueObjectModel> sectionMasterPostList) {
        this.sectionMasterPostList = sectionMasterPostList;
    }

    /**
     * @return the ccahraMasterPostList
     */
    public List<CcahraMasterValueObjectModel> getCcahraMasterPostList() {
        return ccahraMasterPostList;
    }

    /**
     * @param ccahraMasterPostList the ccahraMasterPostList to set
     */
    public void setCcahraMasterPostList(List<CcahraMasterValueObjectModel> ccahraMasterPostList) {
        this.ccahraMasterPostList = ccahraMasterPostList;
    }

    /**
     * @return the earningsSlapDetailsPostList
     */
    public List<EarningsSlapDetailsValueObjectModel> getEarningsSlapDetailsPostList() {
        return earningsSlapDetailsPostList;
    }

    /**
     * @param earningsSlapDetailsPostList the earningsSlapDetailsPostList to set
     */
    public void setEarningsSlapDetailsPostList(List<EarningsSlapDetailsValueObjectModel> earningsSlapDetailsPostList) {
        this.earningsSlapDetailsPostList = earningsSlapDetailsPostList;
    }

    /**
     * @return the dsignationMasterPostList
     */
    public List<DesignationMasterValueObjectModel> getDesignationMasterPostList() {
        return designationMasterPostList;
    }

    /**
     * @param dsignationMasterPostList the dsignationMasterPostList to set
     */
    public void setDesignationMasterPostList(List<DesignationMasterValueObjectModel> designationMasterPostList) {
        this.designationMasterPostList = designationMasterPostList;
    }


}
