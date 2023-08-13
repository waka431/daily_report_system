package services;


import action.views.EmployeeView;
import action.views.FollowView;
import constants.JpaConst;

class FollowService extends ServiceBase{
    /**
     * idを条件に従業員データを論理削除する
     * @param id
     */
    public void follow(Integer id) {

       EmployeeService es = new EmployeeService();
        //idを条件に登録済みの従業員情報を取得する
        EmployeeView savedEmp =es.findOne(id);

        FollowView fw =new FollowView();

        fw.setMain(JpaConst.EMP_COL_ID);
        fw.setFollower(JpaConst.REP_COL_EMP);



    }
}
