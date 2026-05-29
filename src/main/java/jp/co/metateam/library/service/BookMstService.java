package jp.co.metateam.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.common.util.StringUtils;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;

@Service
public class BookMstService {

    private final BookMstRepository bookMstRepository;

    @Autowired
    public BookMstService(BookMstRepository bookMstRepository) {
        this.bookMstRepository = bookMstRepository;
    }

    public List<BookMstDto> findAvailableWithStockCount() {
        List<BookMst> books = this.bookMstRepository.findLimitedBook();
        List<BookMstDto> bookMstDtoList = new ArrayList<BookMstDto>();
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

    // 以下入力
    @Transactional // この処理を1セットのDB処理として扱う。途中でエラーなら保存をすべてなかったことに（dbへは最後の最後の確定した段階で保存したい）
    public void save(BookMstDto bookMstDto) { // puplicでコントローラーから呼べる。voidで戻り値なし、今回は保存のみなので画面へ帰す値は不要
        // 以下dto型からdbへの変換
        BookMst book = new BookMst();// Controllerから受け取った画面入力データ（titleとisbn）
        book.setTitle(bookMstDto.getTitle());// 画面入力をDB保存用Entityへ詰め替える
        book.setIsbn(bookMstDto.getIsbn());// 画面入力をDB保存用Entityへ詰め替える。dbへの保存
        this.bookMstRepository.save(book);// Repositoryへ保存依頼(JpaRepository継承)
    }
}