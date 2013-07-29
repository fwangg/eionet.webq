<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

 <!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="-1" />
        <meta name="Publisher" content="EEA, The European Environment Agency" />

        <title>Web Questionnaires</title>

        <link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
        <link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />
        <link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
    </head>
    <body>
        <h1>Web Questionnaires</h1>
        <c:if test="${not empty message}">
             <div id="message" class="success">${message}</div>
        </c:if>
        <form action="uploadXml" method="POST" enctype="multipart/form-data">
            <fieldset>
                <legend>Upload XML file</legend>

                <p>
                    <label for="fileData">File</label><br/>
                    <input id="uploadedXmlFile" type="file" name="uploadedXmlFile"/>
                </p>

                <p>
                    <input type="submit" />
                </p>

            </fieldset>
        </form>
        <fieldset>
            <legend>Uploaded XMLs</legend>
            <c:forEach items="${uploadedFiles}" var="fileName">
                <form action="<c:url value="/download"/>">
                    ${fileName}
                    <input type="button" value="Edit with webform"/>
                    <input type="hidden" name="fileName" value="${fileName}"/>
                    <input type="submit" value="Download ${fileName}"/>
                </form>
            </c:forEach>
        </fieldset>
        <footer></footer>
    </body>
</html>