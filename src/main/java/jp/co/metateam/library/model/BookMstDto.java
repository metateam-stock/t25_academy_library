package jp.co.metateam.library.model;

import java.security.Timestamp;

import jakarta.validation.constraints.NotEmpty;
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
    
    private String isbn;

    private String title;
    
    private Timestamp deletedAt;

    private BookMst bookMst;

//バリデーションチェック
    @NotEmpty(message = "書籍名は必須です")
    public String Title;
    @Size(max = 255, message = "書籍名は255文字以内で入力してください")
    public String title;

    @NotEmpty(message = "ISBNは必須です")
    public String Isbn;
    @Pattern(regexp = "^[0-9]{13}$", message = "ISBNは13桁の数字で入力してください")
    public String isbn;
    
}
