package servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthFilterServlet
 */
@WebFilter("/AuthFilterServlet")
public class AuthFilterServlet implements Filter {

    /**
     * Default constructor.
     */
    public AuthFilterServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * 各サーブレット(「LoginPage」このサーブレとはないな？？？？？？？サーブレット以外の)呼び出しに対して設定するにフィルタを設定し、フィルタの中でユーザー認証が行われているかどうかを確認するようにします
     * フィルタではセッションが開始されているかどうか、
     * そしてセッション変数の「login」が設定されているかどうかをチェックして認証されているかどうかを確認します。
     * また作成したフィルタを「LoginPage」サーブレット以外の呼び出しに対して設定する ???このサーブレットはないな最初は login.jsp???
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     *
     *
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {

            String target = ((HttpServletRequest) request).getRequestURI();

            HttpSession session = ((HttpServletRequest) request).getSession();

            if (session == null) {
                session = ((HttpServletRequest) request).getSession(true);

                ((HttpServletResponse) response).sendRedirect("/schedule/LoginPage");
            } else {
                Object loginCheck = session.getAttribute("login");
                if (loginCheck == null) {
                    ((HttpServletResponse) response).sendRedirect("/schedule/LoginPage");
                }
            }

            chain.doFilter(request, response);
        } catch (ServletException se) {
        } catch (IOException e) {
        }

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
