package com.codestates.sliceTest.mock.controller;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.codestates.stamp.Stamp;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerMockTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private MemberService memberService;
    @Autowired
    private MemberMapper mapper;
    @Test
    void postMemberTest() throws Exception {
        // given
        MemberDto.Post post = new MemberDto.Post("mason@test.com",
                                                        "Mason",
                                                    "010-1234-5678");

        Member member = mapper.memberPostToMember(post);
        member.setMemberId(1L);
        member.setStamp(new Stamp());

        // stubbing
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(member);

        String content = gson.toJson(post);


        // when
        ResultActions actions =
                mockMvc.perform(
                                    post("/v11/members")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content)
                                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));
    }
    @Test
    void patchMemberTest() throws Exception {
        MemberDto.Patch patchMember = MemberDto.Patch.builder().phone("010-2222-2222").build();

        Member newMember = mapper.memberPatchToMember(patchMember);
        newMember.setStamp(new Stamp());
        newMember.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);


        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(newMember);

        String content = gson.toJson(patchMember);

        ResultActions actions = mockMvc.perform(
                patch("/v11/members/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phone").value(patchMember.getPhone()));
    }

    @Test
    void getMemberTest() throws Exception {
        Member member = new Member("mason@test.com","Mason","010-1111-1111");
        member.setMemberId(1l);
        member.setStamp(new Stamp());

        given(memberService.findMember(Mockito.anyLong())).willReturn(member);

        ResultActions actions = mockMvc.perform(
                get("/v11/members/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(member.getMemberId()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.phone").value(member.getPhone()));
    }

    @Test
    void getMembersTest() throws Exception {
        Member member1 = new Member("mason1@test.com","Mason1","010-1111-1111");
        Member member2 = new Member("mason2@test.com","Mason2","010-2222-2222");
        member1.setStamp(new Stamp());
        member2.setStamp(new Stamp());

        List<Member> list = new LinkedList<>();
        list.add(member1);
        list.add(member2);

        given(memberService.findMembers(Mockito.anyInt(),Mockito.anyInt())).willReturn(new PageImpl<Member>(list));

        String page = "1";
        String size = "10";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", page);
        queryParams.add("size", size);

        ResultActions actions = mockMvc.perform(
                get("/v11/members")
                        .params(queryParams)
        );

        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.pageInfo.size").value(2))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();

        List<Member> resultList = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");

        MatcherAssert.assertThat(resultList.size(), CoreMatchers.is(2));
    }

    @Test
    void deleteMemberTest() throws Exception {
        ResultActions actions = mockMvc.perform(
                delete("/v11/members/1")
        );

        actions.andExpect(status().isNoContent());
    }
}