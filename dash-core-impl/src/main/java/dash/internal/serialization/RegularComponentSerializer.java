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

package dash.internal.serialization;

import dash.internal.DashImpl;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.*;
import io.ib67.dash.util.CatCodes;
import io.ib67.kiwi.Kiwi;
import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class RegularComponentSerializer implements IComponentSerializer {
    /**
     * @implNote while a new parser strategy is added, also register it on {@link DashImpl#registerStandardCatCodes()}
     * @param code
     * @return
     * @throws CatCodes.InvalidCatCodeException
     */
    @Override
    public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
        return switch (code.getType().toUpperCase()) {
            case "TEXT" -> new Text(code.getProp(CatCodes.CatCode.TEXT_PROP_KEY));
            case "AT" -> {
                if (code.getProp("platform") != null) {
                    yield new At(null, code.getProp("platform"), code.getProp("display"), code.getProp("target"));
                }
                yield Kiwi.todo("convert to contact");
            }
            case "IMAGE" -> Image.builder().path(getPath(code)).build();
            case "FILE" -> File.builder().path(getPath(code)).build();
            case "ACTION" -> new Action(Action.Type.valueOf(code.getProp("type")));
            case "AUDIO" -> Audio.builder().path(getPath(code)).build();
            case "STICKER" -> new Sticker(code.getProp("path") != null ? getPath(code) : null
                    , code.getProp("platform"), Integer.parseInt(code.getProp("id")));
            default -> throw new IllegalArgumentException("Unknown catcode type for RegularComponentSerializer, this is unexpected.");
        };
    }

    private Path getPath(CatCodes.CatCode code) {
        return Path.of(URLDecoder.decode(code.getProp("path"), StandardCharsets.UTF_8));
    }
}
