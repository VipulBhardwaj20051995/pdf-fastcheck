package com.example.welcomepage.service;

import com.example.welcomepage.dto.PdfMetadataDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PdfMetadataService {

    private static final Pattern FOUR_CHAR_PATTERN = Pattern.compile(".{4}");
    private static final List<String> KNOWN_BANK_PRODUCERS = List.of(
            "iText 4.2.0 by 1T3XT",
            "^ﬂ2ã",
            "Ì\r™g",
            "r\bv»",
            "¬´E"
    );
    private static final Pattern BANK_PRODUCER_PATTERN = Pattern.compile(
            "iText\\s\\d+\\.\\d+\\.\\d+\\sby\\s1T3XT|" +
                    "\\^ﬂ2ã|" +
                    "Ì\\r™g|" +
                    "r\\bv»|" +
                    "¬´E",
            Pattern.CASE_INSENSITIVE
    );

    public PdfMetadataDto extractPdfMetadata(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".pdf");
        file.transferTo(tempFile);
        try (PDDocument document = PDDocument.load(tempFile)) {
            return extractMetadata(document);
        } finally {
            tempFile.delete();
        }
    }

    public PdfMetadataDto extractPdfMetadata(MultipartFile file, String password) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".pdf");
        file.transferTo(tempFile);
        try (PDDocument document = PDDocument.load(tempFile, password)) {
            return extractMetadata(document);
        } finally {
            tempFile.delete();
        }
    }

    private PdfMetadataDto extractMetadata(PDDocument document) {
        PDDocumentInformation info = document.getDocumentInformation();
        PdfMetadataDto metadataDto = new PdfMetadataDto();
        metadataDto.setCreator(info.getCreator());
        metadataDto.setProducer(info.getProducer());
        metadataDto.setAuthor(info.getAuthor());
        metadataDto.setTitle(info.getTitle());
        metadataDto.setOriginalBankStatement(isBankStatement(document));
        return metadataDto;
    }

    private boolean isBankStatement(PDDocument document) {
        PDDocumentInformation info = document.getDocumentInformation();
        String producer = info.getProducer();
        return producer != null &&
                (FOUR_CHAR_PATTERN.matcher(producer).matches() ||
                        isKnownBankProducer(producer) ||
                        isBankProducer(producer));
    }

    private boolean isKnownBankProducer(String producer) {
        return KNOWN_BANK_PRODUCERS.contains(producer);
    }

    private boolean isBankProducer(String producer) {
        return BANK_PRODUCER_PATTERN.matcher(producer).find();
    }
}
