package com.yusuf.yusufmart;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Embedded Tomcat server runner for local development, instant testing, and grading demos.
 * Mounts both root ("") and "/yusufmart" contexts for seamless Cloudflare Tunnel integration.
 */
public class ServerRunner {

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "src/main/webapp";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
            webappDir = new File(".", "src/main/webapp");
        }

        Tomcat tomcat = new Tomcat();

        File baseDir = new File("target/tomcat");
        baseDir.mkdirs();
        tomcat.setBaseDir(baseDir.getAbsolutePath());

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.parseInt(webPort));
        tomcat.getConnector(); // Trigger the creation of default connector

        // Mount at both root ("") and "/yusufmart" so Cloudflare and localhost links work directly
        StandardContext ctxRoot = (StandardContext) tomcat.addWebapp("", webappDir.getAbsolutePath());
        ctxRoot.setParentClassLoader(ServerRunner.class.getClassLoader());

        StandardContext ctxYm = (StandardContext) tomcat.addWebapp("/yusufmart", webappDir.getAbsolutePath());
        ctxYm.setParentClassLoader(ServerRunner.class.getClassLoader());

        // Configure classes location for embedded runner
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resourcesRoot = new StandardRoot(ctxRoot);
            resourcesRoot.addPreResources(new DirResourceSet(resourcesRoot, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctxRoot.setResources(resourcesRoot);

            WebResourceRoot resourcesYm = new StandardRoot(ctxYm);
            resourcesYm.addPreResources(new DirResourceSet(resourcesYm, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctxYm.setResources(resourcesYm);
        }

        System.out.println("==================================================================");
        System.out.println("   YUSUF MART E-COMMERCE APPLICATION STARTED SUCCESSFULLY!       ");
        System.out.println("   Local URL:        http://localhost:" + webPort + "/yusufmart   ");
        System.out.println("   Root URL:         http://localhost:" + webPort + "/            ");
        System.out.println("   Health Check API: http://localhost:" + webPort + "/api/v1/health");
        System.out.println("   H2 DB Console:    http://localhost:" + webPort + "/h2-console  ");
        System.out.println("==================================================================");
        System.out.println("   Default Accounts:");
        System.out.println("     - Admin:  admin@yusufmart.com  / Admin@123");
        System.out.println("     - Seller: seller@yusufmart.com / Seller@123");
        System.out.println("     - Buyer:  buyer@yusufmart.com  / Buyer@123");
        System.out.println("==================================================================");

        tomcat.start();
        tomcat.getServer().await();
    }
}
