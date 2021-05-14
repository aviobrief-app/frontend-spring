function highlightMenuLink() {

    window.addEventListener('load', highlightLink);

}

highlightMenuLink();

function highlightLink(){

    let currentPath = window.location.pathname;

    let targetPath = currentPath.split('/')[1];

    let correspondingLink = document.getElementById(targetPath);

    if(correspondingLink){
        correspondingLink.classList.remove('link-muted');
        correspondingLink.classList.add('link-selected')
    }
}

