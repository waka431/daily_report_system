package services;

import java.time.LocalDateTime;
import java.util.List;

import action.views.EmployeeConverter2;
import action.views.EmployeeView2;
import action.views.ReportConverter2;
import action.views.ReportView2;
import constants.JpaConst2;
import models.Report2;
import models.validators.ReportValidator2;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class ReportService2 extends ServiceBase2 {

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView2> getMinePerPage(EmployeeView2 employee, int page) {

        List<Report2> reports = em.createNamedQuery(JpaConst2.Q_REP_GET_ALL_MINE, Report2.class)
                .setParameter(JpaConst2.JPQL_PARM_EMPLOYEE, EmployeeConverter2.toModel(employee))
                .setFirstResult(JpaConst2.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst2.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter2.toViewList(reports);
    }

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView2 employee) {

        long count = (long) em.createNamedQuery(JpaConst2.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst2.JPQL_PARM_EMPLOYEE, EmployeeConverter2.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView2> getAllPerPage(int page) {

        List<Report2> reports = em.createNamedQuery(JpaConst2.Q_REP_GET_ALL, Report2.class)
                .setFirstResult(JpaConst2.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst2.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter2.toViewList(reports);
    }

    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst2.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;
    }

    /**
     * idを条件に取得したデータをReportViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public ReportView2 findOne(int id) {
        return ReportConverter2.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(ReportView2 rv) {
        List<String> errors = ReportValidator2.validate(rv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に、日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView2 rv) {

        //バリデーションを行う
        List<String> errors = ReportValidator2.validate(rv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Report2 findOneInternal(int id) {
        return em.find(Report2.class, id);
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(ReportView2 rv) {

        em.getTransaction().begin();
        em.persist(ReportConverter2.toModel(rv));
        em.getTransaction().commit();

    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(ReportView2 rv) {

        em.getTransaction().begin();
        Report2 r = findOneInternal(rv.getId());
        ReportConverter2.copyViewToModel(r, rv);
        em.getTransaction().commit();

    }

}