package net.dusense.framework.extension.autotest;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockitoAnnotations;

/** 基于mock */
public class TestMockBase {

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
