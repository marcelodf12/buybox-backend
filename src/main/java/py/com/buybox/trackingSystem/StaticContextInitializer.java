package py.com.buybox.trackingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import py.com.buybox.trackingSystem.security.JwtUtil;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init(){
        JwtUtil.setAppConfig(appConfig);
    }

}
