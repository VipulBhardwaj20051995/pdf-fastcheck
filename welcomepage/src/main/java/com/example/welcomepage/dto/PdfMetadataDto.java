package com.example.welcomepage.dto;

public class PdfMetadataDto {
    private String author;
    private String title;
    private String producer;
    private String creator;
    private boolean originalBankStatement;

    // Getters and setters

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isOriginalBankStatement() {
        return originalBankStatement;
    }

    public void setOriginalBankStatement(boolean originalBankStatement) {
        this.originalBankStatement = originalBankStatement;
    }
}
