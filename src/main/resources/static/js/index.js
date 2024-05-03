let categories = [];
let page = 0;
let totalPage = 0;
let totalElements = 0;
const pageSize = 10;

$(function() {
    addPage(page++);
});

document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(chk => {
        chk.addEventListener('change', function() {
            handleCheckboxChange();
        });
    });

    function handleCheckboxChange() {
        const checkedBoxes = document.querySelectorAll('input[type="checkbox"]:checked');
        let selectedItems = [];
        checkedBoxes.forEach(chk => {
            selectedItems.push(chk.value);
        });

        categories = selectedItems;
        initPage();
        addPage(page++);
    }
});

function initPage() {
    page = 0;
    const releases = document.getElementById('releases-area');
    while (releases.firstChild) {
        releases.removeChild(releases.firstChild);
    }
}

function getDocumentHeight() {
    const body = document.body;
    const html = document.documentElement;

    return Math.max(
        body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight
    );
}

function getScrollTop() {
    return (window.scrollY !== undefined) ?
        window.scrollY :
        (document.documentElement || document.body.parentNode || document.body).scrollTop;
}

function generateTitleArea(data) {
    const linkArea = document.createElement('a');
    linkArea.href = data.url;
    linkArea.target = "_blank";

    const titleArea = document.createElement('h2');
    titleArea.className = 'post-title';
    titleArea.innerText = data.project + ' ' + data.version;
    linkArea.appendChild(titleArea);

    if (data.tags.length > 0) {
        data.tags.forEach(tag => {
            const tagSpan = document.createElement('span');
            tagSpan.className = 'badge badge-pill badge-info';
            tagSpan.innerText = tag;
            linkArea.appendChild(tagSpan);
        })
    }

    const subtitleArea = document.createElement('h3');
    subtitleArea.className = 'post-subtitle';
    subtitleArea.innerText = data.version;
    linkArea.appendChild(subtitleArea);

    return linkArea;
}

function generateMetaInfoArea(data) {
    const metaInfoArea = document.createElement('p');
    metaInfoArea.className = 'post-meta';
    metaInfoArea.innerText = data.createdDt;

    return metaInfoArea
}

function generateHrArea(postArea) {
    const hrArea = document.createElement('hr');
    hrArea.className = 'my-4';

    return hrArea;
}

function getPost(data) {
    const postArea = document.createElement('div');
    postArea.className = 'post-preview';

    const linkArea = generateTitleArea(data);
    const metaInfoArea = generateMetaInfoArea(data);
    const hrArea = generateHrArea(data);

    postArea.appendChild(linkArea);
    postArea.appendChild(metaInfoArea);
    postArea.appendChild(hrArea);

    return postArea;
}

function addPage(page) {
    $.ajax({
        type: 'GET',
        url: '/releases',
        async: false,
        data: {
            categories: categories,
            page: page,
            size: pageSize,
        },
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    }).done(function (result) {
        if (result.success) {
            let data = result.data;
            totalPage = data.totalPages;
            totalElements = data.totalElements;
            $('#total-count').html(totalElements);

            if (data.empty) {
                $('#loading').css('display', 'none');
            }

            let releases = data.content;
            $('#no-data').css('display', 'none');
            if (releases.length == 0 && page == 0) {
                $('#no-data').css('display', 'block');
                return;
            }

            let releasesArea = document.getElementById('releases-area');
            releases.forEach(release => {
                let post = getPost(release);
                releasesArea.appendChild(post);
            })

            return;
        }

        alert("페이지 로드에 실패하였습니다. 다시 시도해 주세요.")
    }).fail(function (error) {
        let responseJson = error.responseJSON;
        alert("페이지 로드에 실패하였습니다. 다시 시도해 주세요.\n(" + responseJson.message + ")");
    })
}

window.onscroll = function () {
    if ((getScrollTop() + 300) < getDocumentHeight() - window.innerHeight) return;
    // 스크롤이 페이지 하단에 도달할 경우 새 페이지 로드
    if (totalPage == page) {
        $('#loading').css('display', 'none');
        return;
    }

    addPage(page++);
};