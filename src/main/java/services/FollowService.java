package services;


import java.util.List;

import action.views.FollowConverter;
import action.views.FollowView;
import constants.JpaConst;
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



}
