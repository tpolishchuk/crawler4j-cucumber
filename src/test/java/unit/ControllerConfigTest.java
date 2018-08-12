package unit;

import org.junit.Test;
import org.lrp.dtos.config.ControllerConfig;
import org.lrp.exceptions.MandatoryParameterException;
import org.lrp.helpers.config.ControllerConfigHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ControllerConfigTest extends AbstractUnitTest {

    @Test
    public void getConfigWithExistentCommonConfiguration() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        assertEquals("Unexpected politeness_delay is got from config", Integer.valueOf(1000), controllerConfig.getPolitenessDelay());
    }

    @Test
    public void getConfigWithExistentSeveralSeeds() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        assertEquals("Unexpected amount of seeds is got from config", 2, controllerConfig.getSeedConfigs().size());
    }

    @Test
    public void getSeedConfigOverridingCommonConfig() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        Integer maxDepthOfCrawling = controllerConfig.getParameter("http://demoqa.com/", "max_depth_of_crawling", Integer.class, true);

        assertEquals("Unexpected parameter is got from config", new Integer(5), maxDepthOfCrawling);
    }

    @Test(expected = MandatoryParameterException.class)
    public void getAbsentMandatoryParameter() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        controllerConfig.getParameter("http://demoqa.com/", "someparam", Integer.class, true);
    }

    @Test
    public void getAbsentNotMandatoryParameter() {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig("multiseed.controller.config.yaml");

        String param = controllerConfig.getParameter("http://demoqa.com/", "someparam", String.class, false);
        assertNull("Controller returned not null value", param);
    }
}
