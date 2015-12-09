/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tncscpayroll.transferobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(namespace = "RegionMasterValueObjectModel")
public class RegionMasterValueObjectModel {
    private String id;
    private String regionname;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the regionname
     */
    public String getRegionname() {
        return regionname;
    }

    /**
     * @param regionname the regionname to set
     */
    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

}
