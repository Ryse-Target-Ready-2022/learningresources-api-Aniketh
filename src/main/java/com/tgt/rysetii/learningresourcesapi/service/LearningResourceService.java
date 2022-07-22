package com.tgt.rysetii.learningresourcesapi.service;

import com.tgt.rysetii.learningresourcesapi.entity.LearningResource;
import com.tgt.rysetii.learningresourcesapi.entity.LearningResourceStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LearningResourceService {
    public List<LearningResource> getLearningResources(){
        File file = new File("LearningResources.csv");
        List<LearningResource> learningResources = loadLearningResourceFromCsv(file);
        return learningResources;
    }


    private List<LearningResource> loadLearningResourceFromCsv(File file) {
        List<LearningResource> learningResources = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = null;
            line = bufferedReader.readLine();
            while (line!=null){
                String[] attributes = line.split(",");
                LearningResource learningResource = createLearningResource(attributes);
                learningResources.add(learningResource);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return learningResources;
    }

    private LearningResource createLearningResource(String[] attributes) {
        Integer id = Integer.parseInt(attributes[0].replaceAll("\\D+", ""));
        String resourceName = attributes[1];
        Double costPrice = Double.parseDouble(attributes[2]);
        Double sellingPrice = Double.parseDouble(attributes[3]);
        LearningResourceStatus learningResourceStatus = LearningResourceStatus.valueOf(attributes[4]);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate createdDate = LocalDate.parse(attributes[5],dateTimeFormatter);
        LocalDate publishedDate = LocalDate.parse(attributes[6],dateTimeFormatter);
        LocalDate retiredDate = LocalDate.parse(attributes[7],dateTimeFormatter);
        LearningResource learningResource = new LearningResource(id,resourceName,costPrice,sellingPrice,learningResourceStatus,createdDate,publishedDate,retiredDate);
        return learningResource;
    }
    public void saveLearningResources(List<LearningResource> learningResources){
        populateLearningResourcesToCsv(learningResources);
    }

    private void populateLearningResourcesToCsv(List<LearningResource> learningResources){
        final String delimiter = ",";
        try {
            FileWriter file = new FileWriter("LearningResources.csv",true);
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            for(LearningResource learningResource:learningResources){
                bufferedWriter.newLine();
                StringBuffer line = new StringBuffer();
                line.append(learningResource.getId());
                line.append(delimiter);
                line.append(learningResource.getProductName());
                line.append(delimiter);
                line.append(learningResource.getCostPrice());
                line.append(delimiter);
                line.append(learningResource.getSellingPrice());
                line.append(delimiter);
                line.append(learningResource.getLearningResourceStatus());
                line.append(delimiter);
                line.append(learningResource.getCreatedDate());
                line.append(delimiter);
                line.append(learningResource.getPublishedDate());
                line.append(delimiter);
                line.append(learningResource.getRetiredDate());
                line.append(delimiter);
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Double> getProfitMargin(){
        List<LearningResource> learningResources = getLearningResources();
        List<Double> profitMargin = learningResources.stream().map(e -> ((e.getSellingPrice()-e.getCostPrice())/e.getSellingPrice())).collect(Collectors.toList());
        return profitMargin;
    }

    public List<LearningResource> sortLearningResourcesByProfitMargin(){
        List<LearningResource> learningResources = getLearningResources();

        learningResources.sort((lr1, lr2) -> {
            Double profitMargin1 = (lr1.getSellingPrice() - lr1.getCostPrice())/lr1.getSellingPrice();
            Double profitMargin2 = (lr2.getSellingPrice() - lr2.getCostPrice())/lr2.getSellingPrice();

            return profitMargin2.compareTo(profitMargin1) ;
        });

        return learningResources;
    }
}