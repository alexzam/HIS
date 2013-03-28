package az.his.dsmanager;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSchemaManagerFilter implements Filter {
    private String dbVersionStr;
    private int dbVersion;
    private int targetVersion;

    boolean autoUpgradeMode = false;
    private DataSchemaManager dataSchemaManager;

    private static final Logger log = Logger.getLogger(DataSchemaManagerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String jndiName = filterConfig.getInitParameter("datasource");

        try {
            log.info("Checking DB schema...");

            dataSchemaManager = new DataSchemaManager(jndiName);
            dbVersionStr = dataSchemaManager.getDBVersion();
            dbVersion = dataSchemaManager.getDBVersionId();
            targetVersion = Integer.parseInt(filterConfig.getInitParameter("target-version"));

            log.info("Found version " + dbVersionStr + " (" + dbVersion + ")");
            log.info("Target version is " + targetVersion);

            if (targetVersion > dbVersion) {
                log.debug("Reading config...");
                InputStream stream = getClass().getClassLoader().getResourceAsStream("db/migration.xml");

                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(stream);
                stream.close();

                NodeList configList = document.getElementsByTagName("config");
                if (configList.getLength() > 1) throw new StreamCorruptedException("Migration file is corrupted.");

                if (configList.getLength() > 0) {
                    NodeList configNodes = configList.item(0).getChildNodes();
                    for (int i = 0; i < configNodes.getLength(); i++) {
                        Node item = configNodes.item(i);
                        if (item.getNodeName().equals("auto-upgrade")) {
                            log.debug("Auto-upgrade mode on");
                            autoUpgradeMode = true;
                        }
                    }
                }

                NodeList versionList = document.getElementsByTagName("version");

                List<Version> versions = new ArrayList<Version>();

                for (int i = 0; i < versionList.getLength(); i++) {
                    Node item = versionList.item(i);
                    NamedNodeMap attributes = item.getAttributes();

                    int verId = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                    if (verId <= dbVersion || verId > targetVersion) {
                        log.debug("Version " + verId + " is NOT needed.");
                        continue;
                    }
                    log.debug("Version " + verId + " is needed.");

                    versions.add(new Version(attributes.getNamedItem("name").getNodeValue(),
                            verId,
                            attributes.getNamedItem("upscript").getNodeValue()));
                }

                Collections.sort(versions);

                if (autoUpgradeMode) {
                    log.info("Starting auto-upgrade");
                    dataSchemaManager.upgrade(versions);
                    dbVersionStr = dataSchemaManager.getDBVersion();
                    dbVersion = dataSchemaManager.getDBVersionId();
                }
            }
        } catch (Exception e) {
            log.error("Init error", e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (targetVersion > dbVersion) {
            PrintWriter writer = servletResponse.getWriter();
            writer.println("<html><body><h1>");
            writer.println("Incorrect DB version.");
            writer.println("</h1><h2>");
            writer.print("Detected " + dbVersionStr + " (" + dbVersion + ") but expected " + targetVersion);
            writer.println("</h2></body></html>");
            writer.close();

            return;
        }
        servletRequest.setAttribute("dbVersion", dbVersionStr);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
