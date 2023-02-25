/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
