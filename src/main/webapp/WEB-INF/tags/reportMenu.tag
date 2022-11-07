<%@ tag pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>
<%@ attribute name="report" required="true" rtexprvalue="true" type="com.webapp.conferences.dto.ReportDto" %>

<ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}">
    <h6 class="text-center">${report.status}</h6>
    <ul class="list-group">
        <ctl:accessRule contextRules="UNDETAILED" context="${report.status}">
            <c:forEach var="speaker" items="${report.proposedSpeakers}">
                <li id="reportRequest${report.id}"
                    class="list-group-item list-group-item-action d-flex justify-content-between align-items-start">
                    <div class=" me-auto w-100">
                        <div class="rep-speaker" value="${speaker.id}">
                            <span class="rep-speaker-name">${speaker.firstName} ${speaker.lastName}</span>
                            <span class="fst-italic text-black-50 rep-speaker-email">${speaker.login}</span>
                        </div>
                    </div>
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-outline-success" onclick="acceptSpeaker(${report.id}, ${speaker.id})">Accept</button>
                        <button type="button" class="btn btn-outline-danger" onclick="rejectSpeaker(${report.id}, ${speaker.id})">Reject</button>
                    </div>
                </li>
            </c:forEach>
        </ctl:accessRule>
        <ctl:accessRule contextRules="CONSOLIDATED UNCONFIRMED" context="${report.status}">
            <li id="reportRequest${report.id}"
                class="list-group-item list-group-item-action d-flex justify-content-between align-items-start">
                <div class=" me-auto w-100">
                    <div class="rep-speaker" value="${report.id}">
                        <span class="rep-speaker-name">${report.speakerName}</span>
                        <span class="fst-italic text-black-50 rep-speaker-email">${report.speakerEmail}</span>
                    </div>
                </div>
                <div class="btn-group" role="group">
                    <form id="dismissReport">
                        <input name="reportId" type="hidden" value="${report.id}">
                        <input type="hidden" name="topic" value="${report.topic}">
                        <input type="hidden" name="speaker" value="0">
                        <button type="button" class="btn btn-outline-danger"
                                onclick="updateReport(${requestScope.event.id}, '#dismissReport')">Dismiss
                        </button>
                    </form>
                </div>
            </li>
        </ctl:accessRule>
    </ul>
</ctl:accessRule>
<ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}">
    <ul class="list-group">
        <li id="reportRequest${report.id}"
            class="list-group-item list-group-item-action d-flex justify-content-center align-items-center">
            <ctl:accessRule contextRules="UNDETAILED" context="${report.status}">
                <ctl:accessRule contextRules="false" context="${report.requested}">
                    <button type="button" class="btn btn-outline-dark"
                            onclick="proposeSpeaker(${report.id})">Propose yourself
                    </button>
                </ctl:accessRule>
                <ctl:accessRule contextRules="true" context="${report.requested}">
                    <button class="btn btn-outline-success" disabled>Requested
                    </button>
                </ctl:accessRule>
            </ctl:accessRule>
            <ctl:accessRule contextRules="UNCONFIRMED CONSOLIDATED" context="${report.status}">

                <div class="rep-speaker" value="${report.id}">
                    <span class="rep-speaker-name">${report.speakerName}</span>
                    <span class="fst-italic text-black-50 rep-speaker-email">${report.speakerEmail}</span>
                </div>
            </ctl:accessRule>
        </li>
    </ul>
</ctl:accessRule>

