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
    public BookController(BookMstService bookMstService) {
        this.bookMstService = bookMstService;
    }

    /**
     * 書籍登録クラス
     */
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

}

@PostMapping("/book/add")
    public String addBook
    (@Valid @ModelAttribute BookMstDto book, BindingResult result, RedirectAttributes ra) {
        try{
        //入力チェックエラーがある場合

            if(result.hasErrors()){
                throw new Exception( "入力エラーがあります");
            }
            //　ISBN重複チェック
            BookMst bookExist ＝
            　　　　this.bookMstService.selectByIsbn(bookMstDto.getIsbn());

            if(bookExist != null){
                result.rejectValue("isbn", "error.value", "登録済みのISBNです"
                );
                
                throw new Exception("Book already exists.");
            }

            //保存
            this.bookMstService.save(bookMstDto);

            //成功したら一覧へ戻る

            return "redirect:/book/index";
        } catch (Exception e) {
            log.error(e.getMessage());

            //入力内容とエラーを持ったまま登録画面へ戻す
            ra.addFlashAttribute("bookMstDto", bookMstDto);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto", result

            );
            return "redirect:/book/add";
        }
   //testコメント
    }
