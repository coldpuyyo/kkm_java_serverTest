package com.psy7758.context;

import javax.servlet.ServletContext;

/*
 * 서블릿이 아닌 클래스(개별 DAO 클래스)에서도 ServletContext 를 이용하여 초기 파라미터를 심고 읽을 수
 * 있도록 활용하기 위해, 아래와같이 유사 싱글턴 패턴 형식으로 정의.
 * 이를 애플리케이션의 초기 환경 설정을 위한 ServletContextListener 클래스(ContextParamInitializer)
 * 에서 contextInitialized 메서드에 의해 수신되는 ServletContextEvent 파라미터 객체를 통해 ServletContext
 * 의 참조를 얻고, 해당 참조를 아래 ServletContextHolder 클래스에 저장함으로써, 서블릿이 아닌 클래스에서도
 * ServletContextHolder 를 이용하여 ServletContext 의 참조를 얻어 초기 파라미터를 심고 얻을 수 있도록 설정.
 */
public class ServletContextHolder {
   private static ServletContext context;
   
   private ServletContextHolder() {}
   
   public static void setServletContext(ServletContext servletContext) {
      context = servletContext;
   }
   
   public static ServletContext getServletContext() {
      return context;
   }
}