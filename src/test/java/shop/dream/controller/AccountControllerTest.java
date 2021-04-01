package shop.dream.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import shop.dream.domain.Account;
import shop.dream.domain.AccountRole;
import shop.dream.repository.AccountRepository;
import shop.dream.service.AccountService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/signup"))
                .andExpect(model().attributeExists("signUpRequestDto"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        // 비밀 번호 불일치
        mockMvc.perform(post("/signup")
                .param("username", "seongbin")
                .param("email", "email@naver.com")
                .param("password", "12345")
                .param("password", "11111")
                .with(csrf()))
                .andExpect(status().isOk()) //잘못된 데이터지만 프론트에서 오기때문에 이값은 ok
                .andExpect(view().name("account/signup"))
                .andExpect(unauthenticated());
        // 올바르지 않은 닉네임
        mockMvc.perform(post("/signup")
                .param("username", "***")
                .param("email", "email@naver.com")
                .param("password", "123")
                .param("password", "1234")
                .with(csrf()))
                .andExpect(status().isOk()) //잘못된 데이터지만 프론트에서 오기때문에 이값은 ok
                .andExpect(view().name("account/signup"))
                .andExpect(unauthenticated());

        // 비밀번호와 닉네임 일치
        mockMvc.perform(post("/signup")
                .param("username", "1234")
                .param("email", "email@naver.com")
                .param("password", "1234")
                .param("password", "1234")
                .with(csrf()))
                .andExpect(status().isOk()) //잘못된 데이터지만 프론트에서 오기때문에 이값은 ok
                .andExpect(view().name("account/signup"))
                .andExpect(unauthenticated());
    }
    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/signup")
                .param("username", "koseongbin")
                .param("email", "koseongbin@email.com")
                .param("password", "12345678")
                .param("passwordConfirm", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(unauthenticated()); // 로그인은 하지 않기 때문

        Account account = accountRepository.findByUsername("koseongbin").orElse(null);
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertEquals(account.getRole() , AccountRole.ROLE_USER);
    }

}