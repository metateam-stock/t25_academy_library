package jp.co.metateam.library.model;

import java.security.Timestamp;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
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
    
    private Long id; 
   @NotEmpty(message = "ISBNは必須です")
    @Size(max = 13,message = "ISBNは13文字以下で入力してください")
    @Pattern(regexp = "^[0-9]+$" ,message= "ISBNの形が不正です。数値のみで入力してください")
    private String isbn;//テキストボックス内のデータを確認する

    @NotEmpty(message = "書籍名は必須です")
    @Size(max = 255,message = "書籍名は255文字以下で入力してください")
    private String title;//テキストボックス内のデータを確認する
    
    private Timestamp deletedAt;

    private BookMst bookMst;

}
