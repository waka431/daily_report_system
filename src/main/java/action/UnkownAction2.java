package action;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.ForwardConst2;

public class UnkownAction2 extends ActionBase2 {

    @Override
    public void process() throws ServletException, IOException {

        //エラー画面を表示
        forward(ForwardConst2.FW_ERR_UNKNOWN);

    }

}
