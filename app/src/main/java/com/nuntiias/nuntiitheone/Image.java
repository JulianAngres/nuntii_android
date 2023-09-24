package com.nuntiias.nuntiitheone;

import java.util.List;

public class Image {

    private String url;
    private String webUrl;
    private String author;
    private String title;
    private String description;
    private String license;
    private List<String> htmlAttributions;

    public Image(String url, String webUrl, String author, String title, String description, String license, List<String> htmlAttributions) {
        this.url = url;
        this.webUrl = webUrl;
        this.author = author;
        this.title = title;
        this.description = description;
        this.license = license;
        this.htmlAttributions = htmlAttributions;
    }

    public String getUrl() {
        return url;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLicense() {
        return license;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }
}
