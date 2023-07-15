package action;

import java.io.IOException;

import javax.servlet.ServletException;

import action.views.EmployeeView2;
import constants.AttributeConst2;
import constants.ForwardConst2;
import constants.MassageConst2;
import constants.PropertyConst2;
import services.EmployeeService2;

/**
 * 認証に関する処理を行うActionクラス
 *
 */
public class AuthAction2 extends ActionBase2 {

    private EmployeeService2 service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService2();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * ログイン画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void showLogin() throws ServletException, IOException {

        //CSRF対策用トークンを設定
        putRequestScope(AttributeConst2.TOKEN, getTokenId());

        //セッションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
        String flush = getSessionScope(AttributeConst2.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst2.FLUSH,flush);
            removeSessionScope(AttributeConst2.FLUSH);
        }

        //ログイン画面を表示
        forward(ForwardConst2.FW_LOGIN);
    }
    /**
     * ログイン処理を行う
     * @throws ServletException
     * @throws IOException
     */
    public void login() throws ServletException, IOException {

        String code = getRequestParam(AttributeConst2.EMP_CODE);
        String plainPass = getRequestParam(AttributeConst2.EMP_PASS);
        String pepper = getContextScope(PropertyConst2.PEPPER);

        //有効な従業員か認証する
        Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

        if (isValidEmployee) {
            //認証成功の場合

            //CSRF対策 tokenのチェック
            if (checkToken()) {

                //ログインした従業員のDBデータを取得
                EmployeeView2 ev = service.findOne(code, plainPass, pepper);
                //セッションにログインした従業員を設定
                putSessionScope(AttributeConst2.LOGIN_EMP, ev);
                //セッションにログイン完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_LOGINED.getMessage());
                //トップページへリダイレクト
                redirect(ForwardConst2.ACT_TOP, ForwardConst2.CMD_INDEX);
            }
        } else {
            //認証失敗の場合

            //CSRF対策用トークンを設定
            putRequestScope(AttributeConst2.TOKEN, getTokenId());
            //認証失敗エラーメッセージ表示フラグをたてる
            putRequestScope(AttributeConst2.LOGIN_ERR, true);
            //入力された従業員コードを設定
            putRequestScope(AttributeConst2.EMP_CODE, code);

            //ログイン画面を表示
            forward(ForwardConst2.FW_LOGIN);
        }
    }
    /**
     * ログアウト処理を行う
     * @throws ServletException
     * @throws IOException
     */
    public void logout() throws ServletException, IOException {

        //セッションからログイン従業員のパラメータを削除
        removeSessionScope(AttributeConst2.LOGIN_EMP);

        //セッションにログアウト時のフラッシュメッセージを追加
        putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_LOGOUT.getMessage());

        //ログイン画面にリダイレクト
        redirect(ForwardConst2.ACT_AUTH, ForwardConst2.CMD_SHOW_LOGIN);

    }

}
