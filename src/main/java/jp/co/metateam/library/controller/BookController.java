package jp.co.metateam.library.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2
@Controller
public class BookController {
    
    private final BookMstService bookMstService;

    @Autowired
    public BookController(BookMstService bookMstService){
        this.bookMstService = bookMstService;
    }

    @GetMapping("/book/index")
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();//保存されたデータを書籍テーブルから取得。リストで受け取っている
        
        model.addAttribute("bookMstList", bookMstList);//htmlに渡すmodelはhtmlへ

        return "book/index";
    }

    @GetMapping("/book/add")//add htmlのgetからとんでくる
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }

        return "book/add";
    }
    


    @PostMapping("/book/add")
    public String registBook(@Valid @ModelAttribute BookMstDto bookMstDto, Model model, RedirectAttributes ra) {

        //modelattributeは画面とコントローラーをつなぐ
         try {
             
             boolean errtitleFlg = false;
             boolean errIsbnFlg = false;
             String titleExist = bookMstDto.getTitle();
             String IsbnExist = bookMstDto.getIsbn();
             List<String> errTitleList = new ArrayList<>();
             List<String> errIsbnList = new ArrayList<>();

             if(StringUtils.isEmpty(titleExist)){
                errTitleList.add("書籍名は必須です");
                errtitleFlg = true;
                }
             else if( titleExist != null && titleExist.length()>255){
                    errTitleList.add("書籍名は255文字以内で入力してください");
                    errtitleFlg = true;
                    }
             if(StringUtils.isEmpty(IsbnExist)){
                errIsbnList.add("ISBNは必須です");
                errIsbnFlg = true;
                }
            
             else {if(IsbnExist  != null && IsbnExist.length() != 13){
                errIsbnList.add("ISBNは13文字で入力してください");
                errIsbnFlg = true;
                }  
                if(IsbnExist != null && !IsbnExist.matches("^[0-9]+$")){
                    errIsbnList.add("ISBNの形式が不正です");
                    errIsbnFlg = true;
                    } 
                }
            
            
            BookMst selectCount = null;
             if(errIsbnList.isEmpty()){
                selectCount = this.bookMstService.selectByIsbn(bookMstDto.getIsbn());
            }

             if(selectCount != null){
                errIsbnList.add("登録済みのISBNです");
                errIsbnFlg = true;
            }
 
             if (!errTitleList.isEmpty() || !errIsbnList.isEmpty()) {
               model.addAttribute("errTitle", errTitleList);
               model.addAttribute("errIsbn", errIsbnList);
                return "book/add";
            }

            bookMstService.save(bookMstDto);

            return "redirect:/book/index";//if処理を反映して返している
         }
        catch (Exception e) {
            log.error(e.getMessage());

            ra.addFlashAttribute("bookMstDto", bookMstDto);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto");

     
        return "book/add";
        }
    } 
}
