package action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import action.views.EmployeeView2;
import constants.AttributeConst2;
import constants.ForwardConst2;
import constants.JpaConst2;
import constants.MassageConst2;
import constants.PropertyConst2;
import services.EmployeeService2;

public class EmployeeAction2 extends ActionBase2 {
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
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) { //追記

            //指定されたページ数の一覧画面に表示するデータを取得
            int page = getPage();
            List<EmployeeView2> employees = service.getPerPage(page);

            //全ての従業員データの件数を取得
            long employeeCount = service.countAll();

            putRequestScope(AttributeConst2.EMPLOYEES, employees); //取得した従業員データ
            putRequestScope(AttributeConst2.EMP_COUNT, employeeCount); //全ての従業員データの件数
            putRequestScope(AttributeConst2.PAGE, page); //ページ数
            putRequestScope(AttributeConst2.MAX_ROW, JpaConst2.ROW_PER_PAGE); //1ページに表示するレコードの数

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst2.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst2.FLUSH, flush);
                removeSessionScope(AttributeConst2.FLUSH);
            }

            //一覧画面を表示
            forward(ForwardConst2.FW_EMP_INDEX);

        } //追記

    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) { //追記
            putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst2.EMPLOYEE, new EmployeeView2()); //空の従業員インスタンス

            //新規登録画面を表示
            forward(ForwardConst2.FW_EMP_NEW);
        } //追記
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) { //追記

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView2 ev = new EmployeeView2(
                    null,
                    getRequestParam(AttributeConst2.EMP_CODE),
                    getRequestParam(AttributeConst2.EMP_NAME),
                    getRequestParam(AttributeConst2.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst2.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst2.DEL_FLAG_FALSE.getIntegerValue());

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst2.PEPPER);

            //従業員情報登録
            List<String> errors = service.create(ev, pepper);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst2.EMPLOYEE, ev); //入力された従業員情報
                putRequestScope(AttributeConst2.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst2.FW_EMP_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst2.ACT_EMP, ForwardConst2.CMD_INDEX);
            }

        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) { //追記

            //idを条件に従業員データを取得する
            EmployeeView2 ev = service.findOne(toNumber(getRequestParam(AttributeConst2.EMP_ID)));

            if (ev == null || ev.getDeleteFlag() == AttributeConst2.DEL_FLAG_TRUE.getIntegerValue()) {

                //データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst2.FW_ERR_UNKNOWN);
                return;
            }

            putRequestScope(AttributeConst2.EMPLOYEE, ev); //取得した従業員情報

            //詳細画面を表示
            forward(ForwardConst2.FW_EMP_SHOW);
        } //追記

    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) { //追記

            //idを条件に従業員データを取得する
            EmployeeView2 ev = service.findOne(toNumber(getRequestParam(AttributeConst2.EMP_ID)));

            if (ev == null || ev.getDeleteFlag() == AttributeConst2.DEL_FLAG_TRUE.getIntegerValue()) {

                //データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst2.FW_ERR_UNKNOWN);
                return;
            }

            putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst2.EMPLOYEE, ev); //取得した従業員情報

            //編集画面を表示する
            forward(ForwardConst2.FW_EMP_EDIT);

        } //追記
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) { //追記
            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView2 ev = new EmployeeView2(
                    toNumber(getRequestParam(AttributeConst2.EMP_ID)),
                    getRequestParam(AttributeConst2.EMP_CODE),
                    getRequestParam(AttributeConst2.EMP_NAME),
                    getRequestParam(AttributeConst2.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst2.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst2.DEL_FLAG_FALSE.getIntegerValue());

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst2.PEPPER);

            //従業員情報更新
            List<String> errors = service.update(ev, pepper);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst2.EMPLOYEE, ev); //入力された従業員情報
                putRequestScope(AttributeConst2.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst2.FW_EMP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst2.ACT_EMP, ForwardConst2.CMD_INDEX);
            }
        }
    }

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) { //追記

            //idを条件に従業員データを論理削除する
            service.destroy(toNumber(getRequestParam(AttributeConst2.EMP_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst2.ACT_EMP, ForwardConst2.CMD_INDEX);
        }
    }

    /**
     * ログイン中の従業員が管理者かどうかチェックし、管理者でなければエラー画面を表示
     * true: 管理者 false: 管理者ではない
     * @throws ServletException
     * @throws IOException
     */
    private boolean checkAdmin() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView2 ev = (EmployeeView2) getSessionScope(AttributeConst2.LOGIN_EMP);

        //管理者でなければエラー画面を表示
        if (ev.getAdminFlag() != AttributeConst2.ROLE_ADMIN.getIntegerValue()) {

            forward(ForwardConst2.FW_ERR_UNKNOWN);
            return false;

        } else {

            return true;
        }

    }

}

