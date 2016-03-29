package cn.careerforce.sj.model;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-11-5
 * Time: 上午8:53
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private String userId;
    private String nickName;
    private String city;
    private String industry;
    private String career;
    private int age;
    private String consigneeName;
    private String consigneeTel;
    private String consigneeAddress;
    private String headImg;

    public User() {
    }

    public User(String userId, String nickName, String city, String industry, String career, int age, String consigneeName, String consigneeTel, String consigneeAddress, String headImg) {
        this.userId = userId;
        this.nickName = nickName;
        this.city = city;
        this.industry = industry;
        this.career = career;
        this.age = age;
        this.consigneeName = consigneeName;
        this.consigneeTel = consigneeTel;
        this.consigneeAddress = consigneeAddress;
        this.headImg = headImg;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIndustry() {
        return industry == null ? "" : industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCareer() {
        return career == null ? "" : career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConsigneeName() {
        return consigneeName == null ? "" : consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeTel() {
        return consigneeTel == null ? "" : consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddress() {
        return consigneeAddress == null ? "" : consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getHeadImg() {
        return headImg == null ? "" : headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
