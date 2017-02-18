package com.taskmanager.app.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link UUIDUtility}
 */
public class UUIDUtilityTest {

    @Test
    public void testUuid() {
        String uuid1 = UUIDUtility.newUuid();
        String uuid2 = UUIDUtility.newUuid();
        assertNotEquals(uuid1, uuid2);
    }

}