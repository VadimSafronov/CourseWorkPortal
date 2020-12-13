package com.safronov.courseworksapp.Models;

public class FavoriteWork {

    public int id;
    public int username_Id;
    public int courseWork_Id;

    public FavoriteWork(int username_Id, int courseWork_Id) {
        this.username_Id = username_Id;
        this.courseWork_Id = courseWork_Id;
    }
}
