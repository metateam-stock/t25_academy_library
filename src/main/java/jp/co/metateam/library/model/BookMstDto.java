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
    
    
    private Long id; //これいらないんじゃないか（Dtoは画面から受け取った情報を受け取るから、サーバ側で自動採番するならいらないんじゃないか）
    
    @NotEmpty(message="ISBNを入力してください")
    @Size(max=13, message = "ISBNは13字以内で入力してください")
    @Pattern(regexp = "^[0-9]+$", message = "半角数字で入力してください")
    private String isbn;

    @NotEmpty(message ="書籍名は必須です" )
    @Size(max = 255, message = "書籍名は255字以下で入力してください")
    private String title;
    
    private Timestamp deletedAt;//これいらないんじゃないか（画面でこれ入力しないよね）

    private BookMst bookMst;//これいらないんじゃないか（これはなに）
}
