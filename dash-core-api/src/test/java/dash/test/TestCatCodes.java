package dash.test;

import io.ib67.dash.util.CatCodes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCatCodes {
    @Test
    public void testBuilder() {
        var result = CatCodes.ofProps(
                "url", "nc://sb",
                "id", "114514"
        ).type("image").toString();
        var excepted = """
                [dash:IMAGE,id=114514,url=nc%3A%2F%2Fsb]
                """.trim();
        Assertions.assertEquals(result, excepted);
    }

    @Test
    public void testParser() {
        var data = """
                aaa[dash:IMAGE,id=114514,url=nc%3A%2F%2Fsb]aaa
                """.trim();
        var result = CatCodes.fromString(data);
        Assertions.assertEquals(data, CatCodes.toString(result));
    }
}
