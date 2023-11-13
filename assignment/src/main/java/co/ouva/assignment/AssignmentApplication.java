package co.ouva.assignment;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@ComponentScan(value = {"co"})
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AssignmentApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    public static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store"))
                .map((key) -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(
                env.getProperty("server.servlet.context-path")).filter(
                StringUtils::isNotBlank).orElse("/");
        String hostAddress = "localhost";

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var6) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        log.info(
                "Application '{}' is running! Access URLs: Local: {}://localhost:{}{} External: {}://{}:{}{} Profile(s): {}",
                env.getProperty("spring.application.name"), protocol, serverPort, contextPath,
                protocol, hostAddress, serverPort, contextPath, env.getActiveProfiles());
    }

}
