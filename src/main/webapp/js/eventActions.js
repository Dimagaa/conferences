const joinEvent = (eventId, userId) => {
    const  url = $('html').attr('data-contextPath') + "/events/action?";
    doEventAction(url, 'joinEvent', {eventId, userId});
}
const leaveEvent = (eventId, userId) => {
    const  url = $('html').attr('data-contextPath') + "/events/action?";
    doEventAction(url, 'leaveEvent', {eventId, userId});
}

const addReport = (eventId, form) => {
    const params =$(form).serialize();
    const url = window.location.pathname + "?"+params;
    doEventAction(url,"addReport", eventId);
}
const updateReport = (eventId, form) => {
    const params =$(form).serialize();
    const url = $('html').attr('data-contextPath') + "/events/action?"+params;
    doEventAction(url,"updateReport", eventId);
}
const deleteReport = (reportId) => {
    doEventAction(window.location.pathname, "deleteReport", reportId);
    $('#report_'+id).remove();
    decReportCounter();
    checkLimit();
}

const proposeSpeaker = (reportId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "proposeSpeaker", reportId);
}
const acceptSpeaker = (reportId, speakerId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "acceptSpeaker", {reportId, speakerId});
}
const rejectSpeaker = (reportId, speakerId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "rejectSpeaker", {reportId, speakerId});
}
const acceptReport = (reportId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "acceptReport", reportId);
}
const rejectReport = (reportId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "rejectReport", reportId);
}
const proposeReport = (topic, speakerId, eventId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "proposeReport", {topic, speakerId, eventId});
}
const deletePropose = (reportId) => {
    const url = $('html').attr('data-contextPath') + "/events/action";
    doEventAction(url, "deleteReport", reportId);
}
const changeReportModalShow = (repEl, modal, repId) => {
    const selectize = speakerSelectize[1].selectize;
    const topic = $(repEl).find('.rep-topic').text().trim();
    const speaker = {
        id: $(repEl).find('.rep-speaker').attr('value'),
        name: $(repEl).find('.rep-speaker-name').text().trim(),
        email: $(repEl).find('.rep-speaker-email').text().trim()
    }
    if(speaker.id > 0) {
        selectize.addOption(speaker);
        selectize.addItem(speaker.id);
    }
    $(modal).find("input[name~='topic']").val(topic);
    $(modal).find("input[name~='reportId']").val(repId);
    $(modal).modal('show');
}

const     doEventAction = (url, action, data) => {
    $.ajax({
        url: url,
        type: 'POST',
        data: {action, data},
        error: function () {
            alert("Cannot do action. Server error")
        },
        success: function (data) {
            window.location.reload();
        }
    });
}
const checkLimit = () => {
    const counter = parseInt($('#reportsCount').text());
    const limit = $('#limit');
    const limitVal = parseInt(limit.val());
    const addBtn = $('#openReportModal');
    if(counter === limitVal) {
        addBtn.prop('disabled', true);
        limit.removeClass("is-invalid")
        return;
    }
    if(counter < limitVal) {
        addBtn.prop('disabled', false);
        limit.removeClass("is-invalid")
        return;
    }
    if(counter > limitVal) {
        addBtn.prop('disabled', true);
        limit.addClass("is-invalid");
    }
}
const removeReport = (id) => {
    $.ajax({
        url: window.location.href + '?action=delete',
        method: 'post',
        data: {id}
    });
    $('#report_'+id).remove();
    decReportCounter();
    checkLimit();
}
const decReportCounter = () => {
    const counter = $('#reportsCount');
    const value = parseInt(counter.text());
    counter.html(value-1);

}
$(document).on('show.bs.modal', '#addReport', function () {
    const title = $('#name').val();
    const start = $('#startTime').val();
    const end = $('#endTime').val();
    const place = $('#place').val();
    const limit = $('#limit').val();
    $.ajax({
        url: $('html').attr('data-contextPath') + '/events/create?action=update',
        method: 'post',
        data: {title, start, end, place, limit}
    })
})

$(function () {
    const startDate = document.getElementById('startTime');
    if(startDate) {
        startDate.min = new Date().toISOString().slice(0, new Date().toISOString().lastIndexOf(":"));
        startDate.addEventListener('change', ev => {
            let endDate = document.getElementById('endTime');
            endDate.min = startDate.value;
        })
    }
});

$(document).on('input', '#limit', function () {checkLimit()});
$(function () {
    if($('#saveBtn').length > 0) {
        validation('saveBtn');
    }
    checkLimit();
});

const initOfferReportForm = (eventId) => {
    const form = $('#offerReportForm').clone(true);
    form.find("input[name~='eventId']").val(eventId);
    form.addClass("needs-validation");
    form.attr("button-target", "offerBtn");
    form.find("input[name~='topic']").attr("for", "offerBtn");
    form.removeClass("d-none");
    formValidationEventListener(form);
    validationEventListener(form.find('.validation'));
    return form;
}
const destroyOfferReportForm = (form) => {
    form.prev().removeClass("d-none")
    form.remove();
}
$(document).on('click', '.offer-rep-btn', function () {
    $(this).addClass("d-none");
    $(this).after(initOfferReportForm(this.value));
});
$(document).on('click', '.offer-rep-save-btn', function () {
    const form = $(this).closest('form');
    const url = $('html').attr('data-contextPath') + "/events/action?"+form.serialize();
    doEventAction(url, "proposeReport", true);
})
$(document).on('click', '.offer-rep-close-btn', function () {
    const form = $(this).closest('form');
    destroyOfferReportForm(form);
});
$(document).on('hide.bs.modal', '.event-card-modal', function () {
    destroyOfferReportForm($(this).find("#offerReportForm"));
})