package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import action.views.EmployeeView;
import action.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.FollowService;



public class FollowAction extends ActionBase {

    private FollowService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new FollowService();

        //メソッドを実行
        invoke();

        service.close();
    }


    public void follow() throws ServletException, IOException {

            //idを条件に従業員データをフォローデーブルに登録する
        service.follow(toNumber(getRequestParam(AttributeConst.MAIN_ID)),toNumber(getRequestParam(AttributeConst.REP_EM)));

            //一覧画面にリダイレクト
        redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
    }


    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        // 以下追記

        //セッションからログイン中の従業員idを取得
        int fm =toNumber(getRequestParam(AttributeConst.MAIN_ID));
        List<FollowView> ff =service.getFollowViewList(fm);
        EmployeeView ef = service.converter(ff);

        //ログイン中の従業員がフォローした従業員一覧を、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<EmployeeView> employees = service.getMinePerPage(ef, page);


        putRequestScope(AttributeConst.REPORTS, employees); //取得した日報データ
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //一覧画面を表示
        forward(ForwardConst.FW_FOW_INDEX);
    }



}
