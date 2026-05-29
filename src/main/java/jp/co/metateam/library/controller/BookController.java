package jp.co.metateam.library.controller;

//import ○○で必要な機能を使えるように読み込む。道具箱から道具を取ってきて定義する。import パッケージ名.クラス名;
import java.util.List;//箱（List）を準備。複数データを扱う箱
//以下で様々な道具を読み込む
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
@Log4j2 // ログ出力機能を使えるようにする
@Controller // Springへこのクラスはコントローラー担当と伝え管理してもらう
public class BookController {// BookControllerというファイルを作成する

    private final BookMstService bookMstService;// BookMstServiceをこのControllerで使えるように保持する。final（＝変更不能）→途中で勝手に変わるのを防ぐ

    @Autowired // springにbookmstserviceを自動で渡してくださいと指示
    public BookController(BookMstService bookMstService) {// BookController作成時にBookMstServiceを受け取る
        this.bookMstService = bookMstService;// 受け取ったServiceを、このControllerの変数へ保存
    }

    @GetMapping("/book/index") // GETで /book/index にアクセスされたらこの処理を動かす
    public String index(Model model) {// 書籍を全件取得,一覧画面表示処理

        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();// serviceへ「本一覧データ取得して」と依頼

        model.addAttribute("bookMstList", bookMstList);// 画面側へ本一覧データを渡す

        return "book/index";// book/index.html を表示
    }

    @GetMapping("/book/add") // book addにアクセス時登録画面表示処理を動かす
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {// まだDTOがModelに入ってなければ
            model.addAttribute("bookMstDto", new BookMstDto());// 入力箱を画面へ渡す、空の入力データ箱を作成
        }

        return "book/add";
    }

    // 以下入力
    @PostMapping("/book/add") // コントローラー側がそのpostを受け取る。つまり保存ボタン押下→@PostMappingが動くという関係
    public String book(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra) {
        // 保存処理用メソッドを作成し、画面入力をBookMstDtoで受け取る
        // //public String book(...)。public＝外から呼び出せる。
        // string＝文字列を返す。Controllerではこの文字列が、表示する画面名になる
        // @ModelAttributeは、画面入力をDTOへ自動で詰める
        // BookMstDto=入力データを入れる箱、BookMstDto型の変数名
        // 画面入力をBookMstDtoへ自動セットして受け取り、画面名(String)を返す
        bookMstService.save(bookMstDto);// Serviceへ保存依頼
        return "redirect:/book/index";// 保存後に一覧画面へ移動

    }

}