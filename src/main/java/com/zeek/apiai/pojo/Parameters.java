
package com.zeek.apiai.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parameters {

    @SerializedName("aman-recipient-list")
    @Expose
    private String amanRecipientList;

    public String getAmanRecipientList() {
        return amanRecipientList;
    }

    public void setAmanRecipientList(String amanRecipientList) {
        this.amanRecipientList = amanRecipientList;
    }

}
