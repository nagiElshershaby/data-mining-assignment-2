package com.example.application.views.list;

import com.example.application.data.Movie;
import com.example.application.services.Kmeans;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.upload.Upload;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("list")
@Route(value = "")
public class ListView extends VerticalLayout {

    public ListView() {
        Kmeans kmeans = new Kmeans();

        IntegerField percentageField = new IntegerField("Percentage of data to read");
        IntegerField clustersField = new IntegerField("Number of clusters (K)");
        IntegerField iterationsField = new IntegerField("Number of iterations");
        NumberField outlierField = new NumberField("outliers distance");

        Div Suffix = new Div();
        Suffix.setText("%");

        percentageField.setSuffixComponent(Suffix);
        percentageField.setMin(0);
        percentageField.setMax(100);
        percentageField.setValue(100);
        clustersField.setValue(5);
        iterationsField.setValue(100);
        outlierField.setValue(1.5);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);


        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            Notification.show("File uploaded successfully!");
            kmeans.readMoviesFromFile(inputStream, percentageField.getValue());
        });

        Button processButton = new Button("Process");
        VerticalLayout gridsLayout = new VerticalLayout();
        processButton.addClickListener(event -> {
            // validate input
            if (percentageField.isEmpty() || clustersField.isEmpty() || iterationsField.isEmpty() || outlierField.isEmpty()) {
                Notification.show("Please fill all the fields");
                return;
            }
            if (percentageField.getValue() <= 1 || percentageField.getValue() > 100) {
                Notification.show("Please enter a valid percentage value");
                return;
            }
            // validate input file
            if (kmeans.getMovies().isEmpty()) {
                Notification.show("Please upload a file first");
                return;
            }


            Notification.show("Processing...");
            kmeans.K_means_algorithm(clustersField.getValue(), iterationsField.getValue(), outlierField.getValue());
            Notification.show("Processed!");


            // Clear the layout before adding new grids
            gridsLayout.removeAll();
            remove(gridsLayout);
//            gridsLayout.setSpacing(true);
            // Create a grid to display clusters
            Map<Double, List<Movie>> clusters = kmeans.getClustersOutliers().getKey();
            for (Map.Entry<Double, List<Movie>> clusterEntry : clusters.entrySet()) {
                Grid<Movie> clusterGrid = new Grid<>(Movie.class);
//                clusterGrid.setColumns("movie_Name", "IMDB_Rating","Release_Year", "Duration", "Metascore", "Votes", "Genre", "Director", "Cast", "Gross");

                // Set items for the grid
                clusterGrid.setItems(clusterEntry.getValue());

                H3 clusterTitle = new H3("Cluster with centroid: " + clusterEntry.getKey());
//                clusterTitle.getStyle().set("margin-top", "30px");
                // Add the grid to the layout
                gridsLayout.add(clusterTitle,clusterGrid);
            }

            // Create a grid to display outliers
            Grid<Movie> outlierGrid = new Grid<>(Movie.class);
//            outlierGrid.setColumns("movie_Name", "IMDB_Rating","Release_Year", "Duration", "Metascore", "Votes", "Genre", "Director", "Cast", "Gross");

            // Get outliers from the result map
            List<Movie> outliers = kmeans.getClustersOutliers().getValue();
            // Add outliers to the grid
            outlierGrid.setItems(outliers);

            H3 outliersTitle = new H3("Outliers");
            outliersTitle.getStyle().set("margin-top", "20px");
            // Add the outliers grid to the layout
            gridsLayout.add(outliersTitle,outlierGrid);

            add(gridsLayout);
        });

        HorizontalLayout NumericInput = new HorizontalLayout();
        HorizontalLayout Buttons = new HorizontalLayout();
        NumericInput.add(percentageField, clustersField, iterationsField, outlierField);
        Buttons.add(upload, processButton);
        NumericInput.setAlignItems(Alignment.BASELINE);
        Buttons.setAlignItems(Alignment.BASELINE);

        add(NumericInput, Buttons);





    }

}
