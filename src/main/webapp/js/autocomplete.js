const getData = (query, callback, target) => {
    if (!query.length || query.length < 3) return callback();
    $.ajax({
        url: $('html').attr('data-contextPath') + '/autocomplete', type: 'POST', dataType: 'json', data: {
            query, target
        }, error: function () {
            callback();
        }, success: function (res) {
            callback(res);
        }
    });
};
const storeSelectedSpeakers = (speaker) => {
    if (!speaker) {
        return;
    }
    $.ajax({
        url: $('html').attr('data-contextPath') + "/autocomplete",
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(speaker),
    });
};
$(document).on('input', '.autocomplete', function () {
    const dataList = this.parentNode.querySelector('datalist');
    let out = "";
    const target = this.getAttribute("name");
    const callback = (data) => {
        if(!data) {
            return;
        }
        for(let i = 0; i < data.length; i++) {
            out += "<option value='" + data[i].value + "'></option>"
        }
        dataList.innerHTML = out;
    };
    const item = dataList.children.item(0);
    if(item && item.getAttribute("value").includes(this.value)) {
        return;
    }
    getData(this.value, callback, target)
});
const speakerSelectize = $('.speaker-autocomplete').selectize({
    valueField: 'id',
    labelField: 'name',
    searchField: ['name'],
    highlight: false,
    maxItems: 1,
    loadThrottle: 300,
    onItemAdd: function () {
        const selected = (this.$control[0]).children;

        Array.from(selected).filter(function (el) { return el.hasAttribute("data-value")}) .forEach( function (el) {

            const speaker = {
                id: el.getAttribute("data-value"),
                name: el.querySelector('.name').innerText ,
                email: el.querySelector('.email').innerText
            };
            storeSelectedSpeakers(speaker);
        })
    },
    render: {
        item: function (item, escape) {
            return "<div>" +
                (item.name ? '<span class="name speaker-option">' + escape(item.name) + "</span>" : "") + (item.email ? '<span class="email speaker-option text-black-50 fst-italic"> ' + escape(item.email) + "</span>" : "") +
                "<input type='hidden' name='speakerName' value='" + item.name + " '>" +
                "<input type='hidden' name='speakerEmail' value='" + item.email + " '>" +
                "</div>";
        }, option: function (item, escape) {
            const label = item.name || item.email;
            const caption = item.name ? item.email : null;
            return "<div class='border-bottom'>" + '<span class="label speaker-option">' + escape(label) + "</span>" + (caption ? '<span class="caption speaker-option text-black-50 fst-italic">' + escape(caption) + "</span>" : "") + "</div>";
        },
    },
    load: function (query, callback) {
        getData(query, callback, 'speaker')
    }
});
$('.events-place-autocomplete').selectize({
    valueField: 'value',
    labelField: 'value',
    searchField: ['value'],
    highlight: false,
    maxItems: 1,
    loadThrottle: 300,
    load: function (query, callback) {
        getData(query, callback, 'place')
    }
});
$('.simple-select').selectize({
    valueField: 'value',
    labelField: 'label',
    maxItems:1,
});