package com.example.demo;

public class Word {
    String name="";
    String type ="";
    String meaning ="";
    String example ="";
    String meaningTR ="";
    String level ="";

    public Word(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaningTR() {
        return meaningTR;
    }

    public void setMeaningTR(String meaningTR) {
        this.meaningTR = meaningTR;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
