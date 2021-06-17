package com.cloud.xtilus.makingfriends.bean;

/**
 * 2018/4/13.
 */

public class LotteryTicket extends BaseBean{
    private  String name;
    private  int currentCloseState;
    private String currentCase;
    private String currentCaseOpenTime;
    private String currentCaseCloseTime;
    private String url;
    private int order;
    private String addUser;
    private String addTime;
    private String lastModifyUser;
    private  String lastModifyTime;
    private int enable;
    private String  memo;
    private int  type;
    private String openDateTime;
    private String imgUrl;
    private String memo1;
    private String  memo2;
    private  int hot;
    private  int frequency;
    private  long remainingTime;
    private String outCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentCloseState() {
        return currentCloseState;
    }

    public void setCurrentCloseState(int currentCloseState) {
        this.currentCloseState = currentCloseState;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(String currentCase) {
        this.currentCase = currentCase;
    }

    public String getCurrentCaseOpenTime() {
        return currentCaseOpenTime;
    }

    public void setCurrentCaseOpenTime(String currentCaseOpenTime) {
        this.currentCaseOpenTime = currentCaseOpenTime;
    }

    public String getCurrentCaseCloseTime() {
        return currentCaseCloseTime;
    }

    public void setCurrentCaseCloseTime(String currentCaseCloseTime) {
        this.currentCaseCloseTime = currentCaseCloseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOpenDateTime() {
        return openDateTime;
    }

    public void setOpenDateTime(String openDateTime) {
        this.openDateTime = openDateTime;
    }

    public String getMemo1() {
        return memo1;
    }

    public void setMemo1(String memo1) {
        this.memo1 = memo1;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }
}
