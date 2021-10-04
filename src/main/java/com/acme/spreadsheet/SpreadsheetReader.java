package com.acme.spreadsheet;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpreadsheetReader {

    private InputStream inputStream;

    public SpreadsheetReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<LinkedHashMap<String, String>> read() {
        List<String[]> rows;
        try (var reader = new CSVReader(new InputStreamReader(this.inputStream))) {
            rows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
        var header = rows.get(0);
        var result = rows.stream()
                .skip(1)
                .map(row -> IntStream.range(0, row.length)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> header[i],
                                i -> row[i],
                                (a, b) -> b,
                                LinkedHashMap::new)))
                .toList();
        return result;
    }
}
