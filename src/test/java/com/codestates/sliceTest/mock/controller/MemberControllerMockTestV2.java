package com.codestates.sliceTest.mock.controller;

// 중복 코드
import com.codestates.helper.MemberControllerTestHelper;
import com.codestates.helper.StubData;
//

import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 리펙토링 포인트
 * 1. 중복코드를 helper 패키지로 이동
 * 2. Mapper 클래스와 그 메서드들도 mocking 처리
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerMockTestV2 implements MemberControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberMapper mapper;
    @Test
    void postMemberTest() throws Exception {
        // given
        // StubData 클래스를 이용해 테스트 데이터를 매 번 정의하는 중복을 제거한다.
        MemberDto.Post post = (MemberDto.Post) StubData.MockMember.getRequestBody(HttpMethod.POST);

        // Stubbing by Mockito
        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());

        Member mockResultMember = new Member();
        mockResultMember.setMemberId(1L);
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(mockResultMember);


        String content = toJsonContent(post);
        URI uri = getURI();

        // when
        ResultActions actions = mockMvc.perform(postRequestBuilder(uri, content));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));
    }

    @Test
    void patchMemberTest() throws Exception {
        // given
        long memberId = 1L;

        MemberDto.Patch patch = (MemberDto.Patch) StubData.MockMember.getRequestBody(HttpMethod.PATCH);

        MemberDto.Response response =
                StubData.MockMember.getSingleResponseBody(null, "010-2222-2222", null);


        // Stubbing by Mockito
        given(mapper.memberPatchToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());

        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(new Member());

        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        String content = toJsonContent(patch);
        URI uri = getURI(memberId);

        // when
        ResultActions actions = mockMvc.perform(patchRequestBuilder(uri, content));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phone").value(patch.getPhone()));
    }

    @Test
    void getMemberTest() throws Exception {
        // given
        long memberId = 1L;
        Member member = StubData.MockMember.getSingleResultMember(memberId);
        MemberDto.Response response = StubData.MockMember.getSingleResponseBody();

        // Stubbing by Mockito
        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        URI uri = getURI(memberId);

        // when
        ResultActions actions = mockMvc.perform(getRequestBuilder(uri));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.phone").value(member.getPhone()));
    }

    @Test
    void getMembersTest() throws Exception {
        // given
        Page<Member> pageMembers = StubData.MockMember.getMultiResultMember();

        List<MemberDto.Response> responses = StubData.MockMember.getMultiResponseBody();

        // Stubbing by Mockito
        given(memberService.findMembers(Mockito.anyInt(), Mockito.anyInt())).willReturn(pageMembers);
        given(mapper.membersToMemberResponses(Mockito.anyList())).willReturn(responses);

        String page = "1";
        String size = "10";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", page);
        queryParams.add("size", size);

        URI uri = getURI();

        // when
        ResultActions actions = mockMvc.perform(getRequestBuilder(uri, queryParams));

        // then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();

        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");
        System.out.println(list);
        assertThat(list.size(), is(2));
    }

    @Test
    void deleteMemberTest() throws Exception {
        // given
        long memberId = 1L;

        // Stubbing by Mockito
        doNothing().when(memberService).deleteMember(memberId);

        // when
        ResultActions actions = mockMvc.perform(deleteRequestBuilder(getURI(memberId)));

        // then
        actions.andExpect(status().isNoContent());
    }
}