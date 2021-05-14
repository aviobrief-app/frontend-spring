const ROOT_URL = window.location.origin;
const BASE_URL = 'documents/api';
const allDocumentsUrl = `${ROOT_URL}/${BASE_URL}`;

function renderAllDocuments() {

    const documentsContainer = document.getElementById('documents-container');
    documentsContainer.innerHTML = '';

    fetch(allDocumentsUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {

            if (response.ok) {
                return response.json();
            } else {
                const message = `Documents COULD NOT BE LOADED FROM ${allDocumentsUrl}`;
                displayRestGetAllError(message);
                return Promise.reject(new Error(message + response.status + response.statusText));
            }

        })
        .then(documents => {

            documents.length > 0
                ? documents.forEach(document => renderChapter(document))
                : documentsContainer.innerHTML = '<p class="text text-danger">No Documents in Database</p>';

        })
        .catch(error => {
            displayRestGetAllError(`Documents COULD NOT BE LOADED FROM ${allDocumentsUrl}`);
            console.log(error)
        });

    function renderChapter({id, name}) {


        //new tr
        let newTr = document.createElement('tr');
        newTr.classList.add('d-grid','gap-1')
        newTr.dataset.key = id;

        //error element
        let nameErrorEl = document.createElement('div');
        nameErrorEl.classList.add('text', 'small', 'text-danger');
        nameErrorEl.id = `${id}-name-err`;


        //new chapter name td
        let newNameTd = document.createElement('td');
        let nameDiv = document.createElement('div');
        nameDiv.classList.add('d-inline')
        nameDiv.textContent = name;
        newNameTd.appendChild(nameDiv)


        //edit button
        let newEditBtn = document.createElement('button');
        newEditBtn.classList.add('btn', 'btn-outline-dark', 'mx-2', 'btn-sm', 'float-end');
        newEditBtn.innerText = 'Edit';
        newEditBtn.addEventListener('click',
            (e) => renderEditForm(e, newNameTd, nameDiv, name, newEditBtn, id, nameErrorEl));

        //add button to nameTd
        newNameTd.appendChild(newEditBtn);

        //append data
        newTr.appendChild(nameErrorEl);
        newTr.appendChild(newNameTd);

        documentsContainer.appendChild(newTr)

    }

    function renderEditForm(e, nameTdElement, nameDivElement, currDocName, editBtn, id, nameErrorEl) {
        e.preventDefault();

        //new inputEl
        let inputEl = document.createElement('input');
        inputEl.type = 'text';
        inputEl.classList.add('d-inline', 'border');
        inputEl.value = currDocName;

        //remove old name div
        nameTdElement.firstElementChild.remove();

        //append new input element
        nameTdElement.insertBefore(inputEl, editBtn);

        //remove edit btn
        nameTdElement.lastElementChild.remove();

        //add two new buttons - confirm and cancel

        //cancel button
        let cancelBtn = document.createElement('button');
        cancelBtn.classList.add('btn', 'btn-outline-danger', 'mx-2', 'btn-sm', 'float-end');
        cancelBtn.textContent = 'Cancel';
        nameTdElement.appendChild(cancelBtn);
        cancelBtn.addEventListener("click", (e) => {

            e.preventDefault();
            showNameDivAndEditButtonAgain(nameTdElement, nameDivElement, editBtn, nameErrorEl, inputEl);

        });

        //confirm button
        let confirmBtn = document.createElement('button');
        confirmBtn.classList.add('btn', 'btn-outline-success', 'mx-2', 'btn-sm', 'float-end');
        confirmBtn.textContent = 'Confirm';
        nameTdElement.appendChild(confirmBtn);
        confirmBtn.addEventListener('click', (e) => checkInputAndPostChanges(e, id, inputEl, nameErrorEl))

        //EO. add two new buttons - confirm and cancel


    }

    function checkInputAndPostChanges(e, id, inputEl, nameErrorEl) {
        e.preventDefault();

        //check input and display error message
        if (inputEl.value == null || [...inputEl.value].length < 0) {// todo [...inputEl.value].length < 1 - change to 3

            nameErrorEl.innerText = 'Document name must be min 3 characters!';
            inputEl.classlist.add('border-danger');

            return;
        }


        //all ok, continue
        let newName = inputEl.value;

        let editObj = JSON.stringify({
            id: id,
            name: newName,
        });

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        fetch(`${allDocumentsUrl}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: editObj
        })
            .then(response => {

                if (response.ok || response.status === 422) {
                    return response.json();
                }

                return Promise.reject(new Error("Error getting answer from server"));

            })
            .then(data => {

                let nameErrorElement = document.getElementById(`${id}-name-err`);
                if (data.status === 'BAD_REQUEST' && data.errors) {

                    let nameErrorMessages = data.errors.map(err => err['messageError'].trim());
                    nameErrorElement.innerText = nameErrorMessages.join('\n');
                    inputEl.classList.add('border-danger')

                } else {

                    inputEl.classList.remove('border-danger');
                    nameErrorElement.innerText = '';
                    displaySuccessPopUpWindow(`${newName} successfully saved!`);
                    renderAllDocuments();

                }

            })
            .catch(error => console.log(err));

    }

    function showNameDivAndEditButtonAgain(nameTdElement, nameDivElement, editBtn, nameErrorElement, inputEl) {

        nameErrorElement.innerText = '';
        inputEl.classList.remove('border-danger');


        nameTdElement.lastElementChild.remove();
        nameTdElement.lastElementChild.remove();
        nameTdElement.lastElementChild.remove();

        nameTdElement.appendChild(nameDivElement);
        nameTdElement.appendChild(editBtn);

    }

    function displaySuccessPopUpWindow(...messages) {

        document.getElementById('success-message').textContent = messages.join('\n')
        $(document).ready(function () {
            $('#seed-ok').toast('show');
        });
    }

    function displayRestGetAllError(message) {
        documentsContainer.innerText = message;
    }
}

renderAllDocuments();

