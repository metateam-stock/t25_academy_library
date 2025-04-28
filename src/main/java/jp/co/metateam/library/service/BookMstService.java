package jp.co.metateam.library.service;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;
import lombok.Getter;
import lombok.Setter;

@Service
public class BookMstService {

    private final BookMstRepository bookMstRepository;
    
    @Autowired
    public BookMstService(BookMstRepository bookMstRepository){
        this.bookMstRepository = bookMstRepository;
    }
    
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


//今回のポストだよ！！　バリデーション処理を書いてる
//あとでコントローラーにも同じ処理を書いて有効化する
@PostMapping("/book/add")
public boolean checkEntry(BookMstDto bookMstDto, Model model) {

    //入力されたデータを個別に変数に入れる
    //BookMustの中からタイトルはここに格納、ISBNはここに格納
    String bookTitle = bookMstDto.getTitle();
    String bookIsbn = bookMstDto.getIsbn();

    List<String> errTitleList = new ArrayList<>();
    List<String> errIsbnList = new ArrayList<>();

    if (StringUtils.isEmpty(bookTitle)) {
        errTitleList.add("*書籍名は必須です");
        model.addAttribute("errTitle",errTitleList); //controllerからHTMLテンプレートに値を渡す方法
    }else if (bookTitle.length() > 255 ){
        errTitleList.add("*書籍名は255文字以内で入力してください");
        model.addAttribute("errTitle",errTitleList);
    }
    if (StringUtils.isEmpty(bookIsbn)) {
        errIsbnList.add("*ISBNは必須です");
        model.addAttribute("errIsbn",errIsbnList);
    }else if (bookIsbn.length() > 13 ){
        errIsbnList.add("*ISBNは13桁で入力してください");
        model.addAttribute("errIsbn",errIsbnList);
    }

    // ISBNの半角数字
    if (!bookIsbn.matches("^[0-9]+$")) {
        errIsbnList.add("ISBNは半角数字で入力してください");
        model.addAttribute("errIsbn", errIsbnList);
    }
    
    if (!errTitleList.isEmpty() || !errIsbnList.isEmpty()){
        return true;

    }



    Optional<BookMst> bookMst  = this.bookMstRepository.selectByIsbn(bookIsbn);
        if (!bookMst.isEmpty()) {
            errIsbnList.add("登録されているISBNです");
            model.addAttribute("errIsbn", errIsbnList);
            return true;}
        return false;
}

    




    @Transactional
    public void save(BookMstDto bookMstDto) {
        try {
            
            BookMst book = new BookMst();

            book.setTitle(bookMstDto.getTitle());
            book.setIsbn(bookMstDto.getIsbn());
            


            // データベースへの保存
            this.bookMstRepository.save(book);
        } catch (Exception e) {
            throw e;
        }
    }
    
}



