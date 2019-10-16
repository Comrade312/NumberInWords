package NumberInWords;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static NumberInWords.NumberDeclensionContsrants.*;

public class NumberConvector {
    public String translateNumberToString(BigInteger num) throws IOException {
        Map<Integer, String> data = readData("core.xlsx");
        Map<Integer, String> dataDegree = readData("degree.xlsx");

        String result = "";
        String numStr = numberInspection(num, Collections.max(dataDegree.keySet())).toString();

        int degree = (numStr.length() - 1 - (numStr.length() - 1) % 3);
        int stepLength = (numStr.length() % 3 == 0) ? 3 : numStr.length() % 3;
        int numberStep = 0;
        int degreeStep = 0;

        for (int i = degree / 3 + 1; i > 0; i--) {
            char[] numberInterArray = numStr.substring(numberStep, numberStep + stepLength).toCharArray();
            result = result.concat(insertWords(data, degree - degreeStep, numberInterArray));
            numberStep += stepLength;
            stepLength = 3;

            String tmpDegree = correctDegreeDeclension(degree - degreeStep, numberInterArray, dataDegree);
            result = (tmpDegree != null) ? result.concat(tmpDegree + " ") : result;
            degreeStep += 3;

        }
        return result.trim();
    }

    public String correctDeclension(Integer number, Map<Integer, String> data) {
        switch (number) {
            case 1:
                return ONE_FEM;
            case 2:
                return TWO_FEM;
            default:
                return data.get(number);
        }
    }

    public String correctDegreeDeclension(Integer degree, char[] numberArray, Map<Integer, String> dataDegree) {
        Integer value = (numberArray.length > 1 && numberArray[numberArray.length - 2] == '1')
                ? Character.getNumericValue('1' + numberArray[numberArray.length - 1])
                : Character.getNumericValue(numberArray[numberArray.length - 1]);

        if (degree > 0) {
            if (degree == 3) {
                switch (value) {
                    case 1:
                        return dataDegree.get(degree);
                    case 2:
                    case 3:
                    case 4:
                        return ONE_THOUSAND_PLURAL;
                    default:
                        return ONE_THOUSAND_PLURAL_GENTITIVE;
                }
            } else {
                switch (value) {
                    case 1:
                        return dataDegree.get(degree);
                    case 2:
                    case 3:
                    case 4:
                        return dataDegree.get(degree) + DEGREE_SINGULAR_ENDING;
                    default:
                        return dataDegree.get(degree) + DEGREE_PLURAL_ENDING;
                }
            }
        } else return null;
    }

    public String insertWords(Map<Integer, String> data, Integer degree, char[] numberArray) {
        String str = "";
        for (int j = 0; j < numberArray.length; j++) {
            Integer localDegree = (int) Math.pow(10, numberArray.length - 1 - j);
            Integer number = (numberArray[j] == '1' && j == numberArray.length - 2)
                    ? Character.getNumericValue(numberArray[j]) * localDegree + Character.getNumericValue(numberArray[j++ + 1])
                    : Character.getNumericValue(numberArray[j]) * localDegree;
            String wordValue = (degree == 3 && (numberArray.length == 1 || numberArray[numberArray.length - 2] != '1'))
                    ? correctDeclension(number, data)
                    : data.get(number);

            if (wordValue != null) {
                str = str.concat(wordValue + " ");
            }
        }
        return str;
    }

    public Map<Integer, String> readData(String path) throws IOException {
        FileInputStream file = new FileInputStream(new File(path));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, String> data = new HashMap<>();
        int curValue = 0;
        int counter = 0;
        for (Row row : sheet) {
            for (Cell cell : row) {
                if(counter % 2 == 0){
                    curValue = (int) cell.getNumericCellValue();
                }
                else {
                    data.put(curValue, cell.getRichStringCellValue().getString());
                }
                counter++;
            }
        }
        return data;
    }

    public BigInteger numberInspection(BigInteger number, int maxDegree) throws NumberLengthException{
        int numberLength = (int) (Math.log10(number.doubleValue()) + 1);
        BigDecimal maxNumber = BigDecimal.valueOf(Math.pow(10, maxDegree + 3) - 1);

        if(numberLength > maxDegree + 3) {
            throw new NumberLengthException("Число не должно превышать " + maxNumber.toPlainString());
        }

        return number;
    }

    public static void main(String[] args) throws IOException {
        NumberConvector tmp = new NumberConvector();
        System.out.println(tmp.translateNumberToString(new BigInteger("12321321321")));
    }
}
