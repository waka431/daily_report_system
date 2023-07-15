package action;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst2;
import constants.ForwardConst2;



/**
 * トップページに関する処理を行うActionクラス
 *
 */
public class TopAction2 extends ActionBase2 {

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        //メソッドを実行
        invoke();

    }

    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst2.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst2.FLUSH, flush);
            removeSessionScope(AttributeConst2.FLUSH);
        }
        //一覧画面を表示
        forward(ForwardConst2.FW_TOP_INDEX);
    }

}
