/*
*CampaignRecommendEx.java
*Created on 2015/10/4 上午12:12 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.cloud.xtilus.makingfriends.bean;

import java.io.Serializable;

/**
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class HomeCampaign implements Serializable {


    private Long id;
    private String title;
    private int campaignOne;
    private int campaignTwo;
    private int campaignThree;
    private Campaign cpOne;
    private Campaign cpTwo;
    private Campaign cpThree;


    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }

    public int getCampaignOne() {
        return campaignOne;
    }

    public void setCampaignOne(int campaignOne) {
        this.campaignOne = campaignOne;
    }

    public int getCampaignTwo() {
        return campaignTwo;
    }

    public void setCampaignTwo(int campaignTwo) {
        this.campaignTwo = campaignTwo;
    }

    public int getCampaignThree() {
        return campaignThree;
    }

    public void setCampaignThree(int campaignThree) {
        this.campaignThree = campaignThree;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
