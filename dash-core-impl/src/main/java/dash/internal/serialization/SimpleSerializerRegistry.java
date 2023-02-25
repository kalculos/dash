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

import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.util.CatCodes;
import io.ib67.kiwi.option.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SimpleSerializerRegistry implements ISerializerRegistry, IComponentSerializer {
    private final Map<String, IComponentSerializer> serializers = new HashMap<>();

    @Override
    public void registerComponentSerializer(String codeType, IComponentSerializer serializer) {
        serializers.put(codeType.toUpperCase(), serializer);
    }

    @Override
    public Option<IComponentSerializer> getComponentSerializer(String codeType) {
        return Option.of(serializers.get(codeType.toUpperCase()));
    }


    @Override
    public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
        if(code == null) throw new CatCodes.InvalidCatCodeException("code is null");
        return serializers.getOrDefault(code.getType(), FailureSerializer.INSTANCE).deserialize(code);
    }

    @Slf4j
    private static class FailureSerializer implements IComponentSerializer {
        private static final FailureSerializer INSTANCE = new FailureSerializer();

        @Override
        public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
            throw new CatCodes.InvalidCatCodeException("Cannot find serializer for " + code.getType());
        }
    }
}
