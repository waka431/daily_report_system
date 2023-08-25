package actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import action.views.EmployeeView;
import action.views.FollowView;
import action.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.EmployeeService;
import services.FollowService;
import services.ReportService;



public class FollowAction extends ActionBase {

    private FollowService service;
    private EmployeeService employeeservice;
    private ReportService  reportservice;



    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new FollowService();
        employeeservice =new EmployeeService();
        reportservice = new ReportService();

        //メソッドを実行
        invoke();

        service.close();
        employeeservice.close();
        reportservice.close();

    }


    public void follow() throws ServletException, IOException {

            //idを条件に従業員データをフォローデーブルに登録する
        service.follow(toNumber(getRequestParam(AttributeConst.MAIN_ID)),toNumber(getRequestParam(AttributeConst.REP_EM)));

            //一覧画面にリダイレクト
        redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
    }


    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        // 以下追記

        //セッションからログイン中の従業員idを取得
        int mainID =toNumber(getRequestParam(AttributeConst.MAIN_ID));
        List<FollowView> ff =service.getFollowViewList(mainID);
        List<EmployeeView> ev=new ArrayList<EmployeeView>();

        for(FollowView f: ff) {
           EmployeeView e = employeeservice.findOne(f.getFollower());
           ev.add(e);
        }


        //ログイン中の従業員がフォローした従業員一覧を、指定されたページ数の一覧画面に表示する分取得する
        //int page = getPage();
       // List<EmployeeView> employees = service.getMinePerPage(ef, page);


        putRequestScope(AttributeConst.EMPLOYEES, ev); //取得した従業員
       // putRequestScope(AttributeConst.PAGE, page); //ページ数
        //putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数


        //一覧画面を表示
        forward(ForwardConst.FW_FOW_INDEX);
    }

    /**
     * 一覧画面を表示する
     */
    public void show() throws ServletException, IOException {

        // 以下追記

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = employeeservice.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));


        //ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<ReportView> reports = reportservice.getMinePerPage(ev, page);


        //ログイン中の従業員が作成した日報データの件数を取得
        long myReportsCount = reportservice.countAllMine(ev);

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数


        //一覧画面を表示
        forward(ForwardConst.FW_FOW_SHOW);
    }



}
