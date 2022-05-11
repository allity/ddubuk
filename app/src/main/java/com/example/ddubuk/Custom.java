package com.example.ddubuk;

public class Custom {
    private String Customname;
    private String Customregion;
    private String hashtag;

    public Custom(){}

    public String getName() {
        return Customname;
    }

    public void setName(String name) {
        this.Customname = name;
    }

    public String getRegion() {
        return Customregion;
    }

    public void setRegion(String region) {
        this.Customregion = region;
    }

    public String getHashtag() { return hashtag; }

    public void setHashtag(String HashTag) { this.hashtag = HashTag; }
}
