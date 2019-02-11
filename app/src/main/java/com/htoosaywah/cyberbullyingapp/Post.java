package com.htoosaywah.cyberbullyingapp;

public class Post  {

    public String loginId;
    public String username;
    public String userPic;
    public String post;
    public Boolean isBully;
    public String bullyWords;
    public String postedTime;
    public int timestamp;
    public Long postDate;


    public Post() {
        this.loginId = "";
        this.username = "";
        this.userPic = "";
        this.post = "";
        this.isBully = false;
        this.bullyWords = "";
        this.postedTime = "";
        this.timestamp = 0;
        this.postDate = 0L;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Boolean getBully() {
        return isBully;
    }

    public void setBully(Boolean bully) {
        isBully = bully;
    }

    public String getBullyWords() {
        return bullyWords;
    }

    public void setBullyWords(String bullyWords) {
        this.bullyWords = bullyWords;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPostDate() {
        return postDate;
    }

    public void setPostDate(Long postDate) {
        this.postDate = postDate;
    }
}
