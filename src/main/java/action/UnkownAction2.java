package action;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.ForwardConst;

public class UnkownAction2 extends ActionBase {

    @Override
    public void process() throws ServletException, IOException {

        //エラー画面を表示
        forward(ForwardConst.FW_ERR_UNKNOWN);

    }

}
