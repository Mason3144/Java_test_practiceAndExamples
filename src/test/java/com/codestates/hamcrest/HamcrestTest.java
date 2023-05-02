package com.codestates.hamcrest;

import com.codestates.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HamcrestTest {
    @DisplayName("JUnit test using Hamcrest")
    @Test
    public void assertionTest1(){
        String expected = "Hello, JUnit";
        String actual = "Hello, JUnit";

        Member exampleObject = new Member();
        Object test = null;

        Throwable actualException = assertThrows(NullPointerException.class,
                ()->exampleObject.getPhone().toUpperCase());



        assertAll(
                ()-> assertThat(actual, is(equalTo(expected))),
                ()-> assertThat(actual, is(notNullValue())),
                ()-> assertThat(actualException.getClass(), is(NullPointerException.class))

        );


    }
}
