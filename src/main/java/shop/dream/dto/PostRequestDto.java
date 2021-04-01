package shop.dream.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class PostRequestDto {

    @NotBlank
    @Length(min = 3, max = 20)
    private String title;

    @NotBlank
    @Length(min = 30, max = 500)
    private String contents;

}
