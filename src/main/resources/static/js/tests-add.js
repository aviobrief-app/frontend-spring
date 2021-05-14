const ROOT_URL = window.location.origin;

const allQuestionsUrl = `${ROOT_URL}/questions/api`;
const testApiUrl = `${ROOT_URL}/tests/api`;


const questionContainer = document.getElementById('questions-container');
const testQuestionList = document.getElementById('question-list');

const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");


function hideSearchBar() {
    const button = document.getElementById('searchbar-toggle');
    const searchAreaEl = document.getElementById('search-container');

    button.addEventListener('click', (e) => {
        e.preventDefault();
        if (!searchAreaEl.classList.contains('show')) {
            searchAreaEl.classList.add('show');
            button.textContent = 'Hide search options'
        } else {
            searchAreaEl.classList.remove('show');
            button.textContent = 'Show search options'
        }
    })
}

hideSearchBar();

function renderAllQuestions() {

    fetch(allQuestionsUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            return response.json();
        }

        const statusCode = response.status;
        const statusMessage = response.statusText;
        const message = `ATA CHAPTERS COULD NOT BE LOADED FROM ${allChaptersUrl}`;

        //todo - implement
        displayRestGetAllError(message);
        return Promise.reject(new Error(message + statusCode + statusMessage))

    })
        .then(questions => questions.forEach(question => renderQuestion(question)))
        .catch(error => console.log(error))

}

renderAllQuestions();

function renderQuestion(question) {

    let id = question.id;
    let questionText = question['question'];
    let documentRef = 'FCOM';
    let documentSubchapter = 'Limitations';
    let ataChapter = 'Aircraft General';

    let questionTemplate =
        `<div class="row mt-2" id="${id}" onclick="transferQuestion(event)">

                    <div class="col-sm-12 shadow rounded  mx-2 p-1  bg-light ">

                        <div class="input-group">
                            <button class="input-group-text add-question">
                                <
                            </button>
                            <div class="col-sm-10 p-2 border border-1 flex-grow-1">
                                <div class="row">
                                    <div class="col text small fw-bold mb-1 " id="question-text">
                                     Q: ${questionText}
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col text text-truncate">
                                        ${documentRef} - ${documentSubchapter}
                                    </div>
                                    <div class="col text  text-truncate mx-2">
                                        ${ataChapter}
                                    </div>
                                </div>
                            </div>
                            <button class="input-group-text remove-question" style="display: none">
                                >
                            </button>
                        </div>

                    </div>
        </div> `;

    questionContainer.innerHTML += questionTemplate;

}

function transferQuestion(e) {
    e.preventDefault();

    let questionEl = e.currentTarget;
    let clickedEl = e.target;


    if (clickedEl.classList.contains('add-question')) {
        testQuestionList.appendChild(questionEl);
        clickedEl.style.display = 'none';
        clickedEl.parentElement.lastElementChild.style.display = 'block';
    }

    if (clickedEl.classList.contains('remove-question')) {
        questionContainer.appendChild(questionEl);
        clickedEl.style.display = 'none';
        clickedEl.parentElement.firstElementChild.style.display = 'block';
        clickedEl.parentElement.firstElementChild.classList.add('bg-secondary')
    }
}

function addTag() {

    const tagDropdownEl = document.getElementById('test-tags');
    const addTagBtn = document.getElementById('add-test-tag');
    const tagContainer = document.getElementById('tags-container');

    addTagBtn.addEventListener('click', (e) => {

        let selectedIndex = tagDropdownEl.selectedIndex;
        let tagName = tagDropdownEl.options[selectedIndex].text;

        if (selectedIndex !== 0) {

            //tag div
            let tagDiv = document.createElement('div');
            tagDiv.className = "my-2 mx-1 float-start";

            //tag inputDiv
            let inputDiv = document.createElement('div');
            inputDiv.className = "input-group";

            //tag text Div + inner Div
            let tagTextDiv = document.createElement('div');
            tagTextDiv.className = "p-2 border border-1";
            let tagTestInnerDiv = document.createElement('div');
            tagTestInnerDiv.className = "text text-truncate small test-tag";
            tagTestInnerDiv.textContent = tagName;
            tagTextDiv.appendChild(tagTestInnerDiv);

            //buttonDiv
            let closeTagBtn = document.createElement('button');
            closeTagBtn.className = "input-group-text";
            closeTagBtn.textContent = 'x';

            //fill tag div
            inputDiv.appendChild(tagTextDiv);
            inputDiv.appendChild(closeTagBtn);
            tagDiv.appendChild(inputDiv);

            //attach to container
            tagContainer.appendChild(tagDiv)

            //remove tag button event
            closeTagBtn.addEventListener('click', (e) => {
                tagDropdownEl.selectedIndex = -1;
                tagDiv.remove();
            })

            // <div className="my-2 mx-1 float-start">
            //     <div className="input-group">
            //         <div className="p-2 border border-1">
            //             <div className="text text-truncate small test-tag">
            //                 FCOM
            //             </div>
            //         </div>
            //         <button className="input-group-text">
            //             x
            //         </button>
            //     </div>
            // </div>

        }


    })


}

addTag();

function postTestAndHandleErrors() {

    const saveBtn = document.getElementById('add-test');

    saveBtn.addEventListener('click', (e) => {

        let nameEl = document.getElementById('test-name');
        let dueDateEl = document.getElementById('due-date');
        let questionIds = [];
        let testTags = Array.from(document.getElementsByClassName('test-tag'))
            .map(element => element.textContent);

        //get question list from page
        Array.from(document
            .getElementById('question-list').children)
            .forEach(child => questionIds.push(child.id));

        //todo - validate inputs for test adding

        let addTestObj = JSON.stringify({
            name: nameEl.value,
            dueDate: dueDateEl.value,
            questionIds: questionIds,
            testTagEnums: testTags
        })
        console.log(addTestObj);


        fetch(`${testApiUrl}`, {

            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: addTestObj
        })
            .then(response => {

                if (response.ok || response.status === 422) {
                    return response.json();
                }

                return Promise.reject(response.json());

            })
            .then(data => {

                console.log(data)

                if (data['errors']) {

                    let errors = {};

                    data['errors'].forEach(error => {

                        let field = error['fieldName']
                        let errMsg = error['messageError'];

                        errors[field]
                            ? errors[field].push(errMsg)
                            : errors[field] = [errMsg]

                    })

                    let nameErrEl = document.getElementById('test-name-err');
                    if (errors.name) {
                        nameErrEl.innerText = errors['name'].join('\n');
                        nameEl.classList.add('border', 'border-danger');
                    } else {
                        nameErrEl.textContent = '';
                        nameEl.classList.remove('border', 'border-danger');
                    }

                    let dueDateErrEl = document.getElementById('due-date-err');

                    if (errors.dueDate) {
                        dueDateErrEl.innerText = errors['dueDate'].join('\n');
                        dueDateEl.classList.add('border', 'border-danger');
                    } else {
                        dueDateErrEl.textContent = '';
                        dueDateEl.classList.remove('border', 'border-danger');
                    }

                    //todo - handle no questions and the other error


                } else {
                    console.log('no errors')
                    // todo - remove
                }
            })
            .catch(error => {

                console.log(error)

            });
    })

}

postTestAndHandleErrors();

