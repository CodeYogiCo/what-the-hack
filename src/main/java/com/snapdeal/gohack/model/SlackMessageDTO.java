/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 22-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

import com.google.gson.annotations.SerializedName;

public class SlackMessageDTO {

    private String            text;

    private String            username;
    
    @SerializedName("icon_emoji")
    private String icon;



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
