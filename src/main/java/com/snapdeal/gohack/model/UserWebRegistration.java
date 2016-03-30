/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 27-Feb-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(Include.NON_NULL)
public class UserWebRegistration {
    

    
    
    @NotEmpty(message="registrationId cannot be empty")
    @NotNull(message="registrationId cannot be null")
    @JsonProperty("registrationId")
    private String registrationId;
    
    @NotEmpty(message="browserType cannot be empty")
    @NotNull(message="browserType cannot be null")
    @JsonProperty("browserType")
    private String browserType;
    
    
    
    @NotEmpty(message="browserVersion cannot be empty")
    @NotNull(message="browserVersion cannot be null")
    @JsonProperty("browserVersion")
    private String browserVersion;
    
    
    @NotEmpty(message="userAgentInfo cannot be empty")
    @NotNull(message="userAgentInfo cannot be null")
    @JsonProperty("userAgentInfo")
    private String userAgentInfo;
 


    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

   
    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getUserAgentInfo() {
        return userAgentInfo;
    }

    public void setUserAgentInfo(String userAgentInfo) {
        this.userAgentInfo = userAgentInfo;
    }

   
    

}
