package jp.co.metateam.library.model;

import java.security.Timestamp;

import jakarta.validation.constraints.NotBlank; //場所でNotBlankというクラスを使う

import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.Pattern;//NotBlankが必須チェック、Sizeが文字数、Patternが半角数字

import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
public class BookMstDto {
    
    private Long id; 

    private String title;

    private String isbn;

    private Timestamp deletedAt;

    private BookMst bookMst;

}