package yong.petdoc.batch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import yong.petdoc.exception.CustomException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static yong.petdoc.exception.ErrorCode.*;

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
        } catch (FileNotFoundException e) {
            throw new CustomException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CustomException(FILE_OPEN_FAILED);
        }
    }

    @Override
    public Row read() {
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
            throw new CustomException(FILE_CLOSE_FAILED);
        }
    }
}
