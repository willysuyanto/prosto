package com.ladokgi.apps.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordHasherTest {

    @Test
    public void newSaltIsUniqueHex32Chars() {
        String salt = PasswordHasher.newSalt();
        assertEquals(32, salt.length());
        assertNotEquals(salt, PasswordHasher.newSalt());
        assertTrue(salt.matches("[0-9a-f]{32}"));
    }

    @Test
    public void samePasswordAndSaltProduceSameHash() {
        String salt = PasswordHasher.newSalt();
        String first = PasswordHasher.hash("rahasia123", salt);
        String second = PasswordHasher.hash("rahasia123", salt);
        assertEquals(first, second);
        assertTrue(PasswordHasher.matches("rahasia123", salt, first));
    }

    @Test
    public void differentSaltProducesDifferentHash() {
        String a = PasswordHasher.hash("rahasia123", PasswordHasher.newSalt());
        String b = PasswordHasher.hash("rahasia123", PasswordHasher.newSalt());
        assertNotEquals(a, b);
    }

    @Test
    public void wrongPasswordDoesNotMatch() {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash("benar", salt);
        assertFalse(PasswordHasher.matches("salah", salt, hash));
        assertFalse(PasswordHasher.matches("", salt, hash));
    }

    @Test
    public void matchesIsCaseInsensitiveOnHashHex() {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash("benar", salt);
        assertTrue(PasswordHasher.matches("benar", salt, hash.toUpperCase()));
    }

    @Test
    public void nullInputsNeverMatch() {
        String salt = PasswordHasher.newSalt();
        assertFalse(PasswordHasher.matches(null, salt, "abc"));
        assertFalse(PasswordHasher.matches("x", null, "abc"));
        assertFalse(PasswordHasher.matches("x", salt, null));
    }
}
