//package com.codestates.sliceTest.springRestDocs;
//
//import com.codestates.member.controller.MemberController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//@WebMvcTest(MemberController.class)   // (1)
//@MockBean(JpaMetamodelMappingContext.class)   // (2)
//@AutoConfigureRestDocs    // (3)
//public class RestDocsBasicStructureTest {
//    @Autowired
//    private MockMvc mockMvc;  // (4)
//    @MockBean
//    // (5) 테스트 대상 Controller 클래스가 의존하는 객체를 Mock Bean 객체로 주입받기
//    @Test
//    public void postMemberTest() throws Exception {
//        // given
//        // (6) 테스트 데이터
//
//        // (7) Mock 객체를 이용한 Stubbing
//
//        // when
//        ResultActions actions =
//                mockMvc.perform(
//                        // (8) request 전송
//                );
//
//        // then
//        actions
//                .andExpect(// (9) response에 대한 기대 값 검증)
//                .andDo(document(
//                        // (10) API 문서 스펙 정보 추가
//                ));
//    }
//}