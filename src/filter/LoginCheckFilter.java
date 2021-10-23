package filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginCheckFilter
 */
@WebFilter("/LoginCheckFilter")
public class LoginCheckFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginCheckFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 前処理
        // セッションの取得
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        // セッションおよびログインのチェック
        if (session == null) {
          // フォワード
          ((HttpServletRequest) request).getRequestDispatcher("./").forward(request, response);
        } else {
          // ログインユーザの取得
          Map<String, String> userMap = (HashMap<String, String>) session.getAttribute("userMap");
          if (userMap == null) {
            ((HttpServletRequest) request).getRequestDispatcher("./").forward(request, response);
          }
          chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
