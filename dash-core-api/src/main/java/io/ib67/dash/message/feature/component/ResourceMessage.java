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

package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * A {@link ResourceMessage} is a {@link IMessageComponent} that is linked to external resources.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class ResourceMessage implements IMessageComponent {
    private final String type;
    @Getter
    private final Path path;

    protected ResourceMessage(String type, Path path) {
        this.type = type;
        this.path = path;
    }

    @SneakyThrows
    public InputStream openInputStream() {
        return path.toUri().toURL().openStream();
    }

    @Override
    public String toCatCode() {
        return CatCodes.ofProps(
                "path", path.toString()
        ).type(type).toString();
    }
}
