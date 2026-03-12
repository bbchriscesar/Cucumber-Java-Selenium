package io.cucumber.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SortableDataTablesPage extends BasePage {

    @FindBy(id = "table1")
    private WebElement table1;

    @FindBy(id = "table2")
    private WebElement table2;

    public SortableDataTablesPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets all data rows from Example 1 table (table1).
     * Each row is represented as a Map with column headers as keys.
     */
    public List<Map<String, String>> getTable1Data() {
        return getTableData(table1);
    }

    /**
     * Gets all data rows from Example 2 table (table2).
     */
    public List<Map<String, String>> getTable2Data() {
        return getTableData(table2);
    }

    /**
     * Gets a specific column's values from Example 1 table.
     */
    public List<String> getTable1ColumnValues(String columnName) {
        return getColumnValues(table1, columnName);
    }

    /**
     * Gets all email addresses from Example 1 table.
     */
    public List<String> getTable1Emails() {
        return getTable1ColumnValues("Email");
    }

    /**
     * Gets all website URLs from Example 1 table.
     */
    public List<String> getTable1Websites() {
        return getTable1ColumnValues("Web Site");
    }

    /**
     * Returns the total number of data rows in Example 1 table.
     */
    public int getTable1RowCount() {
        List<WebElement> rows = table1.findElements(By.cssSelector("tbody tr"));
        log.info("Table 1 has {} data rows", rows.size());
        return rows.size();
    }

    private List<Map<String, String>> getTableData(WebElement table) {
        List<Map<String, String>> tableData = new ArrayList<>();

        List<WebElement> headers = table.findElements(By.cssSelector("thead th"));
        List<String> headerTexts = headers.stream()
                .map(h -> h.getText().trim())
                .toList();

        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Map<String, String> rowData = new LinkedHashMap<>();
            for (int i = 0; i < Math.min(headerTexts.size(), cells.size()); i++) {
                rowData.put(headerTexts.get(i), cells.get(i).getText().trim());
            }
            tableData.add(rowData);
        }

        log.info("Extracted {} rows from table", tableData.size());
        return tableData;
    }

    private List<String> getColumnValues(WebElement table, String columnName) {
        List<WebElement> headers = table.findElements(By.cssSelector("thead th"));
        int columnIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                columnIndex = i + 1; // CSS nth-child is 1-based
                break;
            }
        }

        if (columnIndex == -1) {
            throw new RuntimeException("Column '" + columnName + "' not found in table headers");
        }

        List<WebElement> cells = table.findElements(
                By.cssSelector("tbody tr td:nth-child(" + columnIndex + ")"));

        return cells.stream()
                .map(c -> c.getText().trim())
                .toList();
    }
}
