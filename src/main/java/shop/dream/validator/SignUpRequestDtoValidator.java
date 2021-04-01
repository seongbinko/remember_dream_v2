package shop.dream.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import shop.dream.dto.SignUpRequestDto;
import shop.dream.repository.AccountRepository;

@Component
@RequiredArgsConstructor
public class SignUpRequestDtoValidator implements Validator {

    private final AccountRepository accountRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpRequestDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpRequestDto signUpRequestDto = (SignUpRequestDto) o;

        if(accountRepository.existsByUsername(signUpRequestDto.getUsername())) {
            errors.rejectValue("username", "invalid.username", new Object[]{signUpRequestDto.getUsername()},"이미 사용중인 닉네임 입니다");
        }
        if(accountRepository.existsByEmail(signUpRequestDto.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpRequestDto.getEmail()},"이미 사용중인 이메일 입니다");
        }

        if(signUpRequestDto.getPassword().equals(signUpRequestDto.getUsername())) {
            errors.rejectValue("password", "wrong.value", "닉네임과 패스워드는 같을 수 없습니다.");
        }
        if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordConfirm())) {
            errors.rejectValue("password", "wrong.value", "입력한 패스워드가 서로 일치하지 않습니다.");
        }
        if(signUpRequestDto.isAdmin()) {
            if(!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                errors.rejectValue("admin", "wrong.admin","관리자 암호가 틀려 등록이 불가능합니다.");
                //throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
        }

    }
}
