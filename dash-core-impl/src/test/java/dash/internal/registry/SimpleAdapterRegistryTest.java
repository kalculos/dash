package dash.internal.registry;

import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Contact;
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
        public Optional<? extends Contact> getContact(String platformId) {
            return Optional.empty();
        }

        @Override
        public Optional<? extends Contact> getContact(long id) {
            return Optional.empty();
        }

        @Override
        public List<? extends Contact> getAllContacts() {
            return null;
        }

        @Override
        public Contact getPlatformBot() {
            return null;
        }
    }

}