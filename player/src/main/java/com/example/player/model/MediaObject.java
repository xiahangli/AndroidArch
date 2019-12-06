package com.example.player.model;

/**
 * @User Xiahangli
 * @Date 2019-12-06  09:13
 * @Email henryatxia@gmail.com
 * @Descrip
 */
public class MediaObject {
    private String title;
    private String mediaUrl;
    private String thumbnail;
    private String description;


    public MediaObject(String title, String mediaUrl, String thumbnail, String description) {
        this.title = title;
        this.mediaUrl = mediaUrl;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
