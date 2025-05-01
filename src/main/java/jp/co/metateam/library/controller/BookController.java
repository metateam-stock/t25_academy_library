package jp.co.metateam.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.hibernate.collection.spi.PersistentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
import jp.co.metateam.library.repository.BookMstRepository;
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
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();
       
        model.addAttribute("bookMstList", bookMstList);
 
        return "book/index";
        }
 
    @GetMapping("/book/add")
    public String add(Model model) {
       
       
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }
        return"book/add";
    }
        @PostMapping("book/add")
        public String addbook(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra,Model model) {
            String title = bookMstDto.getTitle();
            String isbn = bookMstDto.getIsbn();
     
                boolean errIsbncharatype = false;
                boolean errTitleFlg = false;
                boolean errIsbnFlg = false;
                boolean errIsbnnullFlg  = false;
                boolean errIsbncharaCount = false;
       
            List<String> errTitleList = new ArrayList<>();
            List<String> errisbnList = new ArrayList<>();
       List<String> errIsbnList = new ArrayList<>();
           
         // 書籍名のバリデーション
            if (bookMstDto.getTitle() == null || bookMstDto.getTitle().trim().isEmpty()) {
                errTitleList.add("書籍名は必須です。");
                result.rejectValue("title", "error.required", "書籍名は必須です");
                errTitleFlg = true;
            }
         
         // 書籍名が256文字以上の場合、エラーフラグを立てる           
            if (bookMstDto.getTitle().length() > 256) {
                errTitleList.add("書籍名は256文字以内で入力してください");
                result.rejectValue("title", "error.maxlength", "書籍名は256文字以内で入力してください");
                errTitleFlg = true;
            }
         // ISBNのバリデーション
            if (bookMstDto.getIsbn() == null || bookMstDto.getIsbn().trim().isEmpty()) {
                errIsbnList.add("ISBNは必須です");
                result.rejectValue("isbn", "error.required", "ISBNは必須です");
                errIsbnFlg = true;
            }
         
         // ISBNが13文字以外の場合、エラーフラグを立てる
            if (bookMstDto.getIsbn().length() != 13) {
                errIsbnList.add("ISBNは13桁で入力してください");
                result.rejectValue("isbn", "error.length", "ISBNは13桁で入力してください");
                errIsbnFlg = true;
            }
         
         // ISBNが数字のみで構成されていることを確認する正規表現
            if (!bookMstDto.getIsbn().matches("[0-9]+")) {
                errIsbnList.add("ISBNは半角数字で入力してください");
                result.rejectValue("isbn", "error.format", "ISBNは半角数字で入力してください");
                errIsbnFlg = true;
        
            }
         
            
         
         // エラーがあれば、エラーメッセージリストをフラッシュ属性に渡す
            if(errIsbnFlg || errIsbnnullFlg || errIsbncharaCount || errIsbncharatype){
                model.addAttribute( "errtitle",errTitleList);
                // model.addAttribute( "errisbn",errIsbnList);
              return"book/add";
             }
             

            // もしISBNがすでに存在している場合、エラーを返す
             if (bookMstService.isbnDuplicateCheck(isbn)) {
                 model.addAttribute("errisbn", "登録済みのISBNです");
                 return "book/add"; // 入力画面に戻る
             }
                
            
         
            // バリデーションにエラーがなければ、書籍データを保存
                 if (result.hasErrors()) {
                    return "book/add";
                 }
           
        bookMstService.save(bookMstDto);
         
            // 登録成功メッセージを設定して、一覧画面にリダイレクト
           model.addAttribute("message", "書籍が正常に登録されました");
            return "redirect:/book/index";    
        }
}