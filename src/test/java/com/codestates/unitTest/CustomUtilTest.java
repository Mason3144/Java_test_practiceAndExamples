package com.codestates.unitTest;

import com.codestates.coffee.entity.Coffee;
import com.codestates.member.entity.Member;
import com.codestates.utils.CustomBeanUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUtilTest {
    static Member preModifyMember = new Member();
    static Member targetMember = new Member();
    static Coffee preModifyCoffee = new Coffee();
    static Coffee targetCoffee = new Coffee();


    @BeforeAll
    public static void init(){
        preModifyMember.setMemberId(1l);
        preModifyMember.setPhone("010-1111-1111");
        preModifyMember.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);

        targetMember.setMemberId(1l);
        targetMember.setName("Mason");
        targetMember.setEmail("mason@test.com");
        targetMember.setPhone("010-2222-2222");
        targetMember.setMemberStatus(Member.MemberStatus.MEMBER_SLEEP);

        preModifyCoffee.setCoffeeId(1l);
        preModifyCoffee.setCoffeeStatus(Coffee.CoffeeStatus.COFFEE_SOLD_OUT);

        targetCoffee.setCoffeeId(1l);
        targetCoffee.setCoffeeCode("AAA");
        targetCoffee.setPrice(4000);
        targetCoffee.setEngName("Americano");
        targetCoffee.setKorName("아메리카노");
    }
    @DisplayName("A Member info updated")
    @Test
    public void modifyMemberTest(){
        CustomBeanUtils<Member> customBeanUtils = new CustomBeanUtils();

        Member postModifiedMember = customBeanUtils.copyNonNullProperties(preModifyMember,targetMember);

        assertAll(
                ()->assertEquals(postModifiedMember.getMemberId(), preModifyMember.getMemberId()),
                ()->assertNotEquals(postModifiedMember.getName(), preModifyMember.getName()),
                ()->assertNotEquals(postModifiedMember.getEmail(), preModifyMember.getEmail()),
                ()->assertEquals(postModifiedMember.getPhone(), preModifyMember.getPhone()),
                ()->assertEquals(postModifiedMember.getMemberStatus(), preModifyMember.getMemberStatus())
        );
    }

    @DisplayName("A Coffee info updated")
    @Test
    public void modifyCoffeeTest(){
        CustomBeanUtils<Coffee> customBeanUtils = new CustomBeanUtils();

        Coffee postModifiedCoffee = customBeanUtils.copyNonNullProperties(preModifyCoffee,targetCoffee);

        assertAll(
                ()->assertEquals(postModifiedCoffee.getCoffeeId(), preModifyCoffee.getCoffeeId()),
                ()->assertNotEquals(postModifiedCoffee.getCoffeeCode(), preModifyCoffee.getCoffeeCode()),
                ()->assertNotEquals(postModifiedCoffee.getKorName(), preModifyCoffee.getKorName()),
                ()->assertNotEquals(postModifiedCoffee.getEngName(), preModifyCoffee.getEngName()),
                ()->assertEquals(postModifiedCoffee.getCoffeeStatus(), preModifyCoffee.getCoffeeStatus())
        );
    }

    @DisplayName("Wrong parameter input")
    @Test
    public void wrongParameters(){
        CustomBeanUtils customBeanUtils = new CustomBeanUtils();

        Object wrongTest1 = customBeanUtils.copyNonNullProperties(null,null);
        Object wrongTest2 = customBeanUtils.copyNonNullProperties(preModifyMember,null);
        Object wrongTest3 = customBeanUtils.copyNonNullProperties(null,targetMember);
        Object wrongTest4 = customBeanUtils.copyNonNullProperties(preModifyMember,targetCoffee);

        assertAll(
                ()->assertNull(wrongTest1),
                ()->assertNull(wrongTest2),
                ()->assertNull(wrongTest3),
                ()->assertNotNull(wrongTest4,"Null값이 나와야 됩니다.")
        );
    }

}
