import NumberInWords.NumberConvector;
import NumberInWords.NumberLengthException;
import jdk.Exported;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NumberConvectorTest {

    private NumberConvector convector = new NumberConvector();

    @Mock
    Map<Integer, String> data = Mockito.mock(Map.class);

    @Test
    public void trunslateNumberToString_positive() throws IOException {
        String result = convector.translateNumberToString(new BigInteger("32490234821"));
        String expectedResult = "тридцать два миллиарда четыреста девяносто миллионов двесте тридцать четыре тысячи восемьсот двадцать один";
        assertEquals(result, expectedResult);
    }

    @Test
    public void correctDeclension_positive() {
        String result = convector.correctDeclension(1, data);
        String expectedResult = "одна";
        assertEquals(result, expectedResult);
    }

    @Test
    public void correctDegreeDeclension_positive() {
        int degree = 6;
        char[] numberArray = {'2','3','4'};
        Mockito.when(data.get(degree)).thenReturn("миллион");
        String result = convector.correctDegreeDeclension(degree, numberArray, data);
        String expectedResult = "миллиона";
        assertEquals(result, expectedResult);
    }

    @Test
    public void insertWords_positive() {
        int degree = 6;
        char[] numberArray = {'2','3','4'};
        Mockito.when(data.get(200)).thenReturn("двесте");
        Mockito.when(data.get(30)).thenReturn("тридцать");
        Mockito.when(data.get(4)).thenReturn("четыре");
        String result = convector.insertWords(data, degree, numberArray);
        String expectedResult = "двесте тридцать четыре";
        assertEquals(result.trim(), expectedResult);
    }

    @Test
    public void insertWordsForThousands_positive() {
        int degree = 3;
        char[] numberArray = {'2','0','1'};
        Mockito.when(data.get(200)).thenReturn("двесте");
        String result = convector.insertWords(data, degree, numberArray);
        String expectedResult = "двесте одна";
        assertEquals(result.trim(), expectedResult);
    }

    @Test
    public void getData_positive() throws FileNotFoundException {
        String filename = "core.properties";
        assertFalse(convector.getData(filename).isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void getData_NullPointerException() throws FileNotFoundException {
        String filename = "ttt.prop";
        assertFalse(convector.getData(filename).isEmpty());
    }

    @Test
    public void numberInspection_positive() {
        int maxDegree = 9;
        BigInteger insertValue = new BigInteger("2134321");
        BigInteger expectedValue = new BigInteger("2134321");
        assertEquals(expectedValue, convector.numberInspection(insertValue, maxDegree));
    }

    @Test(expected = NumberLengthException.class)
    public void numberInspection_NumberLengthException() {
        int maxDegree = 3;
        BigInteger insertValue = new BigInteger("2134321");
        BigInteger expectedValue = new BigInteger("2134321");
        assertEquals(expectedValue, convector.numberInspection(insertValue, maxDegree));

    }

}
