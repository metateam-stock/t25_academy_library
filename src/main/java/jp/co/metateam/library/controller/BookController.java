package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;

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
        String msg = "a";

        return "book/add";
    }

    @PostMapping("/book/add")
    public String register(@ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra) {
       try {
           boolean hasError = false;
     
           if (bookMstDto.getTitle() == null || bookMstDto.getTitle().trim().isEmpty()) {
               result.rejectValue("title", "error.title.required", "書籍名は必須です");
               hasError = true;
           }
           
           if (bookMstDto.getTitle() != null && bookMstDto.getTitle().length() > 255) {
               result.rejectValue("title", "error.title.length", "書籍名は255文字以内で入力してください");
               hasError = true;
           }
       
           if (bookMstDto.getIsbn() == null || bookMstDto.getIsbn().trim().isEmpty()) {
               result.rejectValue("isbn", "error.isbn.required", "ISBNは必須です");
               hasError = true;
           }
           
           if (bookMstDto.getIsbn() != null && !bookMstDto.getIsbn().isEmpty() && bookMstDto.getIsbn().length() != 13) {
               result.rejectValue("isbn", "error.isbn.length", "ISBNは13桁で入力してください");
               hasError = true;
           }
           
           if (bookMstDto.getIsbn() != null && !bookMstDto.getIsbn().isEmpty() && !bookMstDto.getIsbn().matches("^[0-9]+$")) {
               result.rejectValue("isbn", "error.isbn.hankaku", "ISBNは半角で入力してください");
               hasError = true;
           }
           if (bookMstService.existsByIsbn(bookMstDto.getIsbn())) {
            result.rejectValue("isbn", "error.isbn.duplicate", "このISBNは既に登録されています");
            hasError = true;
        }
           
           if (hasError) {
               throw new Exception("バリデーションエラー");
           }
           
           bookMstService.save(bookMstDto);
           return "redirect:/book/index";
       } catch (Exception e) {
           log.error("書籍登録エラー: {}", e.getMessage());
     
           ra.addFlashAttribute("bookMstDto", bookMstDto);
           ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto", result);
           return "book/add";
       }
    }
      
    
    // @PostMapping("/book/add")
    // public String add(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra, Model model) {
        
    //     boolean errTitleFlg = false;
    //     boolean errIsbnFlg = false;
    //     String isbnCheck =bookMstDto.getIsbn();
    //     String titleCheck =bookMstDto.getTitle();
    //   try{   
    //     // if (!isbn.matches("^[0-9]+$")){  
    //     //     errIsbnFlg = true;  
    //     // }
    //     if (titleCheck.length() > 5){
    //         result.rejectValue("title", "error.value", "書籍名が50文字以上です");
    //         errTitleFlg = true;
    //         // titleError = "書籍名は必須です";
    //         // errTitleFlg = true;
    //     }
    //     // if (!(isbn.startsWith("978") || isbn.startsWith("979"))) {   
    //     //     errIsbnFlg = true;
    //     // }
    //     // if (isbn.length()!=13) {
    //     //     errIsbnFlg = true;
    //     // }
    //      if (errIsbnFlg || errTitleFlg) {
    //          throw new Exception("Account already exists.");
    //     }
        
    //     bookMstService.save(bookMstDto);
    //      return "redirect:/book/index";

    //     }
    //     catch(Exception e){
    //       log.error(e.getMessage());

    //      ra.addFlashAttribute("accountDto", bookMstDto);
    //      ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto", result);
    //      return"redirect:/book/add";
    //     }
       
       
           
        
    // }
    



    
}
