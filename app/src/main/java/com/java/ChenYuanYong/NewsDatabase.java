package com.java.ChenYuanYong;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(TagsConverter.class)
class News{
    @NonNull
    @PrimaryKey
    private String id;

    private String title;
    private String body;
    private List<String> author = new ArrayList<>();
    private String type;
    private String source;
    private String time;
    private boolean hasRead;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<String> getAuthor() { return author; }
    public void setAuthor(List<String> author) { this.author = author; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public boolean isHasRead() { return hasRead; }
    public void setHasRead(boolean hasRead) { this.hasRead = hasRead; }
}

class TagsConverter {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

@Dao
interface NewsDao{
    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news WHERE id = :id")
    News findById(String id);

    @Query("SELECT * FROM news WHERE hasRead = 1")
    List<News> getRead();

    @Query("SELECT * FROM news WHERE title LIKE '%'||:s||'%'")
    List<News> search(String s);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(News news);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(News... news);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(List<News> news);

    @Update()
    int update(News news);

    @Update
    int update(News... news);

    @Delete
    int delete(News news);

    @Delete
    int deleteAll(List<News> news);

    @Delete
    int deleteAll(News... news);

}

@Database(entities = {News.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
