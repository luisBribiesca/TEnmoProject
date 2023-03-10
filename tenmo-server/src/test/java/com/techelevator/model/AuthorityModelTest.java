package com.techelevator.model;

import com.techelevator.tenmo.model.Authority;
import org.junit.Assert;
import org.junit.Test;

public class AuthorityModelTest {
    Authority authority = new Authority("User");
    String check = "User";

    @Test
    public void assessing_name_for_authority() {
        Assert.assertEquals(authority.getName(), check);
    }

    @Test
    public void is_authority() {
        Assert.assertTrue(authority.equals(authority));
    }

}
