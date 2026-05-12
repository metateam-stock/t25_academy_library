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
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

//追加import
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "book/add";
    }

    //BookMstDto bookMstDtoは入力値を受け取る箱
    @PostMapping("/book/add")
    public String add(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra){
        try{
            //ISBN重複エラーがあるかどうかを管理するフラグ。最初はエラーなし。
            boolean errIsbnFlg = false;
            //入力されたISBNを使って、DBに同じISBNの書籍があるか検索
            Optional<BookMst> isbnExist = this.bookMstService.selectByIsbn(bookMstDto.getIsbn());

            //入力チェックエラーがない場合、ISBN重複チェック
            if(isbnExist.isPresent()){
                result.rejectValue("isbn", "error.value", "登録済みのISBNです");
                errIsbnFlg = true;
            }
            //必須・桁数・形式・重複のいずれかにエラーがある場合
            if(errIsbnFlg || result.hasErrors()){
                throw new Exception("Book already exisits.");
            }
            //エラーがない場合、書籍情報をDBに登録。
            bookMstService.save(bookMstDto);
            //成功時、書籍一覧画面に移動。
            return "redirect:/boo/index";
        //エラーがあった場合の処理を開始。
        } catch (Exception e){
            //入力された内容を保持、再表示
            ra.addFlashAttribute("bookMstDto", bookMstDto);
            //エラー情報を画面に渡して、エラーメッセージを表示
            ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto", result);
            //書籍登録画面に戻す
            return "redirect:/book/add";
        }
    }
        

    
}
