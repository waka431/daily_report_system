package action.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst2;
import constants.JpaConst2;
import models.Employee2;


public class EmployeeConverter2 {
    /*
    * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
    * @param ev EmployeeViewのインスタンス
    * @return Employeeのインスタンス
    */
   public static Employee2 toModel(EmployeeView2 ev) {

       return new Employee2(
               ev.getId(),
               ev.getCode(),
               ev.getName(),
               ev.getPassword(),
               ev.getAdminFlag() == null
                       ? null
                       : ev.getAdminFlag() == AttributeConst2.ROLE_ADMIN.getIntegerValue()
                               ? JpaConst2.ROLE_ADMIN
                               : JpaConst2.ROLE_GENERAL,
               ev.getCreatedAt(),
               ev.getUpdatedAt(),
               ev.getDeleteFlag() == null
                       ? null
                       : ev.getDeleteFlag() == AttributeConst2.DEL_FLAG_TRUE.getIntegerValue()
                               ? JpaConst2.EMP_DEL_TRUE
                               : JpaConst2.EMP_DEL_FALSE);
   }

   /**
    * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
    * @param e Employeeのインスタンス
    * @return EmployeeViewのインスタンス
    */
   public static EmployeeView2 toView(Employee2 e) {

       if(e == null) {
           return null;
       }

       return new EmployeeView2(
               e.getId(),
               e.getCode(),
               e.getName(),
               e.getPassword(),
               e.getAdminFlag() == null
                       ? null
                       : e.getAdminFlag() == JpaConst2.ROLE_ADMIN
                               ? AttributeConst2.ROLE_ADMIN.getIntegerValue()
                               : AttributeConst2.ROLE_GENERAL.getIntegerValue(),
               e.getCreatedAt(),
               e.getUpdatedAt(),
               e.getDeleteFlag() == null
                       ? null
                       : e.getDeleteFlag() == JpaConst2.EMP_DEL_TRUE
                               ? AttributeConst2.DEL_FLAG_TRUE.getIntegerValue()
                               : AttributeConst2.DEL_FLAG_FALSE.getIntegerValue());
   }

   /**
    * DTOモデルのリストからViewモデルのリストを作成する
    * @param list DTOモデルのリスト
    * @return Viewモデルのリスト
    */
   public static List<EmployeeView2> toViewList(List<Employee2> list) {
       List<EmployeeView2> evs = new ArrayList<>();

       for (Employee2 e : list) {
           evs.add(toView(e));
       }

       return evs;
   }

   /**
    * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
    * @param e DTOモデル(コピー先)
    * @param ev Viewモデル(コピー元)
    */
   public static void copyViewToModel(Employee2 e, EmployeeView2 ev) {
       e.setId(ev.getId());
       e.setCode(ev.getCode());
       e.setName(ev.getName());
       e.setPassword(ev.getPassword());
       e.setAdminFlag(ev.getAdminFlag());
       e.setCreatedAt(ev.getCreatedAt());
       e.setUpdatedAt(ev.getUpdatedAt());
       e.setDeleteFlag(ev.getDeleteFlag());

   }

}
