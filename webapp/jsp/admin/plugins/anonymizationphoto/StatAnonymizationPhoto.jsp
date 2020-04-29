<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:useBean id="statAnonymizationphoto" scope="session"
	class="fr.paris.lutece.plugins.anonymizationphoto.web.StatAnonymizationPhotoJspBean" />

<jsp:include page="../../AdminHeader.jsp" />
<% statAnonymizationphoto.init( request, statAnonymizationphoto.RIGHT_STAT_ANONYMIZATION ); %>
<%= statAnonymizationphoto.getStats( request ) %>
<%@ include file="../../AdminFooter.jsp"%>