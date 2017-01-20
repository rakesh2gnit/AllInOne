package com.mycollection.rakesh.mycollection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gleecus on 12/5/16.
 */

public class Country {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dial_code")
    @Expose
    private String dialCode;
    @SerializedName("code")
    @Expose
    private String code;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The dialCode
     */
    public String getDialCode() {
        return dialCode;
    }

    /**
     *
     * @param dialCode
     * The dial_code
     */
    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEligibleForQuery(String query) {
        query = query.toLowerCase();
        return getName().toLowerCase().contains(query);
    }
}
