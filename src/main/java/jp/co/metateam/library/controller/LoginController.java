package jp.co.metateam.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン関連クラス。/は最初の入り口で、/loginはログイン専用ページ
 */
@Controller//「このクラスは画面を動かす担当です」ってSpringに教える目印。Spring=「JavaでWebアプリを作るための便利な土台」
public class LoginController {//LoginControllerというクラスを作ります

    @GetMapping("/login")///login にアクセスされた時、この処理を動かします
    public String login() {//loginという処理を作ります
        return "login";//login画面を表示してください」って返してる。
    }
    
    @GetMapping("/")//トップページ（最初のURL）にアクセスされた時の処理
    public String redirectToIndex() {//redirectToIndexという処理を作ります
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//今ログインしている人の情報を取得している
        if (authentication != null && authentication.isAuthenticated()) {//ログインしている人か確認している条件文
            return "redirect:book/index";//book/index に移動してください
        }
        return "redirect:/login";//ログイン画面に飛ばしてください。/login に移動してください
    }
}