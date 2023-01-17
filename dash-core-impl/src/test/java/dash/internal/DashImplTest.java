package dash.internal;

import dash.test.SharedResources;
import dash.test.util.Utility;
import io.ib67.dash.Dash;
import net.sf.persism.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DashImplTest {
    private Dash dash;
    private static final Session session = Utility.createSession();
    @BeforeEach
    public void setup(){
        dash = new DashImpl(null, SharedResources.mainLoop,SharedResources.asyncPool);
    }

    @Test
    public void someWeirdCodesForCoverage(){

    }

}