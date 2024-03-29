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

package dash.internal.registry;

import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.IContact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleAdapterRegistryTest {
    private IAdapterRegistry adapterRegistry;
    @BeforeEach
    public void setup(){
        adapterRegistry = new SimpleAdapterRegistry();
    }
    @Test
    public void testRegistration(){
        adapterRegistry.registerAdapter(new MockAdapter());
        assertTrue(adapterRegistry.getAdapter("MOCK").orElseThrow() instanceof MockAdapter);
        assertTrue(adapterRegistry.allAdapters().toArray()[0] instanceof MockAdapter);
    }

    private static class MockAdapter extends PlatformAdapter {

        public MockAdapter() {
            super("MOCK");
        }

        @Override
        public Optional<? extends IContact> getContact(String platformId) {
            return Optional.empty();
        }

        @Override
        public List<? extends IContact> getAllContacts() {
            return null;
        }

        @Override
        public IContact getPlatformBot() {
            return null;
        }
    }

}