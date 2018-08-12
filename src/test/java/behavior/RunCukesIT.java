package behavior;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/",
                 glue = {"behavior.steps"},
                 tags = {"not @excluded"})
public class RunCukesIT {

}
