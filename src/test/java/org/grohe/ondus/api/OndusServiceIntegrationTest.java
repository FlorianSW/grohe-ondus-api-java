package org.grohe.ondus.api;

import org.junit.Test;

import javax.security.auth.login.LoginException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OndusServiceIntegrationTest {
    @Test
    public void wrongUsernameForWebForm() throws Exception {
        try {
            OndusService.loginWebform("a-test", "a-password");
        } catch (LoginException e) {
            assertThat(e.getMessage(), equalTo("Invalid username/password"));
            return;
        }
        fail("Should throw LoginException");
    }
}
