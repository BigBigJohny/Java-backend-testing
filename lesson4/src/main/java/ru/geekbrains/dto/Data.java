package ru.geekbrains.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Data {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private Object title;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("datetime")
    private Integer datetime;
    @JsonProperty("mp4")
    private Integer mp4;
    @JsonProperty("hls")
    private Integer hls;
    @JsonProperty("type")
    private String type;
    @JsonProperty("animated")
    private Boolean animated;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("views")
    private Integer views;
    @JsonProperty("bandwidth")
    public Integer bandwidth;
    @JsonProperty("vote")
    public Object vote;
    @JsonProperty("favorite")
    public Boolean favorite;
    @JsonProperty("nsfw")
    public Object nsfw;
    @JsonProperty("section")
    public Object section;
    @JsonProperty("account_url")
    public Object accountUrl;
    @JsonProperty("account_id")
    public Integer accountId;
    @JsonProperty("is_ad")
    public Boolean isAd;
    @JsonProperty("in_most_viral")
    public Boolean inMostViral;
    @JsonProperty("has_sound")
    public Boolean hasSound;
    @JsonProperty("tags")
    public List<Object> tags = new ArrayList<Object>();
    @JsonProperty("ad_type")
    public Integer adType;
    @JsonProperty("ad_url")
    public String adUrl;
    @JsonProperty("edited")
    public String edited;
    @JsonProperty("in_gallery")
    public Boolean inGallery;
    @JsonProperty("deletehash")
    public String deletehash;
    @JsonProperty("name")
    public String name;
    @JsonProperty("link")
    public String link;
}
