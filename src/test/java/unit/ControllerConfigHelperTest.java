package unit;

import org.junit.Test;
import org.lrp.dtos.config.ControllerConfig;
import org.lrp.exceptions.ConfigNotFoundException;
import org.lrp.helpers.config.ControllerConfigHelper;

import static org.junit.Assert.assertNotNull;

public class ControllerConfigHelperTest extends AbstractUnitTest {

    @Test
    public void getConfigWithExistentFields() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        assertNotNull("Config is null", controllerConfig);
    }

    @Test(expected = ConfigNotFoundException.class)
    public void getNonexistentConfig() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        controllerConfigHelper.getConfig("somename.yaml");
    }
}
