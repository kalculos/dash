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

package io.ib67.dash.util;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility for parsing / generating CatCodes.<br />
 * A {@code CatCode} is a String representation of a {@link io.ib67.dash.message.feature.CompoundMessage}.<br />
 * CatCode Specification: <a href="https://github.com/kalculos/dash/blob/main/docs/spec/CatCode.md">About CatCode</a> <br /?
 * <p>
 * Note: This is not ForteScarlet/CatCode2. But you can parse our codes according to their specification.
 * Note: This utility is independent. You can copy it under our License.
 */
@ApiStatus.AvailableSince("0.1.0")
@Deprecated
public class CatCodes {

    public static String toString(Collection<? extends CatCode> catCodes) {
        return catCodes.stream().map(CatCode::toString).collect(Collectors.joining());
    }

    /**
     * Parse CatCodes from String.
     *
     * @param code a text contains catcode.
     * @return some catcode
     */
    public static List<CatCode> fromString(String code) {
        return new CatCodeParser(code).get();
    }

    public static CatCode newCatCode(String type) {
        return new CatCode().type(type);
    }

    public static CatCode ofProps(String... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("it must be key-value pairs.");
        }
        var builder = new CatCode();
        for (var i = 0; i < args.length; i++) {
            if (i == 0) continue;
            var last = i - 1;
            builder.prop(args[last], args[i]);
            i++;
        }
        return builder;
    }

    public static class CatCode {
        public static final String TEXT_PROP_KEY = "content";
        private final Map<String, String> properties = new HashMap<>();
        @Getter
        private String type;

        public CatCode type(String type) {
            Objects.requireNonNull(this.type = type.toUpperCase());
            return this;
        }

        public CatCode prop(String key, String value) {
            properties.put(key, URLEncoder.encode(value, StandardCharsets.UTF_8));
            return this;
        }
        public String getProp(String key){
            return properties.get(key);
        }

        @Override
        public String toString() {
            if (type.equals("TEXT")) return properties.get(TEXT_PROP_KEY);
            if (properties.isEmpty()) return "[dash:" + type + "]";
            return "[dash:" + type + "," +
                    properties.entrySet().stream()
                            .map(it -> it.getKey() + "=" + it.getValue())
                            .collect(Collectors.joining(",")) + "]";
        }
    }

    private static class CatCodeParser {
        private final ByteBuffer buffer;

        private CatCodeParser(String content) {
            this.buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        }

        public List<CatCode> get() {
            var result = new ArrayList<CatCode>();
            var begin = buffer.position();
            while (buffer.hasRemaining()) {
                switch (buffer.get()) {
                    case (byte) '\\' -> {
                        if (buffer.hasRemaining()) {
                            buffer.position(buffer.position() + 1);
                        }
                    }
                    case (byte) '[' -> {
                        if(buffer.position() - 1 - begin != 0){
                            result.add(saveLiteral(begin, buffer.position() - 1));
                        }
                        result.add(readCatCode());
                        begin = buffer.position(); // ]a
                    }
                    default -> {
                        // collect...
                    }
                }
            }
            result.add(saveLiteral(begin, buffer.position()));
            return result;
        }

        private String readString(int begin, int dist) {
            var len = dist - begin;
            var dst = new byte[len];
            var now = buffer.position();
            buffer.position(begin);
            buffer.get(dst, 0, len);
            buffer.position(now);
            return new String(dst,StandardCharsets.UTF_8);
        }

        private CatCode saveLiteral(int begin, int dst) {
            return CatCodes.newCatCode("TEXT").prop("content", readString(begin, dst));
        }

        private CatCode readCatCode() {
            var code = new CatCode();
            var begin = buffer.position();
            while (buffer.hasRemaining()) {
                var b = buffer.get();
                if (b == (byte) ']') {//end
                    return code;
                } else {
                    buffer.position(buffer.position() - 1);
                    readProperty(code);
                }
            }
            return code;
        }

        private void readProperty(CatCode cc) {
            var begin = buffer.position();
            var isHead = false;
            String key = null;
            while (buffer.hasRemaining()) {
                var b = buffer.get();
                switch (b) {
                    case ':':
                        isHead = true;
                    case '=':
                        key = readString(begin, buffer.position() - 1);
                        begin = buffer.position();
                        break;
                    case ']', ',':
                        var value = URLDecoder.decode(readString(begin, buffer.position() - 1), StandardCharsets.UTF_8);
                        if (isHead) {
                            cc.type = value;
                        } else {
                            cc.prop(key, value);
                        }
                        if (b == (byte) ']') {
                            buffer.position(buffer.position() - 1);
                        }
                        return;
                }
            }
            throw new InvalidCatCodeException("EOF");
        }
    }

    public static class InvalidCatCodeException extends RuntimeException{
        public InvalidCatCodeException() {
            super();
        }

        public InvalidCatCodeException(String message) {
            super(message);
        }

        public InvalidCatCodeException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidCatCodeException(Throwable cause) {
            super(cause);
        }

        protected InvalidCatCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
