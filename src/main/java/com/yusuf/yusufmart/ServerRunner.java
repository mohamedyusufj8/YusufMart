package com.yusuf.yusufmart;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Embedded Tomcat server runner for local development, instant testing, and grading demos.
 * Launches YusufMart on http://localhost:8080/yusufmart with full JSP and servlet support.
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

        String contextPath = "/yusufmart";
        StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath, webappDir.getAbsolutePath());
        ctx.setParentClassLoader(ServerRunner.class.getClassLoader());

        // Configure classes location for embedded runner
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        System.out.println("==================================================================");
        System.out.println("   YUSUF MART E-COMMERCE APPLICATION STARTED SUCCESSFULLY!       ");
        System.out.println("   Live Application URL: http://localhost:" + webPort + contextPath + "   ");
        System.out.println("   Health Check URL:     http://localhost:" + webPort + contextPath + "/api/v1/health ");
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
