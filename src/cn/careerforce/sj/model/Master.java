package cn.careerforce.sj.model;

/**
 * 申请大师模型
 */
public class Master {
    private int id;
    private String userId = "0";
    private String name = "";
    private String sex = "M";
    private String headImg = "";
    private String idCardPositive = "";
    private String idCardOpposite = "";
    private String career = "";
    private String region = "";
    private String telephone = "";
    private String wxNo = "";
    private String workTime = "";
    private String personalBrand = "";
    private String shopAddress = "";
    private String cooperationPlatform = "";
    private String recommendedPerson = "";
    private String recommendation = "";
    private String description = "";
    private String relatedImages = ""; //相关作品、工作室图片，逗号隔开

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIdCardPositive() {
        return idCardPositive;
    }

    public void setIdCardPositive(String idCardPositive) {
        this.idCardPositive = idCardPositive;
    }

    public String getIdCardOpposite() {
        return idCardOpposite;
    }

    public void setIdCardOpposite(String idCardOpposite) {
        this.idCardOpposite = idCardOpposite;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWxNo() {
        return wxNo;
    }

    public void setWxNo(String wxNo) {
        this.wxNo = wxNo;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getPersonalBrand() {
        return personalBrand;
    }

    public void setPersonalBrand(String personalBrand) {
        this.personalBrand = personalBrand;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getCooperationPlatform() {
        return cooperationPlatform;
    }

    public void setCooperationPlatform(String cooperationPlatform) {
        this.cooperationPlatform = cooperationPlatform;
    }

    public String getRecommendedPerson() {
        return recommendedPerson;
    }

    public void setRecommendedPerson(String recommendedPerson) {
        this.recommendedPerson = recommendedPerson;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelatedImages() {
        return relatedImages;
    }

    public void setRelatedImages(String relatedImages) {
        this.relatedImages = relatedImages;
    }
}
