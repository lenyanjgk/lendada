   package com.lenyan.lendada.Log;

   import lombok.extern.slf4j.Slf4j;
   import org.aspectj.lang.JoinPoint;
   import org.aspectj.lang.ProceedingJoinPoint;
   import org.aspectj.lang.annotation.*;
   import org.springframework.stereotype.Component;
   import org.springframework.util.StopWatch;
   import org.springframework.web.context.request.RequestContextHolder;
   import org.springframework.web.context.request.ServletRequestAttributes;

   import javax.servlet.http.HttpServletRequest;

   @Aspect
   @Component
   @Slf4j
   public class MyLogAspect {

        @Pointcut("execution(* com.lenyan.lendada.controller.*.*(..))")
        public void pointcut() {}

//       @Pointcut("execution(* com.lenyan.lendada.service.*.*(..))")
//       public void pointcut() {}

//        @Pointcut("@annotation(com.lenyan.lendada.Log.MyLog)")
//        public void pointcut() {}

       @Before("pointcut()")
       public void doBefore(JoinPoint joinPoint) {
           log.info("开启打印日志: \n");

           ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
           HttpServletRequest request = requestAttributes.getRequest();

           log.info("=============================== 开始打印日志 =======================================");
           log.info("URL               :{}", request.getRequestURI().toString());
           log.info("HTTP Method       :{}", request.getMethod().toString());
           log.info("Class Method      :{}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
           log.info("IP                :{}", request.getRemoteAddr());
           log.info("Request Args      :{}", joinPoint.getArgs().toString());
       }

       @After("pointcut()")
       public void doAfter() {
           log.info("================================ 结束日志  ====================================");
       }

       @Around("pointcut()")
       public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
           StopWatch stopWatch = new StopWatch();
           stopWatch.start();

           Object result = joinPoint.proceed();

           log.info("响应 参数     :{}", result.toString());
           stopWatch.stop();
           long totalTimeMillis = stopWatch.getTotalTimeMillis();
           log.info("消耗 时间    :{} ms", totalTimeMillis);
           return result;
       }
   }
   