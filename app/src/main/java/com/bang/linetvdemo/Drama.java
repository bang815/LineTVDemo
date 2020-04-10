package com.bang.linetvdemo;

public class Drama {
    private String drama_id;
    private String name;
    private String total_views;
    private String created_at;
    private String thumb;
    private String rating;

    public Drama(String drama_id, String name, String total_views, String created_at, String thumb, String rating) {
        this.drama_id = drama_id;
        this.name = name;
        this.total_views = total_views;
        this.created_at = created_at;
        this.thumb = thumb;
        this.rating=rating;
    }

    public String getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(String drama_id) {
        this.drama_id = drama_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_views() {
        return total_views;
    }

    public void setTotal_views(String total_views) {
        this.total_views = total_views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
