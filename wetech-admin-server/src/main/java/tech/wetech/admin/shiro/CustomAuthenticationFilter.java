package tech.wetech.admin.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import tech.wetech.admin.model.Result;
import tech.wetech.admin.model.enumeration.CommonResultStatus;
import tech.wetech.admin.utils.JsonUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义登录过滤器返回json，默认是返回视图
 *
 * @author cjbi
 */
@Slf4j
public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 登录成功
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        if(log.isDebugEnabled()) {
            log.debug("用户{}登录成功", token.getPrincipal());
        }
        return true;
    }

    /**
     * 登录失败
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if(log.isDebugEnabled()) {
            log.debug("用户{}登录失败", token.getPrincipal(), e);
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //return false 拦截， true 放行
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                return true;
            }
        } else {
            HttpServletResponse res = (HttpServletResponse) response;
            PrintWriter out = response.getWriter();
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            Result failureResult = Result.failure(CommonResultStatus.LOGIN_ERROR, "请先登录再操作");
            JsonUtil json = JsonUtil.getInstance();
            out.println(json.obj2json(failureResult));
            return false;
        }


    }
}
