package com.capstoneproject.ElitesTracker.security.fiftyOneDegrees;

import fiftyone.devicedetection.shared.DeviceData;
import fiftyone.pipeline.core.data.FlowData;
import fiftyone.pipeline.web.Constants;
import fiftyone.pipeline.web.services.FlowDataProviderCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static fiftyone.common.testhelpers.LogbackHelper.configureLogback;
import static fiftyone.pipeline.util.FileFinder.getFilePath;

public class GettingStartedWebCloud extends HttpServlet {

    private static final long serialVersionUID = 1734154705981153540L;
    public static String resourceBase = "web/getting-started.cloud/src/main/webapp";
    public static Logger logger = LoggerFactory.getLogger(GettingStartedWebCloud.class);
    public static void main(String[] args) throws Exception {
        configureLogback(getFilePath("logback.xml"));

        String resourceKey = args.length > 0 ? args[0] : null;
        logger.info("Running Example {}", GettingStartedWebCloud.class);
        // check resource key and set as System Property
//        getOrSetTestResourceKey(resourceKey);
        // start Jetty with this WebApp
//        EmbedJetty.runWebApp(resourceBase, 8081);
    }
    FlowDataProviderCore flowDataProvider = new FlowDataProviderCore.Default();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // the detection has already been carried out by the Filter
        // which is responsible to the lifecycle of the flowData - do NOT dispose
        FlowData flowData = flowDataProvider.getFlowData(request);
        // retrieve the device data from the flowdata
        DeviceData device = flowData.get(DeviceData.class);
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            doHtmlPreamble(out, "Web Integration Cloud Example");
//            // request main 51Degrees Client Side Script - this is automatically
//            // served by inclusion of the PipelineFilter which intercepts the request
//            // and serves dynamically generated JavaScript
//            out.println("<script src=\"" + Constants.CORE_JS_NAME+ "\"></script>");
//            // include description of example
//            doStaticText(out, resourceBase + "/WEB-INF/html/example-description.html");
//            // include a script to display the results of the client side detection
//            doStaticText(out, resourceBase + "/WEB-INF/html/client-side-js-include.html");
//            doDeviceData(out, device, flowData, null);
//            doStaticText(out, resourceBase + "/WEB-INF/html/apple-detection.html");
//            doEvidence(out, request, flowData);
//            doResponseHeaders(out, response);
//            doHtmlPostamble(out);
//        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // any failure causes 500 error
        try {
            processRequest(request, response);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}
