function subscribe() {
    let request = {
        "email": $('#subscribe-email').val(),
    };

    if (request.email.length == 0) {
        alert("An email is required.");
        return;
    }

    $.ajax({
        type: 'POST',
        url: "/subscribe/valid",
        dataType: 'json',
        data: JSON.stringify(request),
        contentType: 'application/json; charset=utf-8'
    }).done(function (response) {
        if (response.success) {
            const data = response.data;
            if (data.empty) {
                doSubscribe(request);
                return;
            }

            unSubscribe(request);
            return;
        }
        alert('Fail to subscribe request. Please try again.');
    }).fail(function (error) {
        let responseJson = error.responseJSON;
        alert(responseJson.message + "\n(Fail to subscribe request. Please contact the administrator.)");
    });
}

function doSubscribe(request) {
    $.ajax({
        type: 'POST',
        url: "/subscribe",
        dataType: 'json',
        data: JSON.stringify(request),
        contentType: 'application/json; charset=utf-8'
    }).done(function (response) {
        if (response.success) {
            alert('success to subscribe request.');
            return;
        }
        alert('Fail to subscribe request. Please try again.');
    }).fail(function (error) {
        let responseJson = error.responseJSON;
        alert(responseJson.message + "\n(Fail to subscribe request. Please contact the administrator.)");
    });
}

function unSubscribe(request) {
    //
    if (!confirm("This mail is in the subscription status.\nDo you want to unsubscribe?")) {
        return;
    }

    $.ajax({
        type: 'POST',
        url: "/subscribe/unsubscribe",
        dataType: 'json',
        data: JSON.stringify(request),
        contentType: 'application/json; charset=utf-8'
    }).done(function (response) {
        if (response.success) {
            alert('success to unsubscribe request.');
            return;
        }
        alert('Fail to unsubscribe request. Please try again.');
    }).fail(function (error) {
        let responseJson = error.responseJSON;
        alert(responseJson.message + "\n(Fail to unsubscribe request. Please contact the administrator.)");
    });
}