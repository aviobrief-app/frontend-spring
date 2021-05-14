const ROOT_URL = window.location.origin;


let elements = {
    allDocSubchaptersUrl: `${ROOT_URL}/document-subchapters/api`,
    allATASubchaptersUrl: `${ROOT_URL}/ata-subchapters/api`,
    allArticlesUrl: `${ROOT_URL}/articles/api`,

    documentDropdown: () => document.getElementById('document'),
    subChapterDropdown: () => document.getElementById('documentSubchapter'),
    ataChapterDropdown: () => document.getElementById('chapter'),
    ataSubChapterDropdown: () => document.getElementById('ataSubChapter'),
    articleDropdown: () => document.getElementById('article'),

}


function populateDocSubchapterDropdown() {

    fetch(elements.allDocSubchaptersUrl)
        .then(response => {

            if (response.ok) {
                return response.json();
            }

            throw new Error(response.status + response.text());

        })
        .then(data => {

           // console.log(data)

            elements.documentDropdown().addEventListener('change', () => filterAndFillDocSubchapterDropdown(data));

        })
        .catch(error => {
            console.log(error)
        })
}

function filterAndFillDocSubchapterDropdown(subchapterList) {

    elements.subChapterDropdown().innerHTML = '<option></option>';

    const selectedIndex = elements.documentDropdown().selectedIndex;
    const documentName = elements.documentDropdown().options[selectedIndex].text;
    const subchapterListFiltered = subchapterList.filter(subchapter => subchapter.documentRef === documentName);

    subchapterListFiltered.forEach(subchapter => {
        elements.subChapterDropdown().innerHTML += `<option>${subchapter.name}</option>`;
    })
}

populateDocSubchapterDropdown();



function populateAtaSubchaptersDropdown() {

    fetch(elements.allATASubchaptersUrl)
        .then(response => {

            if (response.ok) {
                return response.json();
            }

            return Response.redirect(new Error(response.status + response.text()));

        })
        .then(data => {

            elements.ataChapterDropdown().addEventListener('change', () => filterAndFillATASubchaptersDropdown(data));

        })
        .catch(error => {
            console.log(error)
        })
}

function filterAndFillATASubchaptersDropdown(ataSubchapterList) {

    elements.ataSubChapterDropdown().innerHTML = '<option></option>';

    const selectedIndex = elements.ataChapterDropdown().selectedIndex;
    const ataCode = Number(elements.ataChapterDropdown().options[selectedIndex].text.split(' - ')[0]);

    const subchapterListFiltered = ataSubchapterList.filter(subchapter => subchapter.ataChapter === ataCode);

    subchapterListFiltered.forEach(subchapter => {

        elements.ataSubChapterDropdown().innerHTML +=
            `<option>${subchapter.ataSubCode} - ${subchapter.subchapterName}</option>`;
    })

}


populateAtaSubchaptersDropdown();



function populateArticleDropdown() {

    fetch(elements.allArticlesUrl)
        .then(response => {

            if (response.ok) {
                return response.json();
            }

            return Response.redirect(new Error(response.status + response.text()));

        })
        .then(data => {

            data.forEach(article => {

                elements.articleDropdown().innerHTML +=
                    `<option>${article.title}</option>`;
            })

            //todo - implement filtering when selected
            elements.documentDropdown().addEventListener('change', () => filterArticleDropdownByDocument(data));
            elements.ataChapterDropdown().addEventListener('change', () => filterArticleDropdownByAtaChapter(data));

        })
        .catch(error => {
            console.log(error)
        })
}

function filterArticleDropdownByDocument(data) {
    console.log(data);
}

function filterArticleDropdownByAtaChapter(data) {
    console.log(data);
}

populateArticleDropdown();

