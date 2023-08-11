package action.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;

public class FollowConverter {
    public static Follow toModel( FollowView fv) {
        return new Follow(
                fv.getId(),
                fv.getMain(),
                fv.getFollower());

    }

    public static FollowView toView(Follow f) {

        if (f == null) {
            return null;
        }

        return new FollowView(
                f.getId(),
                f.getMain(),
                f.getFollower());
    }
    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<FollowView> toViewList(List<Follow> list) {
        List<FollowView> fvs = new ArrayList<>();

        for (Follow f : list) {
            fvs.add(toView(f));
        }

        return fvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Follow f, FollowView fv) {
        f.setId(fv.getId());
        f.setMain(fv.getMain());
        f.setFollower(fv.getFollower());


    }

}
