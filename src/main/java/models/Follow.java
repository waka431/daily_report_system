package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = JpaConst.TABLE_FOLLOW)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_EMP_GET_FOLLOW,
            query = JpaConst.Q_EMP_GET_FOLLOW_DEF)

})
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity

public class Follow {

        /**
         * id
         */
        @Id
        @Column(name = JpaConst.FOW_COL_ID)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

      //フォローする従業員のID
        @Column(name = JpaConst. FOW_COL_EMP_ID, nullable = false)
        private Integer main;

        //フォローされている従業員
        @Column(name = JpaConst.FOW_COL_SEE_ID, nullable = false)
        private Integer follower;


}
