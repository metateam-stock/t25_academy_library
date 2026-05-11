package jp.co.metateam.library.model;

import java.security.Timestamp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
public class BookMstDto {
    @NotEmpty(message = "パスワードは必須です")
    @Size(min = 5, message="パスワードは5文字以上で入力してください")
    private Long id; 
    
    private String isbn;

    private String title;
    
    private Timestamp deletedAt;

    private BookMst bookMst;
}
