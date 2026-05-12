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

    @NotEmpty(message = "書籍名は必須です") //この入力値はこのルールでチェックして
    @Size(max = 255, message = "書籍名は255文字で入力してください")
    private String title; //画面入力値をまとめる箱

    @NotEmpty(message = "ISBNは必須です")
    @Size(min = 13, max = 13, message = "ISBNは13文字で入力してください")
    @Pattern(regexp = "^[0-9]+$", message = "ISBNの形式が不正です")
    private String isbn; //画面入力値をまとめる箱

    private Timestamp deletedAt;

    private BookMst bookMst;
}
