package com.chuangtu.idcard;

import java.util.Arrays;
import java.util.List;

public class IdcardPojo {

    private String config_str;

    private String address;

    private String num;

    private String sex;

    private String birth;

    private List faceRect;

    private Object[] cardRegion;

    private String nationality;

    private Boolean success;

    private String name;

    private Boolean isFake;

    private String requestId;

    private Object[] faceRectVertices;

    public IdcardPojo() {
    }

    public IdcardPojo(String config_str, String address, String num, String sex, String birth, List faceRect, Object[] cardRegion, String nationality, Boolean success, String name, Boolean isFake, String requestId, Object[] faceRectVertices) {
        this.config_str = config_str;
        this.address = address;
        this.num = num;
        this.sex = sex;
        this.birth = birth;
        this.faceRect = faceRect;
        this.cardRegion = cardRegion;
        this.nationality = nationality;
        this.success = success;
        this.name = name;
        this.isFake = isFake;
        this.requestId = requestId;
        this.faceRectVertices = faceRectVertices;
    }

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public List getFaceRect() {
        return faceRect;
    }

    public void setFaceRect(List faceRect) {
        this.faceRect = faceRect;
    }

    public Object[] getCardRegion() {
        return cardRegion;
    }

    public void setCardRegion(Object[] cardRegion) {
        this.cardRegion = cardRegion;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFake() {
        return isFake;
    }

    public void setFake(Boolean fake) {
        isFake = fake;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object[] getFaceRectVertices() {
        return faceRectVertices;
    }

    public void setFaceRectVertices(Object[] faceRectVertices) {
        this.faceRectVertices = faceRectVertices;
    }

    @Override
    public String toString() {
        return "IdcardPojo{" +
                "config_str='" + config_str + '\'' +
                ", address='" + address + '\'' +
                ", num='" + num + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", faceRect=" + faceRect +
                ", cardRegion=" + Arrays.toString(cardRegion) +
                ", nationality='" + nationality + '\'' +
                ", success=" + success +
                ", name='" + name + '\'' +
                ", isFake=" + isFake +
                ", requestId='" + requestId + '\'' +
                ", faceRectVertices=" + Arrays.toString(faceRectVertices) +
                '}';
    }
}
