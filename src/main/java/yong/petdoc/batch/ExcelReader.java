package yong.petdoc.batch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelReader implements ItemStreamReader<Row> {

    private final String filePath;
    private InputStream inputStream;
    private Workbook workbook;
    private Sheet sheet;
    private int currentRowIndex;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
        currentRowIndex = 1;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            inputStream = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Row read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentRowIndex > sheet.getLastRowNum()) {
            return null;
        }
        return sheet.getRow(currentRowIndex++);
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            if (workbook != null) {
                workbook.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
