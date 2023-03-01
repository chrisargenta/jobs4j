package jobs4j;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import jobs4j.utils.Config;

class TestConfig {

	@Test
	void test() {
		Config config = Config.getInstance();
		if (config == null)
			fail("Config.getInstance returned null");

		try {
			Integer n = Integer.parseInt(config.get(Config.PROPERTY_LOCAL_THREAD_COUNT));
			if (n < 1 || n > 9999)
				fail("Value (" + n + ") out of reasonable range of 1-9999");

		} catch (NumberFormatException | IOException e) {
			fail(e.getMessage());
		}
	}

}
