import NumberInWords.NumberConvector;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DDTTest {
    private Map<Integer, List<String>> data;
    private String result;
    private NumberConvector numberConvector;

    @Before
    public void getDataBeforeTest() throws IOException {
        data = getTestData("test.xlsx");
        numberConvector = new NumberConvector();
    }


    @Test
    public void shouldUpgradeStatusBasedOnPointsEarned() throws IOException {
        for (int row = 1; row < data.size(); row++) {

            String numStr = data.get(row).get(0);
            BigDecimal curNumber = new BigDecimal(numStr);
            result = numberConvector.translateNumberToString(curNumber.toBigInteger());
            System.out.println("Строка - " + row);
            System.out.println("Результат - " + result);
            System.out.println("Ожидаемый - " + data.get(row).get(1));
            assertEquals(result, data.get(row).get(1));
        }
    }

    private Map<Integer, List<String>> getTestData(String path) throws IOException {

        FileInputStream file = new FileInputStream(new File(path));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        data.get(i).add(cell.getNumericCellValue() + "");
                        break;
                }
            }
            i++;
        }
        return data;
    }
}
