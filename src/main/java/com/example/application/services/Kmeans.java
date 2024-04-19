package com.example.application.services;

import com.example.application.data.Movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Kmeans {
    List<Movie> movies = new ArrayList<>();
    public List<Movie> getMovies() {
        return movies;
    }
    public void readMoviesFromFile(String filename, double percentage) {
        if(percentage > 100 || percentage < 0)
            percentage = 0;
        movies.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            // Calculate the number of records to read based on the specified percentage
            int numRecords = (int) Math.ceil(lines.size() * percentage / 100.0);
            Random rand = new Random();
            Set<Integer> indices = new HashSet<>();
            while (indices.size() < numRecords) {
                indices.add(rand.nextInt(lines.size()));
//                System.out.println("indices size: "+indices.size());
            }
//            System.out.println("indices size: "+indices.size());

            for (int index : indices) {
                // Split the line by commas, and each part is a field in the record in a ""
                String[] parts = lines.get(index).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String Movie_Name = parts[0].trim();
                String Release_Year = parts[1].trim();
                String Duration = parts[2].trim();
                double IMDB_Rating = Double.parseDouble(parts[3].trim());
                String Metascore = parts[4].trim();
                String Votes = parts[5].trim();
                String Genre = parts[6].trim();
                String Director = parts[7].trim();
                String Cast = parts[8].trim();
                String Gross = parts[9].trim();

                // Create a Movie object or process the data as needed
                Movie movie = new Movie(Movie_Name, Release_Year, Duration, IMDB_Rating,
                        Metascore, Votes, Genre, Director, Cast, Gross);
                movies.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Map<Double,List<Movie>>,List<Movie>> K_means_algorithm(int k, int maxIterations, double outlier_threshold) {

        // Initialize centroids randomly
        List<Double> centroids = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            centroids.add(movies.get(random.nextInt(movies.size())).getIMDB_Rating());
        }

        // K-means algorithm implementation
        // Assign clusters based on the closest centroid
        Map<Double, List<Movie>> clusters = new HashMap<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            double minDistance = Double.MAX_VALUE;
            double centroid = 0;
            for (Double c : centroids) {
                double distance = Math.abs(movie.getIMDB_Rating() - c);
                if (distance < minDistance) {
                    minDistance = distance;
                    centroid = c;
                }
            }
            if (!clusters.containsKey(centroid)) {
                clusters.put(centroid, new ArrayList<>());
            }
            clusters.get(centroid).add(movie);
        }

        // Update centroids and reassign clusters
        for (int i = 0; i < maxIterations; i++) {
            List<Double> newCentroids = new ArrayList<>();
            for (Map.Entry<Double, List<Movie>> entry : clusters.entrySet()) {
                double sum = 0;
                for (Movie movie : entry.getValue()) {
                    sum += movie.getIMDB_Rating();
                }
                double newCentroid = sum / entry.getValue().size();
                newCentroids.add(newCentroid);
            }
            clusters.clear();
            for (int j = 0; j < movies.size(); j++) {
                Movie movie = movies.get(j);
                double minDistance = Double.MAX_VALUE;
                double centroid = 0;
                for (Double c : newCentroids) {
                    double distance = Math.abs(movie.getIMDB_Rating() - c);
                    if (distance < minDistance) {
                        minDistance = distance;
                        centroid = c;
                    }
                }
                if (!clusters.containsKey(centroid)) {
                    clusters.put(centroid, new ArrayList<>());
                }
                clusters.get(centroid).add(movie);
            }
            if (newCentroids.equals(centroids)) {
                break;
            }
            centroids = newCentroids;
        }

        // Detect outliers based on distance from centroids
        List<Movie> outliers = new ArrayList<>();
        for (Map.Entry<Double, List<Movie>> entry : clusters.entrySet()) {
            double centroid = entry.getKey();
            List<Movie> clusterMovies = entry.getValue();
            for (Movie movie : clusterMovies) {
                double distance = Math.abs(movie.getIMDB_Rating() - centroid);
                if (distance > outlier_threshold) { // Set a threshold for outliers
                    outliers.add(movie);
                }
            }
        }
        // Remove the outliers from their assigned clusters
        for (Movie outlier : outliers) {
            for (Map.Entry<Double, List<Movie>> entry : clusters.entrySet()) {
                if (entry.getValue().contains(outlier)) {
                    entry.getValue().remove(outlier);
                    break; // Move to the next outlier
                }
            }
        }

        // Output clusters
//        System.out.println("Clusters:");
//        System.out.println("centroids: ");
//        for (Map.Entry<Double, List<Movie>> entry : clusters.entrySet()) {
//            System.out.print(" " + entry.getKey());
//        }
//        System.out.println();
//        for (Map.Entry<Double, List<Movie>> entry : clusters.entrySet()) {
//            System.out.println("Cluster with centroid: " + entry.getKey());
//            for (Movie movie : entry.getValue()) {
//                System.out.print(" - " + movie.getIMDB_Rating());
//            }
//            System.out.println();
//        }

        // Output outliers
//        System.out.println("Outliers:");
//        for (Movie movie : outliers) {
//            System.out.println(movie.getMovie_Name() + " - " + movie.getIMDB_Rating());
//        }

        return Map.of(clusters, outliers);
    }

    // main
//    public static void main(String[] args) {
//        Kmeans kmeans = new Kmeans();
//        kmeans.readMoviesFromFile("movies.csv", 100);
//        kmeans.K_means_algorithm(5, 1000, 1.0);
//    }

}
