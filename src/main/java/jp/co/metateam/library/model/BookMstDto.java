package jp.co.metateam.library.model;

import java.security.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
public class BookMstDto {
 
    private Long id;
 
    @NotBlank(message = "ISBNは必須です")
    @Size(min = 13, max = 13, message = "ISBNは13文字で入力してください")
    @Pattern(regexp = "^[0-9]+$", message = "ISBNは半角数字のみで入力してください")
    private String isbn;
 
    @NotBlank(message = "書籍名は必須です")
    @Size(max = 255, message = "書籍名は255文字以下で入力してください")
    private String title;
 
    private Timestamp deletedAt;
 
    private BookMst bookMst;
}