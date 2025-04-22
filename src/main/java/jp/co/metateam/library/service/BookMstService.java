package jp.co.metateam.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;

@Service
public class BookMstService {

    private final BookMstRepository bookMstRepository;
    
    @Autowired
    public BookMstService(BookMstRepository bookMstRepository){
        this.bookMstRepository = bookMstRepository;
    }
    //public Account selectIsbn(String isbn) {
      //  return this.bookMstRepository.selectByisbn(isbn);
    //}

    
    public List<BookMstDto> findAvailableWithStockCount() {
        List<BookMst> books = this.bookMstRepository.findLimitedBook();
        List<BookMstDto> bookMstDtoList = new ArrayList<BookMstDto>();

        // 書籍の在庫数を取得
        // FIXME: 現状は書籍ID毎にDBに問い合わせている。一度のSQLで完了させたい。
        for (int i = 0; i < books.size(); i++) {
            BookMst book = books.get(i);
            BookMstDto bookMstDto = new BookMstDto();
            bookMstDto.setId(book.getId());
            bookMstDto.setIsbn(book.getIsbn());
            bookMstDto.setTitle(book.getTitle());
            bookMstDtoList.add(bookMstDto);
        }

        return bookMstDtoList;
    }
    public boolean checkEntry(BookMstDto bookMstDto, Model model) {
        String bookIsbn = bookMstDto.getIsbn();
        String bookTitle = bookMstDto.getTitle();
        List<String> errTitleList = new ArrayList<>();
        List<String> errIsbnList = new ArrayList<>();
        boolean hasError = false;

        if (StringUtils.isEmpty(bookTitle)) {
            errTitleList.add("書籍名は必須です");
            hasError = true;
        }
    
        
        if (StringUtils.isEmpty(bookIsbn) || !bookIsbn.matches("\\d{13}")) {
            errIsbnList.add("ISBNは13桁の数字で入力してください");
            hasError = true;
        }
    
        
        if (hasError) {
            if (!errTitleList.isEmpty()) {
                model.addAttribute("errTitle", errTitleList);
            }
            if (!errIsbnList.isEmpty()) {
                model.addAttribute("errIsbn", errIsbnList);
            }
        }
    
        return !hasError;
    }
    
    public boolean existsByIsbn(String isbn) {
        return bookMstRepository.existsByIsbn(isbn);
    }
    
    @Transactional
    public void save(BookMstDto BookMstDto) {
        try {
            // AccountDtoからAccountへの変換
            BookMst BookMst = new BookMst();

            BookMst.setTitle(BookMstDto.getTitle());
            BookMst.setIsbn(BookMstDto.getIsbn());
            

            // データベースへの保存
            this.bookMstRepository.save(BookMst);
        } catch (Exception e) {
            throw e;
        }
    }
    
}



