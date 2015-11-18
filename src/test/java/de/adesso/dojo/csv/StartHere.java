package de.adesso.dojo.csv;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class StartHere {

    private Tabellierer tabellierer;

    @Before
    public void createObjectUnderTest() {
        this.tabellierer = new Tabellierer();
    }

    @Test
    public void zeileWirdInSpaltenZerlegt() {
        String zeile = "lukas;ist;cool";
        assertThat(tabellierer.tokenize(zeile, ";")).hasSize(3);
        assertThat(tabellierer.tokenize(zeile, ";").get(1)).isEqualTo("ist");
    }

    @Test
    public void zeileWirdInSpaltenZerlegtMitAnderemSpalter() {
        String zeile = "lukas,ist,meistens,cool";
        assertThat(tabellierer.tokenize(zeile, ",")).hasSize(4);
        assertThat(tabellierer.tokenize(zeile, ",").get(0)).isEqualTo("lukas");
    }

    @Test
    public void parseTest() {
        List<List<String>> list = createParsedLines();
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).hasSize(5);
        assertThat(list.get(1)).hasSize(5);
    }

    @Test
    public void zeilenHabenGleicheLaenge() {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        int col1row1 = result.get(0).length();
        int col1row2 = result.get(2).length();
        assertThat(col1row1).isEqualTo(col1row2);
    }

    private List<List<String>> createParsedLines() {
        List<String> zeilen = new ArrayList<String>();
        zeilen.add("\"first_name\",\"last_name\",\"company_name\",\"address\",\"OtherCity\"");
        zeilen.add("\"first_nameIsLonger\",\"last_name1\",\"company_name1\",\"address\",\"city1\"");
        return tabellierer.parse(zeilen);
    }

    @Test
    public void formatLinesTest() {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        assertThat(result).hasSize(3);
    }

    @Test
    public void maximaleSpaltenLaengenWerdenBerechnet(){
        List<List<String>> list = createParsedLines();
        assertThat(tabellierer.getColumnSize(0)).isEqualTo(20);
        assertThat(tabellierer.getColumnSize(4)).isEqualTo(11);
    }

    @Test
    public void columnCountEquals() throws Exception {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        assertThat(result.get(0).split("\\|").length).isEqualTo(list.get(0).size());
    }

    @Test
    public void columnCountEqualsInHeadline() throws Exception {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        assertThat(result.get(1).split("\\+").length).isEqualTo(list.get(0).size());
    }

    @Test
    public void headlineHasDashes() throws Exception {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        String headline = result.get(1);
        assertThat(headline).startsWith("--------------------+");
    }

    @Test
    public void testRowPrint() {
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);

        String zeile = result.get(2);
        assertThat(zeile).isEqualTo("\"first_nameIsLonger\"|\"last_name1\"|\"company_name1\"|\"address\"|\"city1\"    |");
    }

    @Test
    public void testOut(){
        List<List<String>> list = createParsedLines();
        List<String> result = tabellierer.formatLines(list);
        String out = tabellierer.print(result);
        assertThat(out).isEqualTo(
                "\"first_name\"        |\"last_name\" |\"company_name\" |\"address\"|\"OtherCity\"|\n" +
                "--------------------+------------+---------------+---------+-----------+\n" +
                "\"first_nameIsLonger\"|\"last_name1\"|\"company_name1\"|\"address\"|\"city1\"    |\n");
    }
}
