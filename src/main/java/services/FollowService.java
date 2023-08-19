package services;


import java.util.List;

import action.views.EmployeeConverter;
import action.views.EmployeeView;
import action.views.FollowConverter;
import action.views.FollowView;
import constants.JpaConst;
import models.Employee;
import models.Follow;

public class FollowService extends ServiceBase{
    /**
     * 登録処理
     * @param loginId リクエストパラメタから取得したログインした人の従業員ID
     * @param followId リクエストパラメタから取得したフォローされる人の従業員ID
     */
    public void follow(Integer loginId, Integer followId) {

      FollowView fw = new FollowView(
        null,
        loginId,
        followId
      );

      EmployeeService es = new EmployeeService();
      em.getTransaction().begin();
      em.persist(FollowConverter.toModel(fw));
      em.getTransaction().commit();
    }

    //ログインしている従業員を元にフォローされている従業員を取得
    public List<FollowView> getFollowViewList(int id) {
            List<Follow> follows = em.createNamedQuery(JpaConst.Q_EMP_GET_FOLLOW, Follow.class)
                    .setParameter(JpaConst.JPQL_PARM_ID, id)
                    .getResultList();

            return FollowConverter.toViewList(follows);
        }


//フォローされている従業員のを元に従業員情報を取得したい
    public List<EmployeeView> converter(FollowView ff){
        List<Employee> followers = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL_F, Employee.class)
                                        .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, ff)
                                       .getResultList();
        return EmployeeConverter.toViewList(followers);
            }
    /**
     * 指定した従業員がフォローした従業員データを、指定されたページ数の一覧画面に表示する分取得しEmployeeViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<EmployeeView> getMinePerPage(EmployeeView ef, int page) {

        List<Employee> followers = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL_F, Employee.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ef))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return EmployeeConverter.toViewList(followers);
    }

    /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public EmployeeView findOne(int id) {
        Employee e = findOneInternal(id);
        return EmployeeConverter.toView(e);
    }


    /**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Employee findOneInternal(int id) {
        Employee e = em.find(Employee.class, id);

        return e;
    }

    


}
