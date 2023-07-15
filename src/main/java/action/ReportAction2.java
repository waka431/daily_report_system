package action;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import action.views.EmployeeView2;
import action.views.ReportView2;
import constants.AttributeConst2;
import constants.ForwardConst2;
import constants.JpaConst2;
import constants.MassageConst2;
import services.ReportService2;


/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class ReportAction2 extends ActionBase2 {

    private ReportService2 service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService2();

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

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView2> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        long reportsCount = service.countAll();

        putRequestScope(AttributeConst2.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst2.REP_COUNT, reportsCount); //全ての日報データの件数
        putRequestScope(AttributeConst2.PAGE, page); //ページ数
        putRequestScope(AttributeConst2.MAX_ROW, JpaConst2.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst2.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst2.FLUSH, flush);
            removeSessionScope(AttributeConst2.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst2.FW_REP_INDEX);
    }
    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView2 rv = new ReportView2();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst2.REPORT, rv); //日付のみ設定済みの日報インスタンス

        //新規登録画面を表示
        forward(ForwardConst2.FW_REP_NEW);

    }
    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst2.REP_DATE) == null
                    || getRequestParam(AttributeConst2.REP_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst2.REP_DATE));
            }

            //セッションからログイン中の従業員情報を取得
            EmployeeView2 ev = (EmployeeView2) getSessionScope(AttributeConst2.LOGIN_EMP);

            //パラメータの値をもとに日報情報のインスタンスを作成する
            ReportView2 rv = new ReportView2(
                    null,
                    ev, //ログインしている従業員を、日報作成者として登録する
                    day,
                    getRequestParam(AttributeConst2.REP_TITLE),
                    getRequestParam(AttributeConst2.REP_CONTENT),
                    null,
                    null);

            //日報情報登録
            List<String> errors = service.create(rv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst2.REPORT, rv);//入力された日報情報
                putRequestScope(AttributeConst2.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst2.FW_REP_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst2.ACT_REP, ForwardConst2.CMD_INDEX);
            }
        }
    }
    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に日報データを取得する
        ReportView2 rv = service.findOne(toNumber(getRequestParam(AttributeConst2.REP_ID)));

        if (rv == null) {
            //該当の日報データが存在しない場合はエラー画面を表示
            forward(ForwardConst2.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst2.REPORT, rv); //取得した日報データ

            //詳細画面を表示
            forward(ForwardConst2.FW_REP_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に日報データを取得する
        ReportView2 rv = service.findOne(toNumber(getRequestParam(AttributeConst2.REP_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView2 ev = (EmployeeView2) getSessionScope(AttributeConst2.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {
            //該当の日報データが存在しない、または
            //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
            forward(ForwardConst2.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst2.REPORT, rv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst2.FW_REP_EDIT);
        }

    }
    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に日報データを取得する
            ReportView2 rv = service.findOne(toNumber(getRequestParam(AttributeConst2.REP_ID)));

            //入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst2.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst2.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst2.REP_CONTENT));

            //日報データを更新する
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst2.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst2.REPORT, rv); //入力された日報情報
                putRequestScope(AttributeConst2.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst2.FW_REP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst2.FLUSH, MassageConst2.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst2.ACT_REP, ForwardConst2.CMD_INDEX);

            }
        }
    }


}