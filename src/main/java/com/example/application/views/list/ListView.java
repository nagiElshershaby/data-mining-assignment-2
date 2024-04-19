package com.example.application.views.list;

import com.example.application.data.Movie;
import com.example.application.services.Kmeans;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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
        outlierField.setValue(1.0);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);


        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            Notification.show("File uploaded successfully!");
            kmeans.readMoviesFromFile(inputStream, percentageField.getValue());
        });

        Button processButton = new Button("Process");
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


            Notification.show("Processing...");
            kmeans.K_means_algorithm(clustersField.getValue(), iterationsField.getValue(), outlierField.getValue());
            Notification.show("Processed!");
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
