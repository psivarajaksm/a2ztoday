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
@XmlRootElement(namespace = "SectionMasterValueObjectModel")
public class SectionMasterValueObjectModel {
    private String id;
    private String sectionname;

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
     * @return the sectionname
     */
    public String getSectionname() {
        return sectionname;
    }

    /**
     * @param sectionname the sectionname to set
     */
    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

}
