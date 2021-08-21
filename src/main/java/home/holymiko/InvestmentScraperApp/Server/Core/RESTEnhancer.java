package home.holymiko.InvestmentScraperApp.Server.Core;

import home.holymiko.InvestmentScraperApp.Server.Core.Annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Core.Exception.ResourceNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Optional;

@Configuration
@Aspect
public class RESTEnhancer {

    // Where the annotation can be used
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object resultCheck(ProceedingJoinPoint call) throws Throwable {
        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();

        Object result = null;
        result = call.proceed();

        if (result == null || result == Optional.empty()) {
            ResourceNotFound annotation = method.getAnnotation(ResourceNotFound.class);
            if (annotation != null) {
                throw new ResourceNotFoundException(annotation.message());
            }
        }

        return result;
    }
}
