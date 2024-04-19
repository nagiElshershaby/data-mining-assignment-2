package com.example.application.data;

public class Movie {
    public Movie(String movie_Name, String release_Year, String duration, double IMDB_Rating, String metascore, String votes, String genre, String director, String cast, String gross) {
        Movie_Name = movie_Name;
        Release_Year = release_Year;
        Duration = duration;
        this.IMDB_Rating = IMDB_Rating;
        Metascore = metascore;
        Votes = votes;
        Genre = genre;
        Director = director;
        Cast = cast;
        Gross = gross;
    }

    String Movie_Name;
    String Release_Year;
    String Duration;
    double IMDB_Rating;
    String Metascore;
    String Votes;
    String Genre;
    String Director;
    String Cast;
    String Gross;

    public String getMovie_Name() {
        return Movie_Name;
    }

    public void setMovie_Name(String movie_Name) {
        Movie_Name = movie_Name;
    }

    public String getRelease_Year() {
        return Release_Year;
    }

    public void setRelease_Year(String release_Year) {
        Release_Year = release_Year;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public double getIMDB_Rating() {
        return IMDB_Rating;
    }

    public void setIMDB_Rating(double IMDB_Rating) {
        this.IMDB_Rating = IMDB_Rating;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    public String getVotes() {
        return Votes;
    }

    public void setVotes(String votes) {
        Votes = votes;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getCast() {
        return Cast;
    }

    public void setCast(String cast) {
        Cast = cast;
    }

    public String getGross() {
        return Gross;
    }

    public void setGross(String gross) {
        Gross = gross;
    }
}
