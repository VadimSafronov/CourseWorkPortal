package com.safronov.courseworksapp.Models;

public class CourseWork {

    public int id;
    public String name;
    public String description;
    public String file;
    public String file_name;

    public CourseWork(int id, String name, String description, String file_name) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.file_name = file_name;
    }
}
