package com.codestates.unitTest;

import com.codestates.helper.RandomPasswordGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RandomPasswordGeneratorTest {
    static int numberOfUpperLetters;
    static int numberOfLowerLetters;
    static int numberOfNumbers;
    static int numberOfSpecialLetters;
    static int totalNumberOfLetters;

    @BeforeAll
    public static void init(){
        numberOfUpperLetters = (int) Math.floor(Math.random()*10);
        numberOfLowerLetters = (int) Math.floor(Math.random()*10);
        numberOfNumbers = (int) Math.floor(Math.random()*10);
        numberOfSpecialLetters = (int) Math.floor(Math.random()*10);

        totalNumberOfLetters = numberOfUpperLetters+numberOfLowerLetters+numberOfNumbers+numberOfSpecialLetters;

        System.out.printf("Total: %d, Upper: %d, Lower: %d, Number: %d, Special: %d \n",
                totalNumberOfLetters,
                numberOfUpperLetters,
                numberOfLowerLetters,
                numberOfNumbers,
                numberOfSpecialLetters);

    }

    @DisplayName("실습 3: 랜덤 패스워드 생성 테스트")
    @Test
    public void generateTest() {
        // TODO 여기에 테스트 케이스를 작성해주세요.

        String randomString = RandomPasswordGenerator.generate(numberOfUpperLetters,
                numberOfLowerLetters,
                numberOfNumbers,
                numberOfSpecialLetters);

        StringBuilder onlyUpperLetters= new StringBuilder();
        StringBuilder onlyLowerLetters= new StringBuilder();
        StringBuilder onlyNumbers= new StringBuilder();
        StringBuilder onlySpecialLetters= new StringBuilder();

        for(int i=0; i<randomString.length(); i++){
            char oneLetter = randomString.charAt(i);
            if(oneLetter>=33&&oneLetter<=47) onlySpecialLetters.append(oneLetter);
            else if(oneLetter>=48&&oneLetter<=57) onlyNumbers.append(oneLetter);
            else if(oneLetter>=65&&oneLetter<=90) onlyUpperLetters.append(oneLetter);
            else if(oneLetter>=97&&oneLetter<=122) onlyLowerLetters.append(oneLetter);
        }

        assertEquals(totalNumberOfLetters, randomString.length());
        System.out.printf("Total Length passed \n");

        assertEquals(numberOfUpperLetters,onlyUpperLetters.length());
        System.out.printf("UpperLetters Length passed\n");

        assertEquals(numberOfLowerLetters,onlyLowerLetters.length());
        System.out.printf("LowerLetters Length passed\n");

        assertEquals(numberOfNumbers,onlyNumbers.length());
        System.out.printf("Numbers Length passed\n");

        assertEquals(numberOfSpecialLetters,onlySpecialLetters.length());
        System.out.printf("SpecialLetters Length passed\n");

    }
}
