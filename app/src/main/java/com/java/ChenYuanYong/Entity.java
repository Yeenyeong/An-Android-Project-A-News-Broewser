package com.example.coronanews;

import org.json.JSONArray;
import org.json.JSONObject;

public class Entity {
    private String url;
    private String label;
    private String enwiki;
    private String baidu;
    private String zhwiki;
    private String img_url;

    // COVID JSONObject
    private JSONObject properties;
    private JSONArray relations;

    public Entity() {}

    public void setUrl(final String url) { this.url = url; }
    public String getUrl() { return url; }
    public void setLabel(final String label) { this.label = label; }
    public String getLabel() { return label; }
    public void setImgUrl(final String img_url) { this.img_url = img_url; }
    public String getImgUrl() { return img_url; }

    // set COVID JSONObject
    public void setProperties(final JSONObject properties) { this.properties = properties; }
    public JSONObject getProperties() { return properties; }
    public void setRelations(final JSONArray relations) { this.relations = relations; }
    public JSONArray getRelations() { return relations; }

    // set description
    public void setEnwiki(String s) { enwiki = s; }
    public String getEnwiki() { return enwiki; }
    public void setBaidu(String s) { baidu = s; }
    public String getBaidu() { return baidu; }
    public void setZhwiki(String s) { zhwiki = s; }
    public String getZhwiki() { return zhwiki; }
}
