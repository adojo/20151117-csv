package de.adesso.dojo.csv;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabellierer {

    private Map<Integer, Integer> columnSizes;

    public Tabellierer() {
        this.columnSizes = new HashMap<Integer, Integer>();
    }

    public List<List<String>> parse(List<String> rows) {
        List<List<String>> result = new ArrayList<List<String>>();
        for (String row : rows) {
            List<String> columns = tokenize(row, ",");

            for (int i = 0; i < columns.size(); i++) {
                if (!columnSizes.containsKey(i)) {
                    columnSizes.put(i, columns.get(i).length());
                } else {
                    columnSizes.put(i, Math.max(columnSizes.get(i), columns.get(i).length()));
                }
            }

            result.add(columns);
        }
        return result;
    }

    public List<String> formatLines(List<List<String>> lines) {
        List<String> result = new ArrayList<String>(lines.size() + 1);
        List<String> title = lines.get(0);
        List<List<String>> content = lines.subList(1, lines.size());
        result.add(formatLine(title));

        result.add(formatHeadline());

        for (List<String> line : content) {
            result.add(formatLine(line));
        }
        return result;
    }

    private String formatHeadline() {
        StringBuilder sb = new StringBuilder();
        for (Integer size : columnSizes.values()) {
            sb.append(StringUtils.repeat("-", size));
            sb.append('+');
        }
        return sb.toString();
    }

    private String formatLine(List<String> line) {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i<line.size();i++){
            result.append(StringUtils.rightPad(line.get(i), columnSizes.get(i)));
            result.append('|');
        }

        return result.toString();
    }

    public List<String> tokenize(String zeile, String spalter) {
        String[] split = zeile.split(spalter);
        return Arrays.asList(split);
    }

    public int getColumnSize(int columnIndex) {
        return columnSizes.get(columnIndex);
    }

    public String print(List<String> content){
        StringBuilder out = new StringBuilder();
        for (String line : content) {
            out.append(line);
            out.append(StringUtils.LF);
        }
        return out.toString();
    }
}
