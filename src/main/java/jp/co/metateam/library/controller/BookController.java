package jp.co.metateam.library.controller;

import java.util.ArrayList;
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

    @PostMapping("/book/add")
    public String save(@ModelAttribute("bookMstDto") BookMstDto bookMstDto, Model model) {
        List<String> errorList = new ArrayList<>();

        // 書籍名必須チェック
        if (bookMstDto.getTitle() == null || bookMstDto.getTitle().isBlank()) {
            errorList.add("書籍名は必須です。");
        }

        // 書籍名が100文字以下
        if (bookMstDto.getTitle() != null && bookMstDto.getTitle().length() > 100) {
            errorList.add("書籍名は100文字以下で入力してください。");
        }

        // ISBNが空欄ではない
        if (bookMstDto.getIsbn() == null || bookMstDto.getIsbn().isBlank()) {
            errorList.add("ISBNは必須です。");
        } else {

            // ISBNが13桁
            if (bookMstDto.getIsbn().length() != 13) {
                errorList.add("ISBNは13桁で入力してください。");
            }

            // ISBNが半角数字で構成されている
            if (!bookMstDto.getIsbn().matches("^[0-9]+$")) {
                errorList.add("ISBNの形式が不正です。");
            }

            // 重複チェック
            // if (bookMstService.existsByIsbn(bookMstDto.getIsbn())){
            // errorList.add("入力されたISBNは既に登録されています。");
            // }
        }
        // エラーがある場合
        if (!errorList.isEmpty()) {
            model.addAttribute("errorList", errorList);
            model.addAttribute("bookMstDto", bookMstDto);
            return "book/add";
        }

        // 書籍データ登録
        bookMstService.save(bookMstDto);

        // 一覧画面へ戻る
        return "redirect:/book/index";

    }
}
