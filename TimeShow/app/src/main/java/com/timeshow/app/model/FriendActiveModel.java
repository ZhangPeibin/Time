package com.timeshow.app.model;

import java.util.List;

/**
 * Created by peibin on 18-3-6.
 */

public class FriendActiveModel {
    public String active_title;
    public String active_profile;
    public String address ;
    public String cost;
    public String details_address;
    public String head;
    public int id;
    public String phone;
    public String post_time;
    public String profile;
    public String title;
    public String url;
    public List<String> likes;
    public List<Comment> comments;

    public class Comment{
        public String name;
        public String content;
    }
}
