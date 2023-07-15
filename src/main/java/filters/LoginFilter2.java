package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import action.views.EmployeeView2;
import constants.AttributeConst2;
import constants.ForwardConst2;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter2 implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter2() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String contextPath = ((HttpServletRequest) request).getContextPath();
        String servletPath = ((HttpServletRequest) request).getServletPath();

        if (servletPath.matches("/css.*")) {
            // CSSフォルダ内は認証処理から除外する
            chain.doFilter(request, response);

        } else {
            HttpSession session = ((HttpServletRequest) request).getSession();

            //クエリパラメータからactionとcommandを取得
            String action = request.getParameter(ForwardConst2.ACT.getValue());
            String command = request.getParameter(ForwardConst2.CMD.getValue());

            //セッションからログインしている従業員の情報を取得
            EmployeeView2 ev = (EmployeeView2) session.getAttribute(AttributeConst2.LOGIN_EMP.getValue());

            if (ev == null) {
                //未ログイン

                if (!(ForwardConst2.ACT_AUTH.getValue().equals(action)
                        && (ForwardConst2.CMD_SHOW_LOGIN.getValue().equals(command)
                                || ForwardConst2.CMD_LOGIN.getValue().equals(command)))) {

                    //ログインページの表示またはログイン実行以外はログインページにリダイレクト
                    ((HttpServletResponse) response).sendRedirect(
                            contextPath
                                    + "?action=" + ForwardConst2.ACT_AUTH.getValue()
                                    + "&command=" + ForwardConst2.CMD_SHOW_LOGIN.getValue());
                    return;
                }
            } else {
                //ログイン済

                if (ForwardConst2.ACT_AUTH.getValue().equals(action)) {
                    //認証系Actionを行おうとしている場合

                    if (ForwardConst2.CMD_SHOW_LOGIN.getValue().equals(command)) {
                        //ログインページの表示はトップ画面にリダイレクト
                        ((HttpServletResponse) response).sendRedirect(
                                contextPath
                                        + "?action=" + ForwardConst2.ACT_TOP.getValue()
                                        + "&command=" + ForwardConst2.CMD_INDEX.getValue());
                        return;

                    } else if (ForwardConst2.CMD_LOGOUT.getValue().equals(command)) {
                        //ログアウトの実施は許可

                    } else {
                        //上記以外の認証系Actionはエラー画面

                        String forward = String.format("/WEB-INF/views/%s.jsp", "error/unknown");
                        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
                        dispatcher.forward(request, response);

                        return;

                    }
                }
            }

            //次のフィルタまたはサーブレットを呼び出し
            chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}